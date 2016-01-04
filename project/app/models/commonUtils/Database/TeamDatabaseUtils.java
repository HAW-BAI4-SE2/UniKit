package models.commonUtils.Database;

import assets.Global;
import models.commonUtils.Exceptions.*;
import models.commonUtils.ID.CourseID;
import models.commonUtils.ID.StudentNumber;
import models.commonUtils.ID.TeamID;
import net.unikit.database.exceptions.ConstraintViolationException;
import net.unikit.database.exceptions.EntityNotFoundException;
import net.unikit.database.interfaces.entities.*;
import net.unikit.database.interfaces.managers.TeamManager;
import net.unikit.database.interfaces.managers.TeamRegistrationManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the database interaction related to teams.
 * @author Thomas Bednorz
 */
class TeamDatabaseUtils {
    private static TeamManager teamManager;
    private static TeamRegistrationManager teamRegistrationManager;
    static{
        teamManager = Global.getTeamManager();
        teamRegistrationManager = Global.getTeamRegistrationManager();
    }

    /**
     *
     * @param student the student that is to be added to the team
     * @param team the team to which the student is to be added
     * @throws CourseNotFoundException if the course associated with the team couldn't be found
     * @throws StudentInTeamException if the student is already a member of the team
     * @throws FatalErrorException if the student or the course couldn't be found during registration change
     */
    public static void addStudentToTeam(Student student, Team team) throws StudentNotFoundException, TeamNotFoundException, CourseNotFoundException, StudentInTeamException, FatalErrorException {
        // Get course of the Team
        Course courseForTeam = null;
        try {
            courseForTeam = team.getCourse();
        } catch (EntityNotFoundException e) {
            throw new CourseNotFoundException();
        }

        // Add student to team
        TeamRegistration newTeamRegistration = teamRegistrationManager.createEntity();
        newTeamRegistration.setStudent(student);
        newTeamRegistration.setTeam(team);

        try {
            teamRegistrationManager.addEntity(newTeamRegistration);
        } catch (ConstraintViolationException e) {
            throw new StudentInTeamException(StudentNumber.get(student.getStudentNumber()));
        }

        // Delete all pending invitations
        InvitationDatabaseUtils.deleteAllInvitations(student, courseForTeam);

        // Delete all pending membership requests
        MembershipRequestDatabaseUtils.deleteAllMembershipRequests(student, courseForTeam);

        //change registration Status of the student
        StudentDatabaseUtils.changeTeamRegistrationStatus(student,courseForTeam , true);
    }

    /**
     *
     * @param student
     * @param team
     */
    public static void removeStudent(Student student, Team team) {
        try {
            for (TeamRegistration currentRegistration : teamRegistrationManager.getAllEntities()) {
                if (currentRegistration.getTeam().equals(team) && currentRegistration.getStudent().equals(student)) {
                    teamRegistrationManager.deleteEntity(currentRegistration);
                    break;
                }
            }


        } catch (EntityNotFoundException e) {
            // Student couldn't be found for any registration
            // Entity couldn't be deleted
            e.printStackTrace();
        }
    }

    /**
     *  Creates a team associated with the studentNumber and courseID,updates the registration status and returns the teamID
     *  @param student the first member of the team
     *  @param course the course ID for which the team will be created
     *  @return returns the ID for the new team
     */
    public static TeamID createTeam(Student student, Course course) throws TeamExistsException, StudentInTeamException, FatalErrorException {
        // Create new team
        Team newTeam = teamManager.createEntity();
        newTeam.setCourse(course);
        newTeam.setCreatedBy(student);
        
        try {
            teamManager.addEntity(newTeam);
        } catch (ConstraintViolationException e) {
            throw new TeamExistsException(StudentNumber.get(student.getStudentNumber()),CourseID.get(course.getId()));
        }

        // Register student with team
        TeamRegistration newTeamRegistration = teamRegistrationManager.createEntity();
        newTeamRegistration.setTeam(newTeam);
        newTeamRegistration.setStudent(student);
        try {
            teamRegistrationManager.addEntity(newTeamRegistration);
        } catch (ConstraintViolationException e) {
            throw new StudentInTeamException(StudentNumber.get(student.getStudentNumber()));
        }

        // Update registration status of student
        StudentDatabaseUtils.changeTeamRegistrationStatus(student,course,true);

        // Get the new team
        Team createdTeam = null;
        try {
            createdTeam = TeamDatabaseUtils.getTeam(student,course);
        } catch (TeamNotFoundException e) {
            throw new FatalErrorException("Error while creating the team");
        }
        return TeamID.get(createdTeam.getId());
    }

    /**
     *
     * @param team the ID of the team that will be deleted
     * @return the ID for the associated course
     * @throws TeamNotFoundException
     */
    public static CourseID deleteTeam(Team team) throws CourseNotFoundException, TeamDeletedException, FatalErrorException {
        CourseID courseIDForTeam = null;
        try {
            courseIDForTeam = CourseID.get(team.getCourse().getId());
        } catch (EntityNotFoundException e) {
            throw new CourseNotFoundException();
        }


        // Update students to single registration
        try {
            for (TeamRegistration currentRegistration : team.getTeamRegistrations()) {
                StudentDatabaseUtils.changeTeamRegistrationStatus(currentRegistration.getStudent(),
                        currentRegistration.getTeam().getCourse(),
                        false);
            }
        } catch (EntityNotFoundException e) {
            // Any student couldn't be found
            // Any course couldn't be found
            e.printStackTrace();
        }

        MembershipRequestDatabaseUtils.deleteAllMembershipRequests(team);
        InvitationDatabaseUtils.deleteAllInvitations(team);
        //TODO: delete all invites

        //Delete team
        try {
            teamManager.deleteEntity(team);
        } catch (EntityNotFoundException e) {
            throw new TeamDeletedException(TeamID.get(team.getId()));
        }


        return courseIDForTeam;
    }


        /**
     *
     * @param tID
     * @return
     * @throws TeamNotFoundException
     */
    public static Team getTeam(TeamID tID) throws TeamNotFoundException {
        Team team = null;
        try {
            team = teamManager.getEntity(teamManager.createID(tID.value()));
        } catch (EntityNotFoundException e) {
            throw new TeamNotFoundException(tID);
        }

        return team;
    }

    /**
     * Returns the team for the student number and the course ID
     * @param student the student number of the student
     * @param course the course ID for the team
     * @return the Team-object for the student number and course ID
     */
    public static Team getTeam(Student student, Course course) throws TeamNotFoundException {
        try{
            for(TeamRegistration currentTeamRegistration : teamRegistrationManager.getAllEntities()) {
                if (currentTeamRegistration.getTeam().getCourse().getId().equals(course.getId()) &&
                        currentTeamRegistration.getStudent().getStudentNumber().equals(student.getStudentNumber())) {
                    return currentTeamRegistration.getTeam();
                }
            }
        } catch (Exception e){
            // TODO: Error handling?
            e.printStackTrace();
        }

        throw new TeamNotFoundException();
    }

    /**
     *
     * @param course
     * @return
     */
    public static List<Team> getAllTeams(Course course){
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

    /**
     *
     * @param course
     * @return
     */
    public static List<Team> getAvailableTeams(Course course){
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

    public static boolean isStudentMember(Team team, Student student) throws TeamNotFoundException {
        try {
            for(TeamRegistration currentRegistration : teamManager.getEntity(team.getId()).getTeamRegistrations()){
                if(currentRegistration.getStudent().equals(student)){
                    return true;
                }
            }
        } catch (EntityNotFoundException e) {
            throw new TeamNotFoundException(TeamID.get(team.getId()));
        }

        return false;
    }
}
