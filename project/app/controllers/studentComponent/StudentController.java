package controllers.studentComponent;

/**
 * @author Thomas Bednorz
 */

import assets.Global;
import assets.SessionUtils;
import controllers.courseComponent.CourseController;
import models.studentComponent.StudentDatabaseUtils;
import net.unikit.database.external.interfaces.Student;
import net.unikit.database.unikit_.interfaces.TeamInvitation;
import net.unikit.database.unikit_.interfaces.TeamInvitationManager;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class StudentController extends Controller {

    /**
     *  Creates a team associated with the studentNumber and courseID from the CreateTeamForm-object
     *  @return showEditTeam-page
     */
    public static Result createTeam(int courseID){

        Student currentUser = SessionUtils.getCurrentUser(session());
        Date sessionTimeout = SessionUtils.getSessionTimeout(session());

        //TODO: form validation
        checkNotNull(currentUser.getStudentNumber());
        if(courseID < 0) throw new NullPointerException();

        int newTeam = StudentDatabaseUtils.createTeam(currentUser.getStudentNumber(), courseID);

        //TODO send mail to student

        return redirect(controllers.studentComponent.routes.TeamController.showTeamOverview(newTeam));
    }

    /**
     *  Deletes the team associated with the teamID and updates the registration status of all involved students
     *  @param teamID the team that is to be deleted
     *  @return showCourseDetails-page for the course the team was associated with
     */
    public static Result deleteTeam(int teamID){

        int courseForTeam = StudentDatabaseUtils.deleteTeam(teamID);
        return CourseController.showCourseDetails(courseForTeam);
    }

    public static Result acceptInvitation(int teamID){
        if(teamID < 0) throw new NullPointerException();

        Student currentUser = SessionUtils.getCurrentUser(session());

        //Add the student to the team and updates registration status
        StudentDatabaseUtils.acceptInvitation(currentUser.getStudentNumber(), teamID);

        //TODO: send mail to team members

        return TeamController.addMember();
    }

    /**
     *   Declines (deletes) the invitation for the student by the team. The relevant data is retrived using a TeamStateChangeForm-object
     **/
    public static Result declineInvitation(int teamID){
        if(teamID < 0) throw new NullPointerException();

        Student currentUser = SessionUtils.getCurrentUser(session());

        StudentDatabaseUtils.deleteInvitation(currentUser.getStudentNumber(), teamID);

        //TODO: send mail to teammembers & student

        return TeamController.showEditTeam(teamID);
    }
    /**
     *  The student requests the membership with the team. The relevant data is retrived using a TeamStateChangeForm-object
     *  @return showCourseDetails-page
    **/
    public static Result requestMembership(int teamID){
        if(teamID < 0) throw new NullPointerException();

        Student currentUser = SessionUtils.getCurrentUser(session());

        if (isInvited(teamID)) {
            acceptInvitation(teamID);
        } else {
            StudentDatabaseUtils.storeMembershipRequest(currentUser.getStudentNumber(), teamID);
        }

        //TODO: send mail to team members & student

        int courseToDispay = StudentDatabaseUtils.getTeamByID(teamID).getCourseId();

        return CourseController.showCourseDetails(courseToDispay);
    }

    private static boolean isInvited(int teamID) {
        Student currentUser = SessionUtils.getCurrentUser(session());

        TeamInvitationManager invitationManager = Global.getTeamInvitationManager();
        List<TeamInvitation> allTeamInvitations = invitationManager.getAllTeamInvitations();

        for(TeamInvitation currentInvitation : allTeamInvitations){
            if(currentInvitation.getTeam().getId().equals(teamID) && currentInvitation.getInviteeStudentNumber().equals(currentUser.getStudentNumber())){
                return true;
            }
        }

        return false;
    }

    /**
     *  The student cancels his membership request with the team. The relevant data is retrived using a TeamStateChangeForm-object
     *  @return showCourseDetails-page
    **/
    public static Result cancelMembershipRequest(int teamID){
        if(teamID < 0) throw new NullPointerException();

        Student currentUser = SessionUtils.getCurrentUser(session());

        StudentDatabaseUtils.deleteMembershipRequest(currentUser.getStudentNumber(),teamID);
        
        //TODO: send mail to student

        int courseToDispay = StudentDatabaseUtils.getTeamByID(teamID).getCourseId();
        return CourseController.showCourseDetails(courseToDispay);
    }
}