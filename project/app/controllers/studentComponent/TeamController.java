package controllers.studentComponent;

/**
 * @author Thomas Bednorz
 */

import assets.Global;
import assets.SessionUtils;
import models.commonUtils.CommonDatabaseUtils;
import models.commonUtils.Exceptions.*;
import models.commonUtils.ID.CourseID;
import models.commonUtils.ID.StudentNumber;
import models.commonUtils.ID.TeamID;
import models.studentComponent.TeamModel;
import net.unikit.database.external.interfaces.Course;
import net.unikit.database.external.interfaces.Student;

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


public class TeamController extends Controller {

    private static Student currentUser;

    static{
        currentUser = SessionUtils.getCurrentUser(session());
    }

    /**
     * Removes a student from the team and returns a resultpage. If student was the last member, the associated team gets deleted.
     * @param studentNumber the student number of the student who is to be removed from the team
     * @param teamID the ID of the team from which the student is to be removed
     * @return team overview page if student was removed successful or if student is not a member of the team,
     * course detail page if the team wasn't found or if team was deleted, course overview page if team couldn't be found
     */
    public static Result removeMember(String studentNumber, int teamID){
        // Init
        StudentNumber sNumber = StudentNumber.get(studentNumber);
        TeamID tID = TeamID.get(teamID);

        // Remove student from team or delete team if last member
        Team modifiedTeam = null;
        try {
            modifiedTeam = TeamModel.removeMember(sNumber, tID);
        } catch (TeamNotFoundException e) {
            // TODO error message
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());
        } catch (StudentNotTeamMemberException e) {
            // TODO error message
            return redirect(controllers.studentComponent.routes.TeamController.showTeamOverview(teamID));
        }

        // If team is empty, display course details, else team overview
        if(modifiedTeam.getTeamRegistrations().isEmpty()){
            // TODO error message
            return redirect(controllers.courseComponent.routes.CourseController.showCourseDetails(modifiedTeam.getCourseId()));
        }else{
            // TODO error message
            return redirect(controllers.studentComponent.routes.TeamController.showTeamOverview(teamID));
        }
    }

    /**
     * Invites a student to a team and returns a resultpage. If the student has a pending membership request, the student
     * is immediatly added to the team.
     * @param studentNumber the student number of the student that is to be invited
     * @param teamID the ID of the team for which the student is to be invited
     * @return
     */
    public static Result inviteStudent(String studentNumber, int teamID) {
        // Init
        StudentNumber sNumber = StudentNumber.get(studentNumber);
        TeamID tID = TeamID.get(teamID);

        try {
            TeamModel.inviteStudent(sNumber, tID, currentUser);
            return redirect(controllers.studentComponent.routes.TeamController.showTeamOverview(teamID));
        } catch (TeamMaxSizeReachedException e) {
            // TODO error message
            return redirect(controllers.studentComponent.routes.TeamController.showTeamOverview(teamID));
        } catch (TeamNotFoundException e) {
            // TODO error message
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());
        } catch (FatalErrorException e) {
            // TODO error message
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());
        } catch (CourseNotFoundException e) {
            // TODO error message
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());
        }
    }

    public static Result cancelInvitation(String studentNumber, int teamID){
        // Init
        StudentNumber sNumber = StudentNumber.get(studentNumber);
        TeamID tID = TeamID.get(teamID);

        try {
            TeamModel.cancelInvitation(sNumber, tID);
            return redirect(controllers.studentComponent.routes.TeamController.showTeamOverview(teamID));
        } catch (InvitationNotFoundException e) {
            // TODO error message
            return redirect(controllers.studentComponent.routes.TeamController.showTeamOverview(teamID));
        } catch (TeamNotFoundException e) {
            // TODO error message
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());
        }
    }

    public static Result acceptMembershipRequest(String studentNumber, int teamID){
        // Init
        StudentNumber sNumber = StudentNumber.get(studentNumber);
        TeamID tID = TeamID.get(teamID);

        try {
            TeamModel.acceptMembershipRequest(sNumber,tID);
            return redirect(controllers.studentComponent.routes.TeamController.showTeamOverview(teamID));
        } catch (MembershipRequestNotFoundException e) {
            // TODO error message
            return redirect(controllers.studentComponent.routes.TeamController.showTeamOverview(teamID));
        } catch (TeamNotFoundException e) {
            // TODO error message
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());
        }
    }

    public static Result declineMembershipRequest(String studentNumber, int teamID){
        // Init
        StudentNumber sNumber = StudentNumber.get(studentNumber);
        TeamID tID = TeamID.get(teamID);

        try {
            TeamModel.declineMembershipRequest(sNumber,tID);
            return redirect(controllers.studentComponent.routes.TeamController.showTeamOverview(teamID));
        } catch (MembershipRequestNotFoundException e) {
            // TODO error message
            return redirect(controllers.studentComponent.routes.TeamController.showTeamOverview(teamID));
        } catch (TeamNotFoundException e) {
            // TODO error message
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());
        }
    }

    /**
     *   Displays the details for a team
     **/
    public static Result showTeamOverview(int teamID){
       // Init
        TeamID tID = TeamID.get(teamID);
        Date sessionTimeout = SessionUtils.getSessionTimeout(session());

        // Get team and course associated with team ID
        Team teamToDisplay = null;
        Course course = null;
        try {
            teamToDisplay = CommonDatabaseUtils.getTeamByID(tID);
            course = CommonDatabaseUtils.getCourseByID(CourseID.get(teamToDisplay.getCourseId()));
        } catch (TeamNotFoundException e) {
            // TODO error message
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());
        } catch (CourseNotFoundException e) {
            // TODO error message
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());
        }


        // Get members of team
        List<Student> allStudentsForTeam = CommonDatabaseUtils.getAllStudents(teamToDisplay);

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