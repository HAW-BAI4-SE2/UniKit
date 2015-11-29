package models.studentComponent;

import assets.Global;
import assets.SessionUtils;
import models.commonUtils.CommonDatabaseUtils;
import models.commonUtils.Exceptions.*;
import models.commonUtils.ID.CourseID;
import models.commonUtils.ID.StudentNumber;
import models.commonUtils.ID.TeamID;
import models.commonUtils.NotificationModel;
import net.unikit.database.external.interfaces.Course;
import net.unikit.database.external.interfaces.Student;
import net.unikit.database.unikit_.interfaces.Team;
import net.unikit.database.unikit_.interfaces.TeamApplication;
import net.unikit.database.unikit_.interfaces.TeamInvitation;
import net.unikit.database.unikit_.interfaces.TeamInvitationManager;


import java.awt.*;
import java.util.List;

import static play.mvc.Controller.session;

/**
 * @author Jana Wengenroth
 */
public class StudentModel {
    /**
     *  Creates a team associated with the studentNumber and courseID from the CreateTeamForm-object
     *  @param courseID the course ID for which the team is to be created
     *  @return showEditTeam-page
     */
    public static Team createTeam(StudentNumber sNumber, CourseID cID) throws TeamNotFoundException{

        TeamApplication membershiprequest = null;
        TeamInvitationManager invitationManager = Global.getTeamInvitationManager();
        List<TeamInvitation> allTeamInvitations = invitationManager.getAllTeamInvitations();
        // is the student in an other team in this course?
        try {
            CommonDatabaseUtils.getTeamByStudentAndCourse(sNumber, cID);
        }
        catch (TeamNotFoundException e) {
            // student is in no team of this course
            // create team
            CommonDatabaseUtils.createTeam(sNumber, cID);
        }
    }

    /**
     *  Deletes the team associated with the teamID and updates the registration status of all involved students
     *  @param teamID the team ID that is to be deleted
     *  @return showCourseDetails-page for the course the team was associated with
     */
    public static void deleteTeam(TeamID tID) throws TeamNotFoundException, StudentNotTeamMemberException {
        Student currentUser = SessionUtils.getCurrentUser(session());
        Team teamToDelete = CommonDatabaseUtils.getTeamByID(tID);
        // does the team still exist?
        try {
            CommonDatabaseUtils.getTeamByID(tID);
        }
        catch(TeamNotFoundException e){
            throw  new TeamNotFoundException();
        }
        // is the student still a team member?
        for(Student teamMember : CommonDatabaseUtils.getAllStudents(teamToDelete)){
            if(teamMember.equals(currentUser)){
                for(Student member : CommonDatabaseUtils.getAllStudents(teamToDelete)){
                    // inform students
                    NotificationModel.informStudentTeamDeleted(CommonDatabaseUtils.getTeamByID(tID),
                            StudentNumber.get(member.getStudentNumber()));
                }
                CommonDatabaseUtils.deleteTeam(tID);
                break;
            }
            else{
                throw new StudentNotTeamMemberException(StudentNumber.get(currentUser.getStudentNumber()), tID);
            }
        }
    }

    /**
     * Accepts a pending invitation by a team
     * @param teamID the ID of the team that issued the invite
     * @return showTeamOverview-page
     */
    public static void acceptInvitation(StudentNumber sNumber, TeamID tID) throws InvitationNotFoundException, TeamNotFoundException, MembershipRequestNotFoundException {
            Team thisTeam = CommonDatabaseUtils.getTeamByID(tID);
            TeamInvitationManager invitationManager = Global.getTeamInvitationManager();
            List<TeamInvitation> allTeamInvitations = invitationManager.getAllTeamInvitations();
            int currentCourseID = CommonDatabaseUtils.getTeamByID(tID).getCourseId();
//            // does the team still exist?
//            try {
//                CommonDatabaseUtils.getTeamByID(tID);
//            }
//            catch(TeamNotFoundException e){
//                throw  new TeamNotFoundException();
//            }
            // does the invitation still exist?
            for(TeamInvitation invitation : CommonDatabaseUtils.getTeamByID(tID).getTeamInvitations()){
                if(invitation.getInviteeStudentNumber().equals(sNumber)){
                    // add student to Team
                    CommonDatabaseUtils.addStudentToTeam(sNumber, tID);
                    // inform teammembers
                    NotificationModel.informTeamStudentJoined(thisTeam, sNumber);
                    break;
                }
                else{
                    throw new InvitationNotFoundException(tID);
                }
            }
    }

    /**
     *   Declines (deletes) the invitation for the student by the team. The relevant data is retrived using a TeamStateChangeForm-object
     **/
    public static void declineInvitation(StudentNumber sNumber, TeamID tID) throws InvitationNotFoundException, TeamNotFoundException {
        Team thisTeam = CommonDatabaseUtils.getTeamByID(tID);
        CommonDatabaseUtils.deleteInvitation(sNumber, tID);
        NotificationModel.informTeamInviteCancelled(thisTeam, sNumber);
    }
    /**
     *  The student requests the membership with the team. The relevant data is retrived using a TeamStateChangeForm-object
     *  @param teamID the ID of the team the student requests membership with
     *  @return showCourseDetails-page
     **/
    public static void requestMembership(StudentNumber sNumber, TeamID tID) throws MembershipRequestNotFoundException, TeamNotFoundException, CourseNotFoundException, TeamMaxSizeReachedException, InvitationNotFoundException {
        // does the team still exist?
        try {
            CommonDatabaseUtils.getTeamByID(tID);
        }
        catch(TeamNotFoundException e){
            throw  new TeamNotFoundException();
        }

        Team thisTeam = CommonDatabaseUtils.getTeamByID(tID);
        //Get course for the team
        Course associatedCourse = CommonDatabaseUtils.getCourseByID(CourseID.get(thisTeam.getCourseId()));
        TeamInvitationManager invitationManager = Global.getTeamInvitationManager();
        List<TeamInvitation> allTeamInvitations = invitationManager.getAllTeamInvitations();
        // team already max size?
        if( thisTeam.getTeamRegistrations().size() < associatedCourse.getMaxTeamSize()){
            // invitation exists ? acceptMembershipRequest : storeMembershipRequest
            for(TeamInvitation invitation : allTeamInvitations){
                if(invitation.getTeam().equals(thisTeam) && invitation.getInviteeStudentNumber().equals(sNumber)){
                    CommonDatabaseUtils.addStudentToTeam(sNumber, tID);
                    // inform teammembers of student joins the team
                    NotificationModel.informTeamStudentJoined(thisTeam, sNumber);
                }
                else{
                    CommonDatabaseUtils.storeMembershipRequest(sNumber, tID);
                    // inform teammembers about membershiprequest
                    NotificationModel.informTeamStudentSendMembershipRequest(thisTeam, sNumber);
                }
            }
        }
        else{
            throw  new TeamMaxSizeReachedException(tID);
        }
    }

    /**
     *  The student cancels his membership request with the team. The relevant data is retrived using a TeamStateChangeForm-object
     *  @return showCourseDetails-page
     **/
    public static void cancelMembershipRequest(StudentNumber sNumber, TeamID tID) throws MembershipRequestNotFoundException, TeamNotFoundException {
        // Get team to cancel the membership request
        Team thisTeam = CommonDatabaseUtils.getTeamByID(tID);

        // delete membership request
        CommonDatabaseUtils.deleteMembershipRequest(sNumber, tID);

        // Inform team of membership request was deleted
        NotificationModel.informTeamStudentDeletedMembershipRequest(thisTeam, sNumber);
    }

}
