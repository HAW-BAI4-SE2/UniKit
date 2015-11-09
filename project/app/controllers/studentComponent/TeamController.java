package controllers.studentComponent;

/**
 * @author Thomas Bednorz
 */

import assets.Global;

import assets.SessionUtils;
import models.studentComponent.TeamDatabaseConnector;
import net.unikit.database.external.interfaces.Course;
import net.unikit.database.external.interfaces.Student;
import net.unikit.database.unikit_.interfaces.CourseRegistration;

import controllers.courseComponent.CourseController;
import net.unikit.database.unikit_.interfaces.Team;
import net.unikit.database.unikit_.interfaces.TeamApplication;
import net.unikit.database.unikit_.interfaces.TeamInvitation;
import play.mvc.Controller;
import play.mvc.Result;

import views.html.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TeamController extends Controller {

     public static Result addMember() {
        /*  
         *  TODO:
         *      persist student to team
         *      send mail to new member
         *      send mail to other team members
        */

        Team teamToDisplay = null;

        return showEditTeam(teamToDisplay.getId());
    }

    public static Result removeMember(String studentNumber, int teamID){
        /* TODO:
         *      delete student from team
         *      send mail to former member
         *      send mail to other members
         */
        Team teamToDisplay = null;

        return showEditTeam(teamToDisplay.getId());
    }

    public static Result inviteStudent(){
        /* TODO:
         *      persist invitation to database
         *      send mail to student
         *      send mail to teammembers
         */

        Team teamToDisplay = null;

        return showEditTeam(teamToDisplay.getId());
    }

    public static Result cancelInvitation(){
        /*
        *   TODO:
        *       delete invitation from database
        *       send mail to invited student
        *       send mail to team members
        */
        Team teamToDisplay = null;

        return showEditTeam(teamToDisplay.getId());
    }

    public static Result acceptMembershipRequest(){
        /*
        */
        Team teamToDisplay = null;

        return showEditTeam(teamToDisplay.getId());
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

    public static Result showTeamOverview(int courseID){
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

        Team teamToDispay = Global.getTeamManager().getTeam(teamID);

        List<Student> allStudentsForTeam = TeamDatabaseConnector.getAllStudents(teamToDispay);

        return ok(showTeamEdit.render(allStudentsForTeam,teamToDispay.getTeamApplications(),teamToDispay.getTeamInvitations()));
}


    /**
     *  Displays the teams for this course for which the student can request membership
     *  @return
     **/ 
    public static Result showAvailableTeams(int courseID){
        /*
         *  TODO:
         *      get all teams which are not full for a given course
        */

        Student currentUser = SessionUtils.getCurrentUser(session());
        Date sessionTimeout = SessionUtils.getSessionTimeout(session());

        // =================================== TESTDATA =====================================================
        List<Team> allTeams = Global.getTeamManager().getAllTeams();

        Course course = Global.getCourseManager().getCourse(courseID);
        List<Team> teamForCourse = new ArrayList<>();

        for (Team team : allTeams) {
            if (team.getCourseId() == courseID) {
                teamForCourse.add(team);
            }
        }
        // =================================== TESTDATA =====================================================

        return ok(showTeamAvailable.render(teamForCourse, course, currentUser, sessionTimeout));
    }
}