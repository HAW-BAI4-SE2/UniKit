package controllers.studentComponent;

/**
 * @author Thomas Bednorz
 */

import assets.Global;
import assets.SessionUtils;
import models.studentComponent.FormModels.TeamStateChangeFormModel;
import models.studentComponent.TeamDatabaseUtils;
import net.unikit.database.external.interfaces.Course;
import net.unikit.database.external.interfaces.Student;

import controllers.courseComponent.CourseController;
import net.unikit.database.unikit_.interfaces.CourseRegistration;
import net.unikit.database.unikit_.interfaces.Team;
import net.unikit.database.unikit_.interfaces.TeamApplication;
import net.unikit.database.unikit_.interfaces.TeamInvitation;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import views.html.*;

import java.util.ArrayList;
import java.util.Date;
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

    public static Result removeMember(String studentNumber, int teamID){
//        Form<TeamStateChangeFormModel> removeStudentFromTeam =
//                Form.form(TeamStateChangeFormModel.class)
//                        .bindFromRequest();
//        TeamStateChangeFormModel teamStateChange = removeStudentFromTeam.get();

        //TODO: actual form validation
        checkNotNull(studentNumber);
        checkNotNull(teamID);

        TeamDatabaseUtils.removeStudentFromTeam(studentNumber,teamID);

        //TODO send mail to all team members

        return showEditTeam(teamID);
    }

    public static Result inviteStudent(){
        Form<TeamStateChangeFormModel> inviteStudentToTeam =
                Form.form(TeamStateChangeFormModel.class)
                        .bindFromRequest();
        TeamStateChangeFormModel teamStateChange = inviteStudentToTeam.get();

        //TODO: actual form validation
        checkNotNull(teamStateChange.studentNumber);
        checkNotNull(teamStateChange.teamID);

        //If team is not full and can invite more students, the invitation is sent
        if(!TeamDatabaseUtils.isTeamFull(teamStateChange.teamID) && TeamDatabaseUtils.hasInvitationSlots(teamStateChange.teamID)){
            TeamDatabaseUtils.storeInvitation(teamStateChange.studentNumber, teamStateChange.teamID, SessionUtils.getCurrentUser(session()));
            //TODO: send mail to all team members
        }else{
            //TODO: feedback if team is full or max invitations reached
        }

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

        TeamDatabaseUtils.deleteInvitation(teamStateChange.studentNumber, teamStateChange.teamID, SessionUtils.getCurrentUser(session()));

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

    public static Result showEditTeam(int teamID){
        return showTeamOverview(teamID);
    }

    /**
     *   Displays the details for a team
     **/
    public static Result showTeamOverview(int teamID){
        /**
        *   TODO:
        *       get all members of the current team
        *       get all pending invitations for the team
        *       get all pending membership requests for the team
        **/

        Student currentUser = SessionUtils.getCurrentUser(session());
        Date sessionTimeout = SessionUtils.getSessionTimeout(session());

        Team teamToDisplay = TeamDatabaseUtils.getTeamByID(teamID);
        Course course = Global.getCourseManager().getCourse(teamToDisplay.getCourseId());

        // Get members of team
        List<Student> allStudentsForTeam = TeamDatabaseUtils.getAllStudents(teamToDisplay);

        // Get registered students for course
        List<Student> courseRegistrees = new ArrayList<>();
        List<CourseRegistration> allCourseRegistrations = Global.getCourseRegistrationManager().getAllCourseRegistrations();
        for (CourseRegistration courseRegistration : allCourseRegistrations) {
            if (courseRegistration.getCourseId() == course.getId()) {
                // TODO: Remove students which are already in team
                courseRegistrees.add(Global.getStudentManager().getStudent(courseRegistration.getStudentNumber()));
            }
        }

        // Get applicants for team
        List<Student> applicants = new ArrayList<>();
        List<TeamApplication> teamApplications = teamToDisplay.getTeamApplications();
        for (TeamApplication teamApplication : teamApplications) {
            applicants.add(Global.getStudentManager().getStudent(teamApplication.getApplicantStudentNumber()));
        }

        // Get invitees for team
        List<Student> invitees = new ArrayList<>();
        List<TeamInvitation> teamInvitations = teamToDisplay.getTeamInvitations();
        for (TeamInvitation teamInvitation : teamInvitations) {
            invitees.add(Global.getStudentManager().getStudent(teamInvitation.getInviteeStudentNumber()));
        }

        return ok(showTeamOverview.render(teamToDisplay, allStudentsForTeam, applicants, invitees, course, courseRegistrees, currentUser, sessionTimeout));
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

        Course course = Global.getCourseManager().getCourse(courseID);

        // TODO: Fill
        List<Team> availableTeams = new ArrayList<>();

        return ok(showAvailableTeams.render(availableTeams, course, currentUser, sessionTimeout));
    }
}