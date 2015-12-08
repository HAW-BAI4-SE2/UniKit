package models.commonUtils;

/**
 * @author Thomas Bednorz
 */

import assets.Global;
import models.commonUtils.Exceptions.*;
import models.commonUtils.ID.CourseID;
import models.commonUtils.ID.StudentNumber;
import models.commonUtils.ID.TeamID;
import net.unikit.database.exceptions.ConstraintViolationException;
import net.unikit.database.exceptions.EntityNotFoundException;
import net.unikit.database.interfaces.entities.*;
import net.unikit.database.interfaces.managers.*;

import java.util.ArrayList;
import java.util.List;

public class CommonDatabaseUtils {

    private static TeamManager teamManager;
    private static StudentManager studentManager;
    private static TeamInvitationManager invitationManager;
    private static CourseManager courseManager;
    private static TeamRegistrationManager registrationManager;
    private static MembershipRequestManager membershipRequestManager;
    private static CourseRegistrationManager courseRegistrationManager;

    static{
        invitationManager = Global.getTeamInvitationManager();
        teamManager = Global.getTeamManager();
        studentManager = Global.getStudentManager();
        courseManager = Global.getCourseManager();
        registrationManager = Global.getTeamRegistrationManager();
        membershipRequestManager = Global.getMembershipRequestManager();
        courseRegistrationManager = Global.getCourseRegistrationManager();
    }

    /**
     *
     * @param sNumber
     * @param tID
     */
    public static void removeStudentFromTeam(StudentNumber sNumber, TeamID tID) throws StudentNotFoundException, TeamNotFoundException {
        // Get student to be removed
        Student studentToBeRemoved = getStudent(sNumber);

        // Get team from which the student is to be removed
        Team associatedTeam = getTeam(tID);

        try{
            for(TeamRegistration currentRegistration: registrationManager.getAllEntities()){
                if(currentRegistration.getTeam().equals(associatedTeam) && currentRegistration.getStudent().equals(studentToBeRemoved)){
                    registrationManager.deleteEntity(currentRegistration);
                    break;
                }
            }
        } catch (Exception e){
            // TODO: Error handling
            e.printStackTrace();
        }
    }


    /**
     *  Creates a team associated with the studentNumber and courseID,updates the registration status and returns the teamID
     *  @param sNumber the first member of the team
     *  @param cID the course ID for which the team will be created
     *  @return returns the ID for the new team
     */
    public static TeamID createTeam(StudentNumber sNumber, CourseID cID) throws CourseNotFoundException, StudentNotFoundException, FatalErrorException, TeamExistsException {
        // Get course object
        Course course = getCourse(cID);

        // Get student object
        Student student = getStudent(sNumber);

        // Create new team
        Team newTeam = teamManager.createEntity();
        newTeam.setCourse(course);
        newTeam.setCreatedBy(student);
        try {
            teamManager.addEntity(newTeam);
        } catch (ConstraintViolationException e) {
            throw new TeamExistsException(sNumber, cID);
        }

        // Register student with team
        TeamRegistration newTeamRegistration = registrationManager.createEntity();
        newTeamRegistration.setTeam(newTeam);
        newTeamRegistration.setStudent(student);
        try {
            registrationManager.addEntity(newTeamRegistration);
        } catch (ConstraintViolationException e) {
            new StudentInTeamException(sNumber);
        }

        // Update registration status of student
        changeTeamRegistrationStatus(sNumber,cID,true);

        // Get the new team
        Team createdTeam = null;
        try {
            createdTeam = getTeamByStudentAndCourse(sNumber,cID);
        } catch (TeamNotFoundException e) {
            throw new FatalErrorException("Error while creating the team");
        }
        return TeamID.get(createdTeam.getId());
    }

    /**
     *
     * @param tID the ID of the team that will be deleted
     * @return the ID for the associated course
     * @throws TeamNotFoundException
     */
    public static CourseID deleteTeam(TeamID tID) throws TeamNotFoundException, TeamDeletedException {
        Team teamToBeDeleted = getTeam(tID);
        CourseID courseIDForTeam = null;
        try {
            courseIDForTeam = CourseID.get(teamToBeDeleted.getCourse().getId());
        } catch (EntityNotFoundException e) {
            //TODO: Error handling
            e.printStackTrace();
        }

        // Update students to single registration
        try{
            for(TeamRegistration currentRegistration: teamToBeDeleted.getTeamRegistrations()){
                changeTeamRegistrationStatus(
                        StudentNumber.get(currentRegistration.getStudent().getStudentNumber()),
                        CourseID.get(currentRegistration.getTeam().getCourse().getId()),
                        false);
            }
        } catch (Exception e){
            // TODO: Error handling
            e.printStackTrace();
        }

        //TODO: delete all membership requests
        //TODO: delete all invites
        
        //Delete team
        try {
            teamManager.deleteEntity(teamToBeDeleted);
        } catch (EntityNotFoundException e) {
            throw new TeamDeletedException(tID);
        }

        return courseIDForTeam;
    }

    /**
     * Stores the new Membershiprequest n the Database
     * @param sNumber the student number of the student who requested membership with the team
     * @param tID the ID of the team that received the membership request
     * @throws TeamNotFoundException
     * */
    public static void storeMembershipRequest(StudentNumber sNumber, TeamID tID) throws TeamNotFoundException, StudentNotFoundException, MembershipRequestExistsException {
        // Get team for the membership request
        Team team = getTeam(tID);

        // Get requesting student
        Student student = getStudent(sNumber);

        // Create membership request
        MembershipRequest newMembershipRequest = membershipRequestManager.createEntity();
        newMembershipRequest.setApplicant(student);
        newMembershipRequest.setTeam(team);
        try {
            membershipRequestManager.addEntity(newMembershipRequest);
        } catch (ConstraintViolationException e) {
            throw new MembershipRequestExistsException(sNumber,tID);
        }
    }
    

    public static List<Student> getAllStudents(Team team) {

        //Get all registered students
        List<Student> allStudentsInTeam = new ArrayList<>();
        for(TeamRegistration currentRegistration : team.getTeamRegistrations()){
            try {
                Student s = currentRegistration.getStudent();
                allStudentsInTeam.add(s);
            } catch (EntityNotFoundException e) {
                //TODO Error handling
                e.printStackTrace();
            }
        }

        return allStudentsInTeam;
    }

    public static List<Student> getAllStudents(Course associatedCourse) {
        List<Student> allStudentsInCourse = new ArrayList<>();
        for(CourseRegistration currentCourseRegistration : courseRegistrationManager.getAllEntities()){
            try {
                if(currentCourseRegistration.getCourse().equals(associatedCourse)){
                    allStudentsInCourse.add(currentCourseRegistration.getStudent());
                }
            } catch (EntityNotFoundException e) {
                //TODO Error handling
                e.printStackTrace();
            }
        }

        return allStudentsInCourse;

    }



    public static List<Student> getAllMembershipRequests(Team team) {
        List<Student> allRequestingStudents = new ArrayList<>();

        for(MembershipRequest currentMembershipRequest : membershipRequestManager.getAllEntities()){
            if(currentMembershipRequest.getTeam().equals(team)){
                try {
                    allRequestingStudents.add(currentMembershipRequest.getApplicant());
                } catch (EntityNotFoundException e) {
                    // TODO: Handle this internal database error (erroneous foreign keys)
                    e.printStackTrace();
                }
            }
        }
        return allRequestingStudents;
    }

    public static List<Student> getAllInvites(Team team) {
        List<Student> allInvitedStudents = new ArrayList<>();

        for(TeamInvitation currentInvitation : invitationManager.getAllEntities()){
            if(currentInvitation.getTeam().equals(team)){
                try {
                    allInvitedStudents.add(currentInvitation.getInvitee());
                } catch (EntityNotFoundException e) {
                    // TODO: Handle this internal database error (erroneous foreign keys)
                    e.printStackTrace();
                }
            }
        }
        return allInvitedStudents;
    }

    public static List<Team> getAllTeams(CourseID courseID) throws CourseNotFoundException {
        Course course = getCourse(courseID);
        List<Team> allTeamsForCourse = new ArrayList<>();

        for(Team currentTeam : teamManager.getAllEntities()){
            try {
                if(currentTeam.getCourse().equals(course)){
                    allTeamsForCourse.add(currentTeam);
                }
            } catch (EntityNotFoundException e) {
                // TODO: Handle this internal database error (erroneous foreign keys)
                e.printStackTrace();
            }
        }
        return allTeamsForCourse;
    }

    public static List<Team> getAvailableTeams(CourseID courseID) throws CourseNotFoundException {
        Course course = getCourse(courseID);
        List<Team> allAvailableTeams = new ArrayList<>();

        for(Team currentTeam : teamManager.getAllEntities()){
            try {
                if(currentTeam.getCourse().equals(course) &&
                        currentTeam.getTeamRegistrations().size() < course.getMaxTeamSize()){
                    allAvailableTeams.add(currentTeam);
                }
            } catch (EntityNotFoundException e) {
                // TODO: Handle this internal database error (erroneous foreign keys)
                e.printStackTrace();
            }
        }

        return allAvailableTeams;
    }

    public static boolean isStudentMember(StudentNumber sNumber, TeamID tID) throws StudentNotFoundException, TeamNotFoundException {
        Student student = getStudent(sNumber);
        Team team = getTeam(tID);

        for(TeamRegistration currentRegistration : team.getTeamRegistrations()){
            try {
                if(currentRegistration.getStudent().equals(student)){
                    return true;
                }
            } catch (EntityNotFoundException e) {
                // TODO: Handle this internal database error (erroneous foreign keys)
                e.printStackTrace();
            }
        }

        return false;
    }

    public static boolean isMemberShipRequested(StudentNumber sNumber, TeamID tID) throws StudentNotFoundException, TeamNotFoundException {
        Student student = getStudent(sNumber);
        Team team = getTeam(tID);

        for(MembershipRequest currentMembershipRequest : team.getMembershipRequests()){
            try {
                if(currentMembershipRequest.getTeam().equals(team) &&
                        currentMembershipRequest.getApplicant().equals(student)){
                    return true;
                }
            } catch (EntityNotFoundException e) {
                // TODO: Handle this internal database error (erroneous foreign keys)
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean isMaxSizeReached(TeamID tID) throws TeamNotFoundException {
        Team team = getTeam(tID);
        try {
            if( ( team.getTeamRegistrations().size() + team.getTeamInvitations().size() ) >= team.getCourse().getMaxTeamSize()){
                return true;
            }
        } catch (EntityNotFoundException e) {
            // TODO: Handle this internal database error (erroneous foreign keys)
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isStudentInvited(StudentNumber sNumber, TeamID tID) throws StudentNotFoundException, TeamNotFoundException {
        Student student = getStudent(sNumber);
        Team team = getTeam(tID);

        for(TeamInvitation currentInvitation : team.getTeamInvitations()){
            try {
                if(currentInvitation.getInvitee().equals(student)){
                    return true;
                }
            } catch (EntityNotFoundException e) {
                // TODO: Handle this internal database error (erroneous foreign keys)
                e.printStackTrace();
            }
        }
        return false;
    }

    public static List<MembershipRequest> getAllMembershipRequests(StudentNumber sNumber) throws StudentNotFoundException {
        Student student = getStudent(sNumber);

        List<MembershipRequest> allMembershipRequests = new ArrayList<>();
        for(MembershipRequest currentMembershipRequest : membershipRequestManager.getAllEntities()){
            try {
                if(currentMembershipRequest.getApplicant().equals(student)){
                    allMembershipRequests.add(currentMembershipRequest);
                }
            } catch (EntityNotFoundException e) {
                // TODO: Handle this internal database error (erroneous foreign keys)
                e.printStackTrace();
            }
        }

        return allMembershipRequests;
    }

    public static List<MembershipRequest> getAllMembershipRequests(StudentNumber sNumber, CourseID cID) throws StudentNotFoundException, CourseNotFoundException {
        Student student = getStudent(sNumber);
        Course course = getCourse(cID);

        List<MembershipRequest> allMembershipRequests = new ArrayList<>();

        for(MembershipRequest currentMembershipRequest : membershipRequestManager.getAllEntities()){
            try {
                if(currentMembershipRequest.getApplicant().equals(student) &&
                        currentMembershipRequest.getTeam().getCourse().equals(course)){
                    allMembershipRequests.add(currentMembershipRequest);
                }
            } catch (EntityNotFoundException e) {
                // TODO: Handle this internal database error (erroneous foreign keys)
                e.printStackTrace();
            }

        }

        return allMembershipRequests;

    }

    public static List<TeamInvitation> getAllInvitations(StudentNumber sNumber, CourseID cID) throws StudentNotFoundException, CourseNotFoundException {
        Student student = getStudent(sNumber);
        Course course = getCourse(cID);

        List<TeamInvitation> allInvitations = new ArrayList<>();

        for(TeamInvitation currentInvitation : invitationManager.getAllEntities()){
            try {
                if(currentInvitation.getInvitee().equals(student) &&
                        currentInvitation.getTeam().getCourse().equals(course)){
                    allInvitations.add(currentInvitation);
                }
            } catch (EntityNotFoundException e) {
                // TODO: Handle this internal database error (erroneous foreign keys)
                e.printStackTrace();
            }

        }
        return allInvitations;
    }



    public static List<Course> getRegisteredCourses(StudentNumber sNumber) throws StudentNotFoundException {
        Student student = getStudent(sNumber);
        List<Course> allRegisteredCourses = new ArrayList<>();

        try {
            for(CourseRegistration currentCourseRegistration : student.getCourseRegistrations()){
                allRegisteredCourses.add(currentCourseRegistration.getCourse());
            }
        } catch (EntityNotFoundException e) {
            // TODO: Handle this internal database error (erroneous foreign keys)
            e.printStackTrace();
        }

        return allRegisteredCourses;
    }

    public static List<Course> getAvailableCourses(StudentNumber sNumber) throws StudentNotFoundException {
        Student student = getStudent(sNumber);

        List<Course> allAvailableCourses = new ArrayList<>(courseManager.getAllEntities());

        System.out.println(allAvailableCourses.size());

        allAvailableCourses.removeAll(student.getCompletedCourses());

        allAvailableCourses.removeAll(getRegisteredCourses(sNumber));

       return allAvailableCourses;
    }

    public static void storeCourseRegistration(StudentNumber sNumber, CourseID cID) throws CourseNotFoundException, StudentNotFoundException {
        Student student = getStudent(sNumber);
        Course course = getCourse(cID);

        CourseRegistration newCourseRegistration = courseRegistrationManager.createEntity();
        newCourseRegistration.setCourse(course);
        newCourseRegistration.setStudent(student);
        newCourseRegistration.setCurrentlyAssignedToTeam(false);

        try {
            courseRegistrationManager.addEntity(newCourseRegistration);
        } catch (ConstraintViolationException e) {
            // NOTE: Registration is already in database!
            // TODO: Show error message in view?
            e.printStackTrace();
        }
    }

    public static void deleteCourseRegistration(StudentNumber sNumber, CourseID cID) throws StudentNotFoundException, CourseNotFoundException, CourseRegistrationNotFoundException {
        Student student = getStudent(sNumber);
        Course course = getCourse(cID);

        CourseRegistration courseRegistration = null;
        for(CourseRegistration currentCourseRegsitration : courseRegistrationManager.getAllEntities()){
            try {
                if(currentCourseRegsitration.getCourse().equals(course) &&
                        currentCourseRegsitration.getStudent().equals(student)){
                    courseRegistration = currentCourseRegsitration;
                }
            } catch (EntityNotFoundException e) {
                // TODO: Handle this internal database error (erroneous foreign keys)
                e.printStackTrace();
            }
        }

        if (courseRegistration == null)
            throw new CourseRegistrationNotFoundException(sNumber, cID);

        try {
            courseRegistrationManager.deleteEntity(courseRegistration);
        } catch (EntityNotFoundException e) {
            // TODO (NOTE): Should never been thrown, because entry is directly taken out of database
            e.printStackTrace();
        }
    }
}
