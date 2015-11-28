package models.studentComponent;

import assets.Global;
import models.commonUtils.Exceptions.InvitationNotFoundException;
import models.commonUtils.Exceptions.MembershipRequestNotFoundException;
import models.commonUtils.Exceptions.TeamNotFoundException;
import models.commonUtils.ID.CourseID;
import models.commonUtils.ID.StudentNumber;
import models.commonUtils.ID.TeamID;
import models.commonUtils.UnikitDatabaseUtils;
import net.unikit.database.external.interfaces.Course;
import net.unikit.database.unikit_.interfaces.TeamInvitation;
import net.unikit.database.unikit_.interfaces.TeamInvitationManager;

import java.util.List;

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
        // (?) are students left to sign in the new team?
        // delete Invitations and Membershiprequests
        // add Student to new Team
        // student change team registration status
    }

    /**
     *  Deletes the team associated with the teamID and updates the registration status of all involved students
     *  @param teamID the team ID that is to be deleted
     *  @return showCourseDetails-page for the course the team was associated with
     */
    public static void deleteTeam(TeamID tID) throws TeamNotFoundException {
        // does the team still exist?
        // is the student still a member of the team?
        // change team registration status of all members
        // inform students
    }

    /**
     * Accepts a pending invitation by a team
     * @param teamID the ID of the team that issued the invite
     * @return showTeamOverview-page
     */
    public static void acceptInvitation(StudentNumber sNumber, TeamID tID) throws InvitationNotFoundException{

            TeamInvitationManager invitationManager = Global.getTeamInvitationManager();
            List<TeamInvitation> allTeamInvitations = invitationManager.getAllTeamInvitations();
            CourseID currentCourseID = null;
            //delete all Invitations for the Student in this course
            for(TeamInvitation teamInvitation : allTeamInvitations) {
                if (teamInvitation.getTeam().getId().equals(tID) &&
                        teamInvitation.getInviteeStudentNumber().equals(sNumber)) {
                    currentCourseID = teamInvitation.getTeam().getCourseId();
                }
                if (teamInvitation.getTeam().getCourseId() == currentCourseID && teamInvitation.getInviteeStudentNumber().equals(sNumber)) {
                    UnikitDatabaseUtils.deleteInvitation(sNumber, teamInvitation.getTeam().getId());
                }
            }
            //UnikitDatabaseUtils.deleteInvitation(sNumber, tID);
            UnikitDatabaseUtils.addStudentToTeam(sNumber, tID);
            //TODO send mail
            UnikitDatabaseUtils.deleteMembershipRequest(sNumber, tID);

            UnikitDatabaseUtils.changeTeamRegistrationStatus(sNumber, currentCourseID, true);
            // (?)team exists?
            // invitation exists?
            // (?)student in an other team?
            // add student to team
            // delete all Invitations
            // delete membershiprequest
            // change team registration status
            // inform students

    }

    /**
     *   Declines (deletes) the invitation for the student by the team. The relevant data is retrived using a TeamStateChangeForm-object
     **/
    public static void declineInvitation(StudentNumber sNumber, TeamID tID) throws InvitationNotFoundException {
        // invitation still exists? (team deleted invatation, team doesn´t exist anymore)
        // inform students
    }
    /**
     *  The student requests the membership with the team. The relevant data is retrived using a TeamStateChangeForm-object
     *  @param teamID the ID of the team the student requests membership with
     *  @return showCourseDetails-page
     **/
    public static void requestMembership(StudentNumber sNumber, TeamID tID) throws MembershipRequestNotFoundException {
        // team exists?
        // team already max size?
        // invitation exists ? acceptMembershipRequest : storeMembershipRequest
        // inform students
    }

    /**
     *  The student cancels his membership request with the team. The relevant data is retrived using a TeamStateChangeForm-object
     *  @return showCourseDetails-page
     **/
    public static void cancelMembershipRequest(StudentNumber sNumber, TeamID tID) throws MembershipRequestNotFoudException {
        // request already accepted?
        // request still exists?
        // inform students

    }

}
