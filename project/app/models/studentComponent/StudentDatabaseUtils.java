package models.studentComponent;

/**
 * @author Thomas Bednorz
 */

import assets.Global;

import models.commonUtils.Exceptions.InvitationNotFoundException;
import models.commonUtils.Exceptions.MembershipRequestNotFoundException;
import models.commonUtils.Exceptions.TeamNotFoundException;
import models.commonUtils.ID.CourseID;
import models.commonUtils.ID.StudentNumber;
import models.commonUtils.ID.TeamID;
import models.commonUtils.CommonDatabaseUtils;

import net.unikit.database.unikit_.interfaces.*;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;


public class StudentDatabaseUtils {

    /**
     *  Creates a team associated with the studentNumber and courseID,updates the registration status and returns the teamID
     *  @param sNumber the first member of the team
     *  @param cID the course ID for which the team will be created
     *  @return returns the ID for the new team
     */
    public static int createTeam(StudentNumber sNumber, CourseID cID){
        int courseID = cID.value();
        String studentNumber = sNumber.value();

        TeamManager teamManager = Global.getTeamManager();

        //Creates a new team and adds it to the database
        Team newTeam = teamManager.createTeam();
        newTeam.setCourseId(courseID);
        newTeam.setCreatedByStudentNumber(studentNumber);
        teamManager.addTeam(newTeam);

        // Register current user in team
        TeamRegistration teamRegistration = Global.getTeamRegistrationManager().createTeamRegistration();
        teamRegistration.setStudentNumber(studentNumber);
        teamRegistration.setTeam(newTeam);
        Global.getTeamRegistrationManager().addTeamRegistration(teamRegistration);

        //Updates registration status for student
        CommonDatabaseUtils.changeTeamRegistrationStatus(sNumber, cID, true);

        //Gets the team that was just created
        Team createdTeam = teamRegistration.getTeam();

        return createdTeam.getId();
    }

    /**
    *  Deletes the team associated with the teamID and updates the flags for the students in the course registration
    *  @param tID the team that is to be deleted
    *  @return courseForTeam the course for which the team was associated
    */
    public static CourseID deleteTeam(TeamID tID) throws TeamNotFoundException {
        return CommonDatabaseUtils.deleteTeam(tID);
    }

    /**
     * Accepts the invitation by adding the student to the team, deleting the invitation and updating the registration status
     * @param sNumber the student number of the student who accepted the invitation and will be added to the team
     * @param tID the team ID of the team that the student will be added to
     */
    public static void acceptInvitation(StudentNumber sNumber, TeamID tID) throws TeamNotFoundException, InvitationNotFoundException {
        int teamID = tID.value();
        String studentNumber = sNumber.value();

        //TODO: check if invite still exists

        //Add student to team
        CommonDatabaseUtils.addStudentToTeam(sNumber, tID);

        //Get course associated with the team
        CourseID associatedCourse = CourseID.get(CommonDatabaseUtils.getTeamByID(tID).getCourseId());

        //Update registration status to team registration
        CommonDatabaseUtils.changeTeamRegistrationStatus(sNumber, associatedCourse, true);

        //Delete invitation
        CommonDatabaseUtils.deleteInvitation(sNumber, tID);

        // If student requested membership for the team, membership request will be deleted
        try{
            CommonDatabaseUtils.deleteMembershipRequest(sNumber,tID);
        }catch(NullPointerException n){

        }
    }

    /**
     * Declines the invitation by deleting the invitation
     * @param sNumber the student number of the student who accepted the invitation and will be added to the team
     * @param tID the ID of the team that the student will be added to
     * @throws InvitationNotFoundException
     */
    public static void deleteInvitation(StudentNumber sNumber, TeamID tID) throws InvitationNotFoundException {
        CommonDatabaseUtils.deleteInvitation(sNumber,tID);
    }

    /**
     * Stores a membership request for the student and the team to the database
     * @param sNumber the student number of the student who requests membership
     * @param tID the ID of the team that gets the membership request
     * @throws TeamNotFoundException
     */
    public static void storeMembershipRequest(StudentNumber sNumber, TeamID tID) throws TeamNotFoundException{
        // Init
        String studentNumber = sNumber.value();

        // Get team for which membership is requested
        Team teamForRequest = CommonDatabaseUtils.getTeamByID(tID);

        // Store membership request
        TeamApplication newMembershipRequest = Global.getTeamApplicationManager().createTeamApplication();
        newMembershipRequest.setApplicantStudentNumber(studentNumber);
        newMembershipRequest.setTeam(teamForRequest);
        Global.getTeamApplicationManager().addTeamApplication(newMembershipRequest);
    }

    /**
     * Deletes a membership request for the student and the team
     * @param sNumber the student number of the student who requests membership
     * @param tID the ID of the team that receivers the membership request
     * @throws MembershipRequestNotFoundException
     * @throws TeamNotFoundException
     */
    public static void deleteMembershipRequest(StudentNumber sNumber, TeamID tID) throws MembershipRequestNotFoundException, TeamNotFoundException {
        // Init
        String studentNumber = sNumber.value();

        // Get team for which membership is requested
        Team teamForRequest = CommonDatabaseUtils.getTeamByID(tID);

        //Get membership request to be deleted
        List<TeamApplication> allMembershipRequests = Global.getTeamApplicationManager().getAllTeamApplications();
        TeamApplication membershipRequestToBeDeleted = null;
        for(TeamApplication currentMembershipRequest : allMembershipRequests){
            if(currentMembershipRequest.getApplicantStudentNumber().equals(studentNumber) &&
                    currentMembershipRequest.getTeam().getId().equals(teamForRequest.getId())){
                membershipRequestToBeDeleted = currentMembershipRequest;
                break;
            }
        }

        // Delete membership requests if existent, else inform system
        if(membershipRequestToBeDeleted == null){
            throw new MembershipRequestNotFoundException(sNumber, tID);
        } else {
            Global.getTeamApplicationManager().deleteTeamApplication(membershipRequestToBeDeleted);
        }
    }


    public static Team getTeamByID(TeamID id) throws TeamNotFoundException {
        return CommonDatabaseUtils.getTeamByID(id);
    }

    /**
     * Evaluates if student is invited by the team
     * @param sNumber the student number of the student
     * @param id the ID of the team
     * @return true if student is invited by the team, else false
     */
    public static boolean isStudentInvited(StudentNumber sNumber, TeamID id) {
        String studentNumber = sNumber.value();
        int teamID = id.value();

        List<TeamInvitation> allTeamInvitations = Global.getTeamInvitationManager().getAllTeamInvitations();

        for(TeamInvitation currentInvitation : allTeamInvitations){
            if(currentInvitation.getTeam().getId().equals(teamID) &&
                    currentInvitation.getInviteeStudentNumber().equals(studentNumber)){
                return true;
            }
        }

        return false;
    }
}