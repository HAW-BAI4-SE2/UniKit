package models.studentComponent;

/**
 * @author Thomas Bednorz
 */

import assets.Global;

import models.utils.UnikitDatabaseUtils;

import models.courseComponent.CourseDatabaseUtils;

import net.unikit.database.unikit_.interfaces.*;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;


public class StudentDatabaseUtils {

    /**
     *  Creates a team associated with the studentNumber and courseID,updates the registration status and returns the teamID
     *  @param studentNumber the first member of the team
     *  @param courseID the course for which the team will be created
     *  @return returns the ID for the new team
     */
    public static int createTeam(String studentNumber, int courseID){
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

        //Gets the team that was just created
        Team createdTeam = null;
        createdTeam = UnikitDatabaseUtils.getTeamByStudentAndCourse(studentNumber,courseID);
        checkNotNull(createdTeam);

        //Updates registration status for student
        CourseDatabaseUtils.changeTeamRegistrationStatus(studentNumber, courseID, true);

        return createdTeam.getId();
    }

    /**
    *  Deletes the team associated with the teamID and updates the flags for the students in the course registration
    *  @param teamID the team that is to be deleted
    *  @return courseForTeam the course for which the team was associated
    */
    public static int deleteTeam(int teamID){
        return UnikitDatabaseUtils.deleteTeam(teamID);
    }

    /**
     * Accepts the invitation by adding the student to the team, deleting the invitation and updating the registration status
     * @param studentNumber the student number of the student who accepted the invitation and will be added to the team
     * @param teamID the ID of the team that the student will be added to
     */
    public static void acceptInvitation(String studentNumber, int teamID){
        //Add student to team
        UnikitDatabaseUtils.addStudentToTeam(studentNumber, teamID);

        //Get course associated with the team
        int associatedCourse = UnikitDatabaseUtils.getTeamByID(teamID).getCourseId();

        //Update registration status to team registration
        UnikitDatabaseUtils.changeTeamRegistrationStatus(studentNumber, associatedCourse, true);

        //Delete invitation
        UnikitDatabaseUtils.deleteInvitation(studentNumber, teamID);
        try{
            UnikitDatabaseUtils.deleteMembershipRequest(studentNumber,teamID);
        }catch(NullPointerException n){

        }
    }

    /**
     * Declines the invitation by deleting the invitation
     * @param studentNumber the student number of the student who accepted the invitation and will be added to the team
     * @param teamID the ID of the team that the student will be added to
     */
    public static void deleteInvitation(String studentNumber, int teamID){
        UnikitDatabaseUtils.deleteInvitation(studentNumber,teamID);
    }

    /**
     * Stores a membership request for the student and the team to the database
     * @param studentNumber the student number of the student who requests membership
     * @param teamID the ID of the team that gets the membership request
     */
    public static void storeMembershipRequest(String studentNumber, int teamID) {
        TeamApplicationManager membershipRequestManager = Global.getTeamApplicationManager();
        TeamApplication newMembershipRequest = membershipRequestManager.createTeamApplication();

        Team teamForRequest = UnikitDatabaseUtils.getTeamByID(teamID);
        checkNotNull(teamForRequest);

        newMembershipRequest.setApplicantStudentNumber(studentNumber);
        newMembershipRequest.setTeam(teamForRequest);

        membershipRequestManager.addTeamApplication(newMembershipRequest);
    }

    /**
     * Deletes a membership request for the student and the team
     * @param studentNumber the student number of the student who requests membership
     * @param teamID the ID of the team that gets the membership request
     */
    public static void deleteMembershipRequest(String studentNumber, int teamID) throws NullPointerException {
        checkNotNull(studentNumber);
        TeamApplicationManager membershipRequestManager = Global.getTeamApplicationManager();

        List<TeamApplication> allMembershipRequests = membershipRequestManager.getAllTeamApplications();

        //Get membership request
        TeamApplication membershipRequestToBeDeleted = null;
        for(TeamApplication currentMembershipRequest : allMembershipRequests){
            if(currentMembershipRequest.getApplicantStudentNumber().equals(studentNumber) &&
                    currentMembershipRequest.getTeam().getId() == teamID){
                membershipRequestToBeDeleted = currentMembershipRequest;
                break;
            }
        }

        checkNotNull(membershipRequestToBeDeleted);

        membershipRequestManager.deleteTeamApplication(membershipRequestToBeDeleted);
    }

    public static Team getTeamByID(int teamID) {
       return UnikitDatabaseUtils.getTeamByID(teamID);
    }
}