package controllers.studentComponent;

/**
 * @author Thomas Bednorz
 */

import assets.Global;

import net.unikit.database.external.interfaces.Course;
import net.unikit.database.external.interfaces.Student;
import net.unikit.database.unikit_.interfaces.CourseRegistration;

import controllers.courseComponent.CourseController;
import play.mvc.Controller;
import play.mvc.Result;

import views.html.*;

public class TeamController extends Controller {

     public static Result addMember() {
        /*  
         *  TODO:
         *      persist student to team
         *      send mail to new member
         *      send mail to other team members
        */
        return showEditTeam();
    }

    public static Result removeMember(){
        /* TODO:
         *      delete student from team
         *      send mail to former member
         *      send mail to other members
         */
        return showEditTeam();
    }

    public static Result inviteStudent(){
        /* TODO:
         *      persist invitation to database
         *      send mail to student
         *      send mail to teammembers
         */
         return showEditTeam();
    }

    public static Result cancelInvitation(){
        /*
        *   TODO:
        *       delete invitation from database
        *       send mail to invited student
        *       send mail to team members
        */
        return showEditTeam();
    }

    public static Result acceptMembershipRequest(){
        /*
        */
        return showEditTeam();
    }

    public static Result declineMembershipRequest(){
        /*
         *  TODO:
         *      delete membership request from database
         *      send mail to student
         *      send mail to all team members
        */
        return CourseController.showCourseDetails();
    }

    public static Result showTeamOverview(){
        /* TODO:
         *      get all registered teams
         *      get all pending requests
         *      get all pending invites
         */
        return ok(showTeamOverview.render());
    }

    /**
     *   Displays the details for a team
     **/
    public static Result showEditTeam(String studentNumber, int courseID){
        /**
        *   TODO:
        *       get all members of the current team
        *       get all pending invitations for the team
        *       get all pending membership requests for the team
        **/
        return ok(showEditTeam.render());
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