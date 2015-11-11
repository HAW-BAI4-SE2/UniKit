package controllers.studentComponent;

/**
 * @author Thomas Bednorz
 */

import assets.Global;
import assets.SessionUtils;
import models.studentComponent.TeamDatabaseUtils;
import net.unikit.database.external.interfaces.Course;
import net.unikit.database.external.interfaces.Student;

import controllers.courseComponent.CourseController;
import net.unikit.database.unikit_.interfaces.CourseRegistration;
import net.unikit.database.unikit_.interfaces.Team;
import net.unikit.database.unikit_.interfaces.TeamApplication;
import net.unikit.database.unikit_.interfaces.TeamInvitation;
import play.mvc.Controller;
import play.mvc.Result;

import views.html.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class TeamController extends Controller {

     public static Result addMember(String studentNumber, int teamID) {
         checkNotNull(studentNumber);
         if(teamID < 0) throw new NullPointerException();

         TeamDatabaseUtils.addStudentToTeam(studentNumber,teamID);

         //TODO: send mail to all team members

         return redirect(controllers.studentComponent.routes.TeamController.showTeamOverview(teamID));
    }

    public static Result removeMember(String studentNumber, int teamID){
        checkNotNull(studentNumber);
        if(teamID < 0) throw new NullPointerException();

        TeamDatabaseUtils.removeStudentFromTeam(studentNumber, teamID);

        //TODO send mail to all team members

        Team alteredTeam = TeamDatabaseUtils.getTeamByID(teamID);

        if(alteredTeam.getTeamRegistrations().isEmpty()){
            TeamDatabaseUtils.deleteTeam(teamID);
            return redirect(controllers.courseComponent.routes.CourseController.showCourseDetails(alteredTeam.getCourseId()));
        }else{
            return redirect(controllers.studentComponent.routes.TeamController.showTeamOverview(teamID));
        }
    }

    public static Result inviteStudent(String studentNumber, int teamID) {
        checkNotNull(studentNumber);
        if(teamID < 0) throw new NullPointerException();

        //If team is not full and can invite more students, the invitation is sent
        if(TeamDatabaseUtils.teamCanInvite(teamID)){
            //If student already requested membership, he is automatically added to the team
            if(TeamDatabaseUtils.isMembershipRequested(studentNumber,teamID)){
                acceptMembershipRequest(studentNumber,teamID);
            }else{
                TeamDatabaseUtils.storeInvitation(studentNumber, teamID, SessionUtils.getCurrentUser(session()));
            }
            //TODO: send mail to all team members
        }else{
            //TODO: feedback if team is full or max invitations reached
        }

        return redirect(controllers.studentComponent.routes.TeamController.showTeamOverview(teamID));
    }

    public static Result cancelInvitation(String studentNumber, int teamID){
        checkNotNull(studentNumber);
        if(teamID < 0) throw new NullPointerException();

        TeamDatabaseUtils.deleteInvitation(studentNumber, teamID);

        //TODO send mail to all team members

        return redirect(controllers.studentComponent.routes.TeamController.showTeamOverview(teamID));
    }

    public static Result acceptMembershipRequest(String studentNumber, int teamID){
        checkNotNull(studentNumber);
        if(teamID < 0) throw new NullPointerException();

        TeamDatabaseUtils.addStudentToTeam(studentNumber, teamID);
        TeamDatabaseUtils.deleteMembershipRequest(studentNumber, teamID);

        //TODO send mail to all team members

        return showEditTeam(teamID);
    }

    public static Result declineMembershipRequest(String studentNumber, int teamID){
       checkNotNull(studentNumber);
        if(teamID < 0) throw new NullPointerException();

        Student currentUser = SessionUtils.getCurrentUser(session());


        TeamDatabaseUtils.deleteMembershipRequest(studentNumber,teamID);

        //TODO: send mail to student

        int courseID = TeamDatabaseUtils.getTeamByID(teamID).getCourseId();

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
                courseRegistrees.add(Global.getStudentManager().getStudent(courseRegistration.getStudentNumber()));
            }
        }

        // Remove members of team from registered students
        courseRegistrees.removeAll(allStudentsForTeam);

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
        List<Team> availableTeams = new ArrayList<>();

        List<Team> allTeams = Global.getTeamManager().getAllTeams();
        for (Team team : allTeams) {
            if (team.getCourseId() == course.getId()) {
                // TODO: Check if team is full
                availableTeams.add(team);
            }
        }

        return ok(showAvailableTeams.render(availableTeams, course, currentUser, sessionTimeout));
    }
}