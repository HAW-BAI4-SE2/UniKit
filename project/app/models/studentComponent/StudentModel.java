package models.studentComponent;

import assets.Global;
import assets.SessionUtils;
import models.commonUtils.CommonDatabaseUtils;
import models.commonUtils.Exceptions.*;
import models.commonUtils.ID.CourseID;
import models.commonUtils.ID.StudentNumber;
import models.commonUtils.ID.TeamID;
import models.commonUtils.NotificationModel;
import net.unikit.database.interfaces.entities.Course;
import net.unikit.database.interfaces.entities.Student;
import net.unikit.database.interfaces.entities.Team;
import net.unikit.database.interfaces.entities.TeamInvitation;
import net.unikit.database.interfaces.managers.TeamInvitationManager;


import java.util.List;

import static models.commonUtils.NotificationModel.*;
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
    public static TeamID createTeam(StudentNumber sNumber, CourseID courseID) throws CourseNotFoundException, StudentNotFoundException, StudentAlreadyInTeamException, FatalErrorException {

        // is the student in an other team in this course?
        try {
            CommonDatabaseUtils.getTeamByStudentAndCourse(sNumber,courseID);
            throw new StudentAlreadyInTeamException(sNumber);
        } catch (TeamNotFoundException e) {
            return CommonDatabaseUtils.createTeam(sNumber, courseID);
        }
    }

    /**
     *  Deletes the team associated with the teamID and updates the registration status of all involved students
     *  @param tID the team ID that is to be deleted
     *  @return showCourseDetails-page for the course the team was associated with
     */
    public static void deleteTeam(TeamID tID) throws TeamNotFoundException, NotTeamMemberExcpetion {
        StudentNumber currentUser = StudentNumber.get(SessionUtils.getCurrentUser(session()).getStudentNumber());
        Team deletedTeam = CommonDatabaseUtils.getTeamByID(tID);

        try {
            if(CommonDatabaseUtils.isStudentMember(currentUser, tID)){
                CommonDatabaseUtils.deleteTeam(tID);
                NotificationModel.informStudentTeamDeleted(deletedTeam, currentUser);
            } else {
                throw new NotTeamMemberExcpetion(currentUser,tID);
            }
        } catch (StudentNotFoundException e) {
            // Do nothing if student doesnt exist
        }
    }

    /**
     * Accepts a pending invitation by a team
     * @param tID the ID of the team that issued the invite
     * @return showTeamOverview-page
     */
    public static void acceptInvitation(StudentNumber sNumber, TeamID tID) throws InvitationNotFoundException, TeamNotFoundException, StudentNotFoundException {
        CommonDatabaseUtils.addStudentToTeam(sNumber, tID);
        CommonDatabaseUtils.deleteInvitation(sNumber,tID);

        try {
            CommonDatabaseUtils.deleteMembershipRequest(sNumber, tID);
        } catch (MembershipRequestNotFoundException e) {
            // It's possible the student had no membership request for the team, do nothing
        }

        informStudentTeamJoined(tID, sNumber);
    }

    /**
     *   Declines (deletes) the invitation for the student by the team. The relevant data is retrived using a TeamStateChangeForm-object
     **/
    public static void declineInvitation(StudentNumber sNumber, TeamID tID) throws InvitationNotFoundException, TeamNotFoundException, StudentNotFoundException {
        CommonDatabaseUtils.deleteInvitation(sNumber, tID);
        NotificationModel.informTeamInviteCancelled(tID, sNumber);
    }

    /**
     *  The student requests the membership with the team. The relevant data is retrived using a TeamStateChangeForm-object
     *  @param tID the ID of the team the student requests membership with
     *  @return showCourseDetails-page
     **/
    public static void requestMembership(StudentNumber sNumber, TeamID tID) throws TeamNotFoundException, CourseNotFoundException, TeamMaxSizeReachedException, StudentNotFoundException {
        if(CommonDatabaseUtils.isStudentInvited(sNumber,tID)){
            CommonDatabaseUtils.addStudentToTeam(sNumber, tID);

            try {
                CommonDatabaseUtils.deleteInvitation(sNumber, tID);
            } catch (InvitationNotFoundException e) {
                // Do nothing, if the invitation gets deleted after the membership gets requested, all is well
            }

            informStudentTeamJoined(tID, sNumber);
            informTeamStudentJoined(tID, sNumber);
        } else {
            if(!CommonDatabaseUtils.isMaxSizeReached(tID)){
                CommonDatabaseUtils.storeMembershipRequest(sNumber, tID);
                informStudentMembershipRequested(tID, sNumber);
                informTeamMembershipRequested(tID, sNumber);
            } else {
                throw new TeamMaxSizeReachedException(tID);
            }
        }
    }

    /**
     *  The student cancels his membership request with the team. The relevant data is retrived using a TeamStateChangeForm-object
     *  @return showCourseDetails-page
     **/
    public static void cancelMembershipRequest(StudentNumber sNumber, TeamID tID) throws MembershipRequestNotFoundException, TeamNotFoundException, StudentNotFoundException {
        // delete membership request
        CommonDatabaseUtils.deleteMembershipRequest(sNumber, tID);

        // Inform team of membership request was deleted
        NotificationModel.informTeamStudentDeletedMembershipRequest(tID, sNumber);
    }
}
