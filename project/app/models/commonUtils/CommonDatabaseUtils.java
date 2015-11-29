package models.commonUtils;

/**
 * @author Thomas Bednorz
 */

import assets.Global;

import models.commonUtils.Exceptions.InvitationNotFoundException;
import models.commonUtils.Exceptions.MembershipRequestNotFoundException;
import models.commonUtils.Exceptions.StudentNotTeamMemberException;
import models.commonUtils.Exceptions.TeamNotFoundException;
import models.commonUtils.ID.CourseID;
import models.commonUtils.ID.StudentNumber;
import models.commonUtils.ID.TeamID;
import net.unikit.database.external.interfaces.Course;
import net.unikit.database.unikit_.interfaces.*;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class CommonDatabaseUtils {

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
            throw new InvitationNotFoundException(tID);
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
     * @throws TeamNotFoundException
     */
    public static void addStudentToTeam(StudentNumber sNummber, TeamID tID) throws TeamNotFoundException {
        int teamID = tID.value();
        String studentNumber = sNummber.value();

        TeamRegistrationManager registrationManager = Global.getTeamRegistrationManager();

        TeamRegistration newTeamRegistration = registrationManager.createTeamRegistration();
        Team teamByID = CommonDatabaseUtils.getTeamByID(tID);

        if(teamByID == null){
            throw new TeamNotFoundException(tID);
        }

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
     *
     * @param sNumber
     * @param tID
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

    public static void inviteStudent(StudentNumber sNumber, TeamID tID) {

    }
}
