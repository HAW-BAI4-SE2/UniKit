package models.commonUtils;

/**
 * @author Thomas Bednorz
 */

import assets.Global;

import models.commonUtils.Exceptions.*;
import models.commonUtils.ID.CourseID;
import models.commonUtils.ID.StudentNumber;
import models.commonUtils.ID.TeamID;
import net.unikit.database.external.interfaces.Course;
import net.unikit.database.external.interfaces.Student;
import net.unikit.database.unikit_.interfaces.*;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class CommonDatabaseUtils {


    public static void storeInvitation(StudentNumber sNumber, TeamID tID, Student currentUser) throws TeamNotFoundException {
        // Init
        String studentNumber = sNumber.value();

        //Get team for the invitation
        Team invitationForTeam = CommonDatabaseUtils.getTeamByID(tID);

        //Create new invitation
        TeamInvitation newInvitation = Global.getTeamInvitationManager().createTeamInvitation();
        newInvitation.setInviteeStudentNumber(studentNumber);
        newInvitation.setTeam(invitationForTeam);
        newInvitation.setCreatedByStudentNumber(currentUser.getStudentNumber());

        //Store invitation in database
        Global.getTeamInvitationManager().addTeamInvitation(newInvitation);
    }

    /**
     *
     * @param sNumber
     * @param tID
     */
    public static void deleteInvitation(StudentNumber sNumber, TeamID tID) throws InvitationNotFoundException {
        // Init
        String studentNumber = sNumber.value();

        // Get invitation
        TeamInvitation teamInvitation = null;
        List<TeamInvitation> allTeamInvitations = Global.getTeamInvitationManager().getAllTeamInvitations();
        for (TeamInvitation currentTeamInvitation : allTeamInvitations) {
            if (currentTeamInvitation.getInviteeStudentNumber().equals(studentNumber) && currentTeamInvitation.getTeam().getId().equals(tID.value())) {
                teamInvitation = currentTeamInvitation;
                break;
            }
        }

        //Delete invitation from database, else inform system that invitation doesn't exist
        if(teamInvitation == null){
            throw new InvitationNotFoundException(tID);
        } else {
            Global.getTeamInvitationManager().deleteTeamInvitation(teamInvitation);
        }
    }

    /**
     *
     * @param sNummber
     * @param tID
     * @throws TeamNotFoundException
     */
    public static void addStudentToTeam(StudentNumber sNummber, TeamID tID) throws TeamNotFoundException {
        // Init
        String studentNumber = sNummber.value();

        // Get team to which the student is to be added
        Team teamByID = CommonDatabaseUtils.getTeamByID(tID);

        // Add student to team
        TeamRegistration newTeamRegistration = Global.getTeamRegistrationManager().createTeamRegistration();
        newTeamRegistration.setStudentNumber(studentNumber);
        newTeamRegistration.setTeam(teamByID);
        Global.getTeamRegistrationManager().addTeamRegistration(newTeamRegistration);
    }

    /**
     *
     * @param sNumber
     * @param tID
     */
    public static void removeStudentFromTeam(StudentNumber sNumber, TeamID tID) throws StudentNotTeamMemberException {
        int teamID = tID.value();
        String studentNumber = sNumber.value();

        TeamRegistrationManager registrationManager = Global.getTeamRegistrationManager();

        // Get registration
        TeamRegistration teamRegistration = null;
        List<TeamRegistration> allTeamRegistrations = registrationManager.getAllTeamRegistrations();
        for (TeamRegistration currentTeamRegistration : allTeamRegistrations) {
            if (currentTeamRegistration.getStudentNumber().equals(studentNumber) &&
                    currentTeamRegistration.getTeam().getId().equals(teamID)) {
                teamRegistration = currentTeamRegistration;
                break;
            }
        }

        // Inform system if
        if(teamRegistration == null){
            throw new StudentNotTeamMemberException(sNumber,tID);
        } else {
            Global.getTeamRegistrationManager().deleteTeamRegistration(teamRegistration);
        }

    }

    /**
     * Changes the registration status of a student for a specified course
     * @param sNumber the student number of the student
     * @param cID the ID of the course
     * @param status true if the student is in a team for the course, else false
     */
    public static void changeTeamRegistrationStatus(StudentNumber sNumber, CourseID cID, boolean status){
        int courseID = cID.value();
        String studentNumber = sNumber.value();

        CourseRegistrationManager courseRegistrationManager = Global.getCourseRegistrationManager();
        List<CourseRegistration> allCourseRegistrations = courseRegistrationManager.getAllCourseRegistrations();

        //Finds the registration for the student and the course ID
        CourseRegistration courseRegistrationToBUpdated = null;
        for(CourseRegistration currentCourseRegistration : allCourseRegistrations){
            if(currentCourseRegistration.getCourseId() == courseID && currentCourseRegistration.getStudentNumber().equals(studentNumber)){
                courseRegistrationToBUpdated = currentCourseRegistration;
                courseRegistrationToBUpdated.setCurrentlyAssignedToTeam(status);
                break;
            }
        }

        courseRegistrationManager.updateCourseRegistration(courseRegistrationToBUpdated);
    }

    /**
     * Returns a Course-object associated with the courseID
     * @param cID the ID of the course for which the course-object is queried
     * @return the Course-object for the given ID
     */
    public static Course getCourseByID(CourseID cID) throws CourseNotFoundException{
        int courseId = cID.value();

        Course course = Global.getCourseManager().getCourse(courseId);

        if (course == null){
            throw new CourseNotFoundException(cID);
        } else {
            return course;
        }
    }

    /**
     * Returns the Team-object associated with the teamID
     * @param tID the ID of the queried team
     * @return the Team-object for the given ID
     * @throws TeamNotFoundException
     */
    public static Team getTeamByID(TeamID tID) throws TeamNotFoundException{
        int teamID = tID.value();

        Team team = Global.getTeamManager().getTeam(teamID);

        if (team == null){
            throw new TeamNotFoundException(tID);
        } else {
            return team;
        }
    }

    /**
     * Returns the team for the student number and the course ID
     * @param sNumber the student number of the student
     * @param cID the course ID for the team
     * @return the Team-object for the student number and course ID
     */
    public static Team getTeamByStudentAndCourse(StudentNumber sNumber, CourseID cID) throws TeamNotFoundException {
        // Init
        String studentNumber = sNumber.value();
        List<TeamRegistration> allTeamRegistrations = Global.getTeamRegistrationManager().getAllTeamRegistrations();

        // Get the team
        Team queriedTeam = null;
        for(TeamRegistration currentTeamRegistration : allTeamRegistrations){
            if(currentTeamRegistration.getStudentNumber().equals(studentNumber) &&
                    currentTeamRegistration.getTeam().getCourseId() == (cID.value())){
                queriedTeam = currentTeamRegistration.getTeam();
                break;
            }
        }

        if(queriedTeam == null){
            throw new TeamNotFoundException();
        } else {
            return queriedTeam;
        }
    }

    /**
     *  Creates a team associated with the studentNumber and courseID,updates the registration status and returns the teamID
     *  @param sNumber the first member of the team
     *  @param cID the course ID for which the team will be created
     *  @return returns the ID for the new team
     */
    public static CourseID createTeam(StudentNumber sNumber, CourseID cID){
        // Init
        String studentNumber = sNumber.value();

        //Create a new team
        Team newTeam = Global.getTeamManager().createTeam();
        newTeam.setCourseId(cID.value());
        newTeam.setCreatedByStudentNumber(studentNumber);
        Global.getTeamManager().addTeam(newTeam);

        // Register current user in team
        TeamRegistration teamRegistration = Global.getTeamRegistrationManager().createTeamRegistration();
        teamRegistration.setStudentNumber(studentNumber);
        teamRegistration.setTeam(newTeam);
        Global.getTeamRegistrationManager().addTeamRegistration(teamRegistration);

        //Updates registration status for student
        CommonDatabaseUtils.changeTeamRegistrationStatus(sNumber, cID, true);

        //Gets the team that was just created
        Team createdTeam = teamRegistration.getTeam();

        return CourseID.get(createdTeam.getId());
    }

    /**
     *
     * @param tID the ID of the team that will be deleted
     * @return the ID for the associated course
     * @throws TeamNotFoundException
     */
    public static CourseID deleteTeam(TeamID tID) throws TeamNotFoundException {
        // Init
        List<Team> allTeams = Global.getTeamManager().getAllTeams();

        // Get team to be deleted
        Team teamToBeDeleted = getTeamByID(tID);

        // Get CourseID for associated course
        CourseID courseForTeamID = CourseID.get(teamToBeDeleted.getCourseId());

        //Update status of students to single registration
        for(TeamRegistration currentRegistration : teamToBeDeleted.getTeamRegistrations()){
            changeTeamRegistrationStatus(
                    StudentNumber.get(currentRegistration.getStudentNumber()),
                    CourseID.get(currentRegistration.getTeam().getCourseId()),
                    false);
        }

        //TODO: delete all membership requests
        //TODO: delete all invites
        
        //Delete team
        Global.getTeamManager().deleteTeam(teamToBeDeleted);

        return courseForTeamID;
    }

    /**
     * Deletes a membership request for the student and the team
     * @param sNumber the student number of the student who requested membership with the team
     * @param tID the ID of the team that received the membership request
     * @throws MembershipRequestNotFoundException
     * @throws TeamNotFoundException
     */
    public static void deleteMembershipRequest(StudentNumber sNumber, TeamID tID) throws MembershipRequestNotFoundException, TeamNotFoundException {
        // Init
        String studentNumber = sNumber.value();

        // Get team for membership request
        Team teamForMembershipRequest = getTeamByID(tID);

        //Get membership request to be deleted
        List<TeamApplication> allMembershipRequests = Global.getTeamApplicationManager().getAllTeamApplications();
        TeamApplication membershipRequestToBeDeleted = null;
        for(TeamApplication currentMembershipRequest : allMembershipRequests){
            if(currentMembershipRequest.getTeam().getId().equals(teamForMembershipRequest.getId()) &&
                    currentMembershipRequest.getApplicantStudentNumber().equals(studentNumber)){
                membershipRequestToBeDeleted = currentMembershipRequest;
                break;
            }
        }

        // Delete membership requests if existent, else inform system
        if(membershipRequestToBeDeleted == null){
            throw new MembershipRequestNotFoundException(sNumber,tID);
        } else {
            Global.getTeamApplicationManager().deleteTeamApplication(membershipRequestToBeDeleted);
        }
    }

    public static List<Student> getAllStudents(Team team) {

        //TODO: what if team has no registrations?

        //Get all registrations for team
        List<TeamRegistration> allRegistrationsForTeam = team.getTeamRegistrations();

        //Get all registered students
        List<Student> allStudentsInTeam = new ArrayList<>();
        for(TeamRegistration currentRegistration : allRegistrationsForTeam){
            allStudentsInTeam.add(Global.getStudentManager().getStudent(currentRegistration.getStudentNumber()));
        }

        checkNotNull(allStudentsInTeam);
        return allStudentsInTeam;
    }
}
