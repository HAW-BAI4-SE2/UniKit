package models.studentComponent;

/**
 * @author Thomas Bednorz
 */

import assets.Global;

import models.commonUtils.ID.CourseID;
import models.commonUtils.ID.StudentNumber;
import models.commonUtils.ID.TeamID;
import models.commonUtils.UnikitDatabaseUtils;

import models.courseComponent.CourseDatabaseUtils;

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
        UnikitDatabaseUtils.changeTeamRegistrationStatus(sNumber, cID, true);

        //Gets the team that was just created
        Team createdTeam = teamRegistration.getTeam();

        return createdTeam.getId();
    }

    /**
    *  Deletes the team associated with the teamID and updates the flags for the students in the course registration
    *  @param tID the team that is to be deleted
    *  @return courseForTeam the course for which the team was associated
    */
    public static int deleteTeam(TeamID tID){
        return UnikitDatabaseUtils.deleteTeam(tID);
    }

    /**
     * Accepts the invitation by adding the student to the team, deleting the invitation and updating the registration status
     * @param sNumber the student number of the student who accepted the invitation and will be added to the team
     * @param tID the team ID of the team that the student will be added to
     */
    public static void acceptInvitation(StudentNumber sNumber, TeamID tID){
        int teamID = tID.value();
        String studentNumber = sNumber.value();

        //TODO: check if invite still exists

        //Add student to team
        UnikitDatabaseUtils.addStudentToTeam(sNumber, tID);

        //Get course associated with the team
        CourseID associatedCourse = CourseID.get(UnikitDatabaseUtils.getTeamByID(tID).getCourseId());

        //Update registration status to team registration
        UnikitDatabaseUtils.changeTeamRegistrationStatus(sNumber, associatedCourse, true);

        //Delete invitation
        UnikitDatabaseUtils.deleteInvitation(sNumber, tID);
        try{
            UnikitDatabaseUtils.deleteMembershipRequest(sNumber,tID);
        }catch(NullPointerException n){

        }
    }

    /**
     * Declines the invitation by deleting the invitation
     * @param sNumber the student number of the student who accepted the invitation and will be added to the team
     * @param tID the ID of the team that the student will be added to
     */
    public static void deleteInvitation(StudentNumber sNumber, TeamID tID){
        UnikitDatabaseUtils.deleteInvitation(sNumber,tID);
    }

    /**
     * Stores a membership request for the student and the team to the database
     * @param sNumber the student number of the student who requests membership
     * @param tID the ID of the team that gets the membership request
     */
    public static void storeMembershipRequest(StudentNumber sNumber, TeamID tID) {
        int teamID = tID.value();
        String studentNumber = sNumber.value();

        TeamApplicationManager membershipRequestManager = Global.getTeamApplicationManager();
        TeamApplication newMembershipRequest = membershipRequestManager.createTeamApplication();

        Team teamForRequest = UnikitDatabaseUtils.getTeamByID(tID);
        checkNotNull(teamForRequest);

        newMembershipRequest.setApplicantStudentNumber(studentNumber);
        newMembershipRequest.setTeam(teamForRequest);

        membershipRequestManager.addTeamApplication(newMembershipRequest);
    }

    /**
     * Deletes a membership request for the student and the team
     * @param sNumber the student number of the student who requests membership
     * @param id the ID of the team that receivers the membership request
     */
    public static void deleteMembershipRequest(StudentNumber sNumber, TeamID id) throws NullPointerException {
        int teamID = id.value();
        String studentNumber = sNumber.value();

        TeamApplicationManager membershipRequestManager = Global.getTeamApplicationManager();

        List<TeamApplication> allMembershipRequests = membershipRequestManager.getAllTeamApplications();

        //Get membership request
        TeamApplication membershipRequestToBeDeleted = null;
        for(TeamApplication currentMembershipRequest : allMembershipRequests){
            if(currentMembershipRequest.getApplicantStudentNumber().equals(studentNumber) &&
                    currentMembershipRequest.getTeam().getId().equals(teamID)){
                membershipRequestToBeDeleted = currentMembershipRequest;
                break;
            }
        }

        checkNotNull(membershipRequestToBeDeleted);

        membershipRequestManager.deleteTeamApplication(membershipRequestToBeDeleted);
    }


    public static Team getTeamByID(TeamID id) {

        return UnikitDatabaseUtils.getTeamByID(id);
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