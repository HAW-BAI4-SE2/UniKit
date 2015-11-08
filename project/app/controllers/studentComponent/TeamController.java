package controllers.studentComponent;

/**
 * @author Thomas Bednorz
 */

import assets.Utils;
import models.studentComponent.FormModels.TeamStateChangeFormModel;
import models.studentComponent.TeamDatabaseUtils;
import net.unikit.database.external.interfaces.Student;

import controllers.courseComponent.CourseController;
import net.unikit.database.unikit_.interfaces.Team;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import views.html.*;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class TeamController extends Controller {

     public static Result addMember() {
         Form<TeamStateChangeFormModel> addStudentToTeam =
                 Form.form(TeamStateChangeFormModel.class)
                         .bindFromRequest();
         TeamStateChangeFormModel teamStateChange = addStudentToTeam.get();

         //TODO: actual form validation
         checkNotNull(teamStateChange.studentNumber);
         checkNotNull(teamStateChange.teamID);

         TeamDatabaseUtils.addStudentToTeam(teamStateChange.studentNumber,teamStateChange.teamID);

         //TODO: send mail to all team members

         return showEditTeam(teamStateChange.teamID);
    }

    public static Result removeMember(){
        Form<TeamStateChangeFormModel> removeStudentFromTeam =
                Form.form(TeamStateChangeFormModel.class)
                        .bindFromRequest();
        TeamStateChangeFormModel teamStateChange = removeStudentFromTeam.get();

        //TODO: actual form validation
        checkNotNull(teamStateChange.studentNumber);
        checkNotNull(teamStateChange.teamID);

        TeamDatabaseUtils.removeStudentFromTeam(teamStateChange.studentNumber,teamStateChange.teamID);

        //TODO send mail to all team members

        return showEditTeam(teamStateChange.teamID);
    }

    public static Result inviteStudent(){
        Form<TeamStateChangeFormModel> inviteStudentToTeam =
                Form.form(TeamStateChangeFormModel.class)
                        .bindFromRequest();
        TeamStateChangeFormModel teamStateChange = inviteStudentToTeam.get();

        //TODO: actual form validation
        checkNotNull(teamStateChange.studentNumber);
        checkNotNull(teamStateChange.teamID);

        TeamDatabaseUtils.storeInvitation(teamStateChange.studentNumber,teamStateChange.teamID, Utils.getCurrentUser(session()));

        //TODO send mail to all team members

        return showEditTeam(teamStateChange.teamID);
    }

    public static Result cancelInvitation(){
        Form<TeamStateChangeFormModel> cancelInvitation =
                Form.form(TeamStateChangeFormModel.class)
                        .bindFromRequest();
        TeamStateChangeFormModel teamStateChange = cancelInvitation.get();

        //TODO: actual form validation
        checkNotNull(teamStateChange.studentNumber);
        checkNotNull(teamStateChange.teamID);

        TeamDatabaseUtils.deleteInvitation(teamStateChange.studentNumber,teamStateChange.teamID, Utils.getCurrentUser(session()));

        //TODO send mail to all team members

        return showEditTeam(teamStateChange.teamID);
    }

    public static Result acceptMembershipRequest(){
        Form<TeamStateChangeFormModel> acceptMembershipRequest =
                Form.form(TeamStateChangeFormModel.class)
                        .bindFromRequest();
        TeamStateChangeFormModel teamStateChange = acceptMembershipRequest.get();

        //TODO: actual form validation
        checkNotNull(teamStateChange.studentNumber);
        checkNotNull(teamStateChange.teamID);

        TeamDatabaseUtils.addStudentToTeam(teamStateChange.studentNumber,teamStateChange.teamID);

        //TODO: delete membership request from database
        
        //TODO send mail to all team members

        return showEditTeam(teamStateChange.teamID);
    }

    public static Result declineMembershipRequest(){
        /*
         *  TODO:
         *      delete membership request from database
         *      send mail to student
         *      send mail to all team members
        */
        int courseID = -1;

        return CourseController.showCourseDetails(courseID);
    }

    public static Result showTeamOverview(int teamID){
        /* TODO:
         *      get all registered teams
         *      get all pending requests
         *      get all pending invites
         */

        Team teamToDisplay = null;
        return ok(showTeamOverview.render(teamToDisplay));
    }

    /**
     *   Displays the details for a team
     **/
    public static Result showEditTeam(int teamID){
        /**
        *   TODO:
        *       get all members of the current team
        *       get all pending invitations for the team
        *       get all pending membership requests for the team
        **/

        Team teamToDispay = null;

        teamToDispay = TeamDatabaseUtils.getTeamByID(teamID);

        List<Student> allStudentsForTeam = TeamDatabaseUtils.getAllStudents(teamToDispay);

        return ok(showEditTeam.render(allStudentsForTeam,teamToDispay.getTeamApplications(),teamToDispay.getTeamInvitations()));
}


    /**
     *  Displays the teams for this course for which the student can request membership
     *  @return
     **/ 
    public static Result showAvailableTeams(){
        /*
         *  TODO:
         *      get all teams which are not full for a given course
        */
        return ok(showAvailableTeams.render());
    }
}