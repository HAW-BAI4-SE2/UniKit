package models.studentComponent;

import models.commonUtils.Exceptions.MembershipRequestNotFoundException;
import models.commonUtils.ID.CourseID;
import models.commonUtils.ID.StudentNumber;
import models.commonUtils.ID.TeamID;
import models.commonUtils.UnikitDatabaseUtils;

import static java.lang.System.err;
import static com.google.common.base.Preconditions.*;

/**
 * @author Jana Wengenroth
 */
public class StudentModel {
    /**
     *  Creates a team associated with the studentNumber and courseID from the CreateTeamForm-object
     *  @param courseID the course ID for which the team is to be created
     *  @return showEditTeam-page
     */
    public static void createTeam(StudentNumber sNumber, CourseID cID){
        // is the student in an other team?
        // are students left to sign in the new team?
        // delete Invitations and Membershiprequests
        // add Student to new Team
    }

    /**
     *  Deletes the team associated with the teamID and updates the registration status of all involved students
     *  @param teamID the team ID that is to be deleted
     *  @return showCourseDetails-page for the course the team was associated with
     */
    public static void deleteTeam(TeamID tID){
        // does the team still exist?
        // is the student a member of the team?
        // inform students
    }

    /**
     * Accepts a pending invitation by a team
     * @param teamID the ID of the team that issued the invite
     * @return showTeamOverview-page
     */
    public static void acceptInvitation(StudentNumber sNumber, TeamID tID) throws InvitationNotFoundException{
        UnikitDatabaseUtils.deleteInvitation(sNumber, tID);
        UnikitDatabaseUtils.addStudentToTeam(sNumber, tID);
        UnikitDatabaseUtils.deleteMembershipRequest(sNumber, tID);
        // team exists?
        // invitation exists?
        // student in an other team?
        // add student to team
        // delete Invitation
        // delete membershiprequests
        // inform students

    }

    /**
     *   Declines (deletes) the invitation for the student by the team. The relevant data is retrived using a TeamStateChangeForm-object
     **/
    public static void declineInvitation(StudentNumber sNumber, TeamID tID){

    }
    /**
     *  The student requests the membership with the team. The relevant data is retrived using a TeamStateChangeForm-object
     *  @param teamID the ID of the team the student requests membership with
     *  @return showCourseDetails-page
     **/
    public static void requestMembership(StudentNumber sNumber, TeamID tID) throws MembershipRequestNotFoundException {
        // team exists?
        // invitation exists ? acceptMembershipRequest : storeMembershipRequest
        // inform students
    }

    /**
     *  The student cancels his membership request with the team. The relevant data is retrived using a TeamStateChangeForm-object
     *  @return showCourseDetails-page
     **/
    public static void cancelMembershipRequest(StudentNumber sNumber, TeamID tID) throws MembershipRequestNotFoudException {

    }

}
