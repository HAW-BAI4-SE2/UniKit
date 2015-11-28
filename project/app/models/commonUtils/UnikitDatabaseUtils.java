package models.commonUtils;

/**
 * @author Thomas Bednorz
 */

import assets.Global;

import models.commonUtils.Exceptions.InvitationNotFoundException;
import models.commonUtils.ID.CourseID;
import models.commonUtils.ID.StudentNumber;
import models.commonUtils.ID.TeamID;
import net.unikit.database.external.interfaces.Course;
import net.unikit.database.unikit_.interfaces.*;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class UnikitDatabaseUtils {

    /**
     *
     * @param sNumber
     * @param tID
     */
    public static void deleteInvitation(StudentNumber sNumber, TeamID tID) throws InvitationNotFoundException {
        int teamID = tID.value();
        String studentNumber = sNumber.value();

        TeamInvitationManager invitationManager = Global.getTeamInvitationManager();
        List<TeamInvitation> allTeamInvitations = invitationManager.getAllTeamInvitations();
        TeamInvitation invitationToBeDeleted = null;

            for (TeamInvitation currentInvitation : allTeamInvitations) {
                if (currentInvitation.getTeam().getId().equals(teamID) &&
                        currentInvitation.getInviteeStudentNumber().equals(studentNumber)) {
                    invitationToBeDeleted = currentInvitation;
                    break;
                }
            }

        if(invitationToBeDeleted == null){
            throw new InvitationNotFoundException();
        }
        else{
            allTeamInvitations.remove(invitationToBeDeleted);
        }

        //checkNotNull(invitationToBeDeleted);

        invitationManager.deleteTeamInvitation(invitationToBeDeleted);
    }

    /**
     *
     * @param sNummber
     * @param tID
     */
    public static void addStudentToTeam(StudentNumber sNummber, TeamID tID) {
        int teamID = tID.value();
        String studentNumber = sNummber.value();

        TeamRegistrationManager registrationManager = Global.getTeamRegistrationManager();

        TeamRegistration newTeamRegistration = registrationManager.createTeamRegistration();
        Team teamByID = UnikitDatabaseUtils.getTeamByID(tID);

        newTeamRegistration.setStudentNumber(studentNumber);
        newTeamRegistration.setTeam(teamByID);

        //TODO: check if team still exists

        registrationManager.addTeamRegistration(newTeamRegistration);
    }

    /**
     *
     * @param sNumber
     * @param tID
     */
    public static void removeStudentFromTeam(StudentNumber sNumber, TeamID tID) {
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

        //Delete registration from database
        checkNotNull(teamRegistration);
        Global.getTeamRegistrationManager().deleteTeamRegistration(teamRegistration);
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
    public static Course getCourseByID(CourseID cID) {
        int courseId = cID.value();

        Course course = Global.getCourseManager().getCourse(courseId);

        checkNotNull(course);
        return course;
    }

    /**
     * Returns the Team-object associated with the teamID
     * @param tID the ID of the queried team
     * @return the Team-object for the given ID
     */
    public static Team getTeamByID(TeamID tID) {
        int teamID = tID.value();

        Team team = Global.getTeamManager().getTeam(teamID);

        checkNotNull(team);
        return team;
    }

    /**
     * Returns the team for the student number and the course ID
     * @param sNumber the student number of the student
     * @param cID the course ID for the team
     * @return the Team-object for the student number and course ID
     */
    public static Team getTeamByStudentAndCourse(StudentNumber sNumber, CourseID cID){
        int courseID = cID.value();
        String studentNumber = sNumber.value();

        List<TeamRegistration> allTeamRegistrations = Global.getTeamRegistrationManager().getAllTeamRegistrations();

        // Get the team
        Team teamByStudentNumberAndCourseID = null;
        for(TeamRegistration currentTeamRegistration : allTeamRegistrations){
            if(currentTeamRegistration.getStudentNumber().equals(studentNumber) &&
                    currentTeamRegistration.getTeam().getCourseId() == courseID){
                teamByStudentNumberAndCourseID = currentTeamRegistration.getTeam();
                break;
            }
        }

        checkNotNull(teamByStudentNumberAndCourseID);
        return teamByStudentNumberAndCourseID;
    }

    /**
     *
     * @param tID
     * @return
     */
    public static int deleteTeam(TeamID tID) {
        int teamID = tID.value();

        TeamManager teamManager = Global.getTeamManager();

        List<Team> allTeams = teamManager.getAllTeams();

        Team teamToBeDeleted = null;
        for(Team currentTeam : allTeams){
            if(currentTeam.getId().equals(teamID)){
                teamToBeDeleted = currentTeam;
                break;
            }
        }

        //TODO: better logic if team doesnt exist

        checkNotNull(teamToBeDeleted);
        int courseForTeamID = teamToBeDeleted.getCourseId();

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
        checkNotNull(teamToBeDeleted);
        teamManager.deleteTeam(teamToBeDeleted);

        return courseForTeamID;
    }

    /**
     *
     * @param sNumber
     * @param tID
     */
    public static void deleteMembershipRequest(StudentNumber sNumber, TeamID tID) {
        int teamID = tID.value();
        String studentNumber = sNumber.value();

        TeamApplicationManager membershipRequestManager = Global.getTeamApplicationManager();

        List<TeamApplication> allMembershipRequests = membershipRequestManager.getAllTeamApplications();

        //Get membership request to be deleted
        TeamApplication membershipRequestToBeDeleted = null;
        for(TeamApplication currentMembershipRequest : allMembershipRequests){
            if(currentMembershipRequest.getTeam().getId().equals(teamID) &&
                    currentMembershipRequest.getApplicantStudentNumber().equals(studentNumber)){
                membershipRequestToBeDeleted = currentMembershipRequest;
                break;
            }
        }

        //Delete membership request from database
        checkNotNull(membershipRequestToBeDeleted);
        membershipRequestManager.deleteTeamApplication(membershipRequestToBeDeleted);
    }
}
