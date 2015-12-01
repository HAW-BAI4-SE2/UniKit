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
     * @param invitedStudentNumber
     * @param invitingTeamID
     * @param createdByStudentNumber
     * @throws TeamNotFoundException
     * @throws StudentNotFoundException
     * @throws InvitationExistsException
     */
    public static void storeInvitation(StudentNumber invitedStudentNumber, TeamID invitingTeamID, StudentNumber createdByStudentNumber) throws TeamNotFoundException, StudentNotFoundException, InvitationExistsException {
        // Get object for invited student
        Student invitedStudent = getStudent(invitedStudentNumber);

        // Get object for inviting student
        Student createdByStudent = getStudent(createdByStudentNumber);

        // Get object for inviting team
        Team invitingTeam = getTeam(invitingTeamID);

        // If invitation already exists, inform the system

        TeamInvitation newInvitation = invitationManager.createEntity();
        try {
            invitationManager.getInvitation(invitedStudent,invitingTeam);
            throw new InvitationExistsException(invitedStudentNumber,invitingTeamID);

        } catch (EntityNotFoundException e) {
            newInvitation.setCreatedBy(createdByStudent);
            newInvitation.setInvitee(invitedStudent);
            newInvitation.setTeam(invitingTeam);
        }

        try {
            invitationManager.addEntity(newInvitation);

        } catch (ConstraintViolationException e) {
            throw new InvitationExistsException(invitedStudentNumber,invitingTeamID);
        }
    }

    /**
     *
     * @param sNumber
     * @param tID
     */
    public static void deleteInvitation(StudentNumber sNumber, TeamID tID) throws InvitationNotFoundException, StudentNotFoundException, TeamNotFoundException {
        // Get student object
        Student invitedStudent = getStudent(sNumber);

        // Get team object
        Team invitingTeam = getTeam(tID);

        // Get the invitation to be deleted
        TeamInvitation invitationToBeDeleted = null;

        try {
            invitationToBeDeleted = invitationManager.getInvitation(invitedStudent,invitingTeam);
        } catch (EntityNotFoundException e) {
            throw new InvitationNotFoundException(tID);
        }

        try {
            invitationManager.deleteEntity(invitationToBeDeleted);
        } catch (EntityNotFoundException e) {
            throw new InvitationNotFoundException(tID);
        }

    }

    /**
     *
     * @param sNummber
     * @param tID
     * @throws TeamNotFoundException
     */
    public static void addStudentToTeam(StudentNumber sNummber, TeamID tID) throws TeamNotFoundException, StudentNotFoundException, CourseNotFoundException, StudentInTeamException {
        // Init
        String studentNumber = sNummber.value();

        // Get the student to be added to the team
        Student studentToBeAdded = getStudent(sNummber);

        // Get team to which the student is to be added
        Team teamByID = getTeam(tID);

        // Get course of the Team
        Course courseForTeam = null;
        try {
            courseForTeam = teamByID.getCourse();
        } catch (EntityNotFoundException e) {
            throw new CourseNotFoundException();
        }

        // Add student to team
        TeamRegistration newTeamRegistration = registrationManager.createEntity();
        newTeamRegistration.setStudent(studentToBeAdded);
        newTeamRegistration.setTeam(teamByID);

        try {
            registrationManager.addEntity(newTeamRegistration);
        } catch (ConstraintViolationException e) {
            throw new StudentInTeamException(sNummber);
        }

        // Delete all pending invitations
        deleteAllInvitations(sNummber,CourseID.get(courseForTeam.getId()));

        // Delete all pending membership requests
        deleteAllMembershipRequests(sNummber, CourseID.get(courseForTeam.getId()));

        //change registration Status of the student
        changeTeamRegistrationStatus(sNummber,CourseID.get(courseForTeam.getId()) , true);
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
     * Changes the registration status of a student for a specified course
     * @param sNumber the student number of the student
     * @param cID the ID of the course
     * @param status true if the student is in a team for the course, else false
     */
    public static void changeTeamRegistrationStatus(StudentNumber sNumber, CourseID cID, boolean status){
        Course.ID courseID = courseManager.createID(cID.value());
        Student.StudentNumber studentNumber = studentManager.createID(sNumber.value());

        try{
            for(CourseRegistration courseRegistration : courseRegistrationManager.getAllEntities()){
                if(courseRegistration.getCourse().getId().equals(courseID) &&
                        courseRegistration.getStudent().getStudentNumber().equals(studentNumber)){
                    courseRegistration.setCurrentlyAssignedToTeam(status);
                    break;
                }
            }
        } catch (Exception e){
            // TODO: Error handling
            e.printStackTrace();
        }
    }

    /**
     * Returns the Team-object associated with the teamID
     * @param tID the ID of the queried team
     * @return the Team-object for the given ID
     * @throws TeamNotFoundException
     */
    public static Team getTeamByID(TeamID tID) throws TeamNotFoundException{
       return getTeam(tID);
    }

    /**
     * Returns the team for the student number and the course ID
     * @param sNumber the student number of the student
     * @param cID the course ID for the team
     * @return the Team-object for the student number and course ID
     */
    public static Team getTeamByStudentAndCourse(StudentNumber sNumber, CourseID cID) throws TeamNotFoundException {
        Course.ID courseID = courseManager.createID(cID.value());
        Student.StudentNumber studentNumber = studentManager.createID(sNumber.value());

        // Get the team
        Team queriedTeam = null;
        try{
            for(TeamRegistration currentTeamRegistration : registrationManager.getAllEntities()) {
                if (currentTeamRegistration.getTeam().getCourse().getId().equals(courseID) &&
                        currentTeamRegistration.getStudent().getStudentNumber().equals(studentNumber)) {
                    return currentTeamRegistration.getTeam();
                }
            }
        } catch (Exception e){
            // TODO: Error handling
            e.printStackTrace();
        }

        throw new TeamNotFoundException();
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

    /**
     * Deletes a membership request for the student and the team
     * @param sNumber the student number of the student who requested membership with the team
     * @param tID the ID of the team that received the membership request
     * @throws MembershipRequestNotFoundException
     * @throws TeamNotFoundException
     */
    public static void deleteMembershipRequest(StudentNumber sNumber, TeamID tID) throws MembershipRequestNotFoundException, TeamNotFoundException, StudentNotFoundException {
        // Get requesting student
        Student requestingStudent = getStudent(sNumber);

        // Get team for membership request
        Team teamForMembershipRequest = getTeamByID(tID);

        // Get membershiprequest to be deleted, throw error if not found
        MembershipRequest membershipRequestToBeDeleted = null;
        try {
            membershipRequestToBeDeleted = membershipRequestManager.getMembershipRequest(requestingStudent,teamForMembershipRequest);
            membershipRequestManager.deleteEntity(membershipRequestToBeDeleted);
        } catch (EntityNotFoundException e) {
            throw new MembershipRequestNotFoundException(sNumber, tID);
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

    /**
     *
     * @param studentNumber
     * @return
     * @throws StudentNotFoundException
     */
    private static Student getStudent(StudentNumber studentNumber) throws StudentNotFoundException{
        Student student = null;
        try {
            student = studentManager.getEntity(studentManager.createID(studentNumber.value()));
        } catch (EntityNotFoundException e) {
            throw new StudentNotFoundException(studentNumber);
        }

        return student;
    }

    /**
     *
     * @param teamID
     * @return
     * @throws TeamNotFoundException
     */
    private static Team getTeam(TeamID teamID) throws TeamNotFoundException {
        Team team = null;
        try {
            team = teamManager.getEntity(teamManager.createID(teamID.value()));
        } catch (EntityNotFoundException e) {
            throw new TeamNotFoundException(teamID);
        }

        return team;
    }

    /**
     *
     * @param courseID
     * @return
     * @throws CourseNotFoundException
     */
    private static Course getCourse(CourseID courseID) throws CourseNotFoundException{
        Course course = null;
        try {
            course = courseManager.getEntity(courseManager.createID(courseID.value()));
        } catch (EntityNotFoundException e) {
            throw new CourseNotFoundException(courseID);
        }

        return course;
    }

    /**
     * Returns a Course-object associated with the courseID
     * @param cID the ID of the course for which the course-object is queried
     * @return the Course-object for the given ID
     */
    public static Course getCourseByID(CourseID cID) throws CourseNotFoundException{
        return getCourse(cID);
    }

    /**
     *
     * @param sNumber
     * @param cID
     */
    private static void deleteAllInvitations(StudentNumber sNumber, CourseID cID){
        Course.ID courseID = courseManager.createID(cID.value());
        Student.StudentNumber studentNumber = studentManager.createID(sNumber.value());

        try{
            for(TeamInvitation teamInvitation : invitationManager.getAllEntities()){
                if(teamInvitation.getTeam().getCourse().getId().equals(courseID) &&
                        teamInvitation.getInvitee().getStudentNumber().equals(studentNumber)){
                    try {
                        deleteInvitation(sNumber, TeamID.get(teamInvitation.getTeam().getId()));
                    } catch (InvitationNotFoundException e) {
                        // Depending of the context of the call, it's expected that no invitations exist
                    }
                }
            }
        } catch (Exception e){
            //TODO Error handling
            e.printStackTrace();
        }
    }

    /**
     *
     * @param sNumber
     * @param cID
     */
    private static void deleteAllMembershipRequests(StudentNumber sNumber, CourseID cID) {
        Course.ID courseID = courseManager.createID(cID.value());
        Student.StudentNumber studentNumber = studentManager.createID(sNumber.value());

        for(MembershipRequest membershipRequest : membershipRequestManager.getAllEntities()){
            try {
                if(membershipRequest.getTeam().getCourse().getId().equals(courseID) &&
                        membershipRequest.getApplicant().getStudentNumber().equals(studentNumber)){
                    try {
                        deleteMembershipRequest(sNumber, TeamID.get(membershipRequest.getTeam().getId()));
                    } catch (Exception e) {
                        // Depending of the context of the call, it's expected that no membership requests exist
                    }
                }
            } catch (EntityNotFoundException e) {
                // TODO: Handle this internal database error (erroneous foreign keys)
                e.printStackTrace();
            }
        }
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
