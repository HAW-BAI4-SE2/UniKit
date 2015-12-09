package models.commonUtils;

import models.commonUtils.ID.StudentNumber;
import models.commonUtils.ID.TeamID;
import net.unikit.database.interfaces.entities.Team;


/**
 * @author Thomas Bednorz
 */
public class NotificationModel {

    public static void informTeamStudentJoined(TeamID thisTeam, StudentNumber student) {
    }

    public static void informStudentRemovedFromTeam(Team thisTeam, StudentNumber student) {

    }

    public static void informStudentTeamJoined(TeamID thisTeam, StudentNumber student) {
    }

    public static void informTeamStudentRemoved(Team thisTeam, StudentNumber student) {

    }

    public static void informStudentTeamDeleted(Team thisTeam, StudentNumber student) {

    }

    public static void informStudentInvited(Team thisTeam, StudentNumber sNumber) {

    }

    public static void informTeamStudentInvited(Team thisTeam, StudentNumber sNumber) {

    }

    public static void informStudentInviteCancelled(Team thisTeam, StudentNumber sNumber) {

    }

    public static void informTeamInviteCancelled(TeamID thisTeam, StudentNumber sNumber) {

    }

    public static void informStudentMembershipRequestDeclined(Team thisTeam, StudentNumber sNumber) {
    }

    public static void informTeamStudentSendMembershipRequest(Team thisTeam, StudentNumber sNumber){
    }

    public static void informTeamStudentDeletedMembershipRequest(TeamID thisTeam, StudentNumber sNumber){
    }

    public static void informTeamMembershipRequested(TeamID tID, StudentNumber sNumber) {

    }

    public static void informStudentMembershipRequested(TeamID tID, StudentNumber sNumber) {

    }

    public static void informTeamTeamDeletedBy(Team thisTeam, StudentNumber currentUser) {

    }
}
