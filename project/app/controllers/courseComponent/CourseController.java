package controllers.courseComponent;

/**
 * @author Thomas Bednorz
 */

import assets.Global;
import assets.SessionUtils;
import models.commonUtils.CommonDatabaseUtils;
import models.commonUtils.Exceptions.CourseNotFoundException;
import models.commonUtils.Exceptions.TeamNotFoundException;
import models.commonUtils.ID.CourseID;
import models.commonUtils.ID.StudentNumber;
import models.courseComponent.CourseModel;
import net.unikit.database.interfaces.entities.*;
import net.unikit.database.external.interfaces.Course;

import net.unikit.database.external.interfaces.Student;
import net.unikit.database.unikit_.interfaces.Team;
import net.unikit.database.unikit_.interfaces.TeamApplication;
import net.unikit.database.unikit_.interfaces.TeamInvitation;
import play.mvc.Controller;
import play.mvc.Result;

import views.html.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CourseController extends Controller {
    public static Result showCourseDetails(int courseID){
        Student currentUser = SessionUtils.getCurrentUser(session());
        Date sessionTimeout = SessionUtils.getSessionTimeout(session());
        CourseID cID = CourseID.get(courseID);

        Course courseToDisplay = null;
        try {
            courseToDisplay = CourseModel.getCourseByID(cID);

        } catch (CourseNotFoundException e) {
            //TODO error message
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());
        }
        //List<Team> availableTeamsForCourse = CourseDatabaseUtils.getAvailableTeamsForCourse(courseID);

        Team team = null;
        try {
            team = CommonDatabaseUtils.getTeamByStudentAndCourse(StudentNumber.get(currentUser.getStudentNumber()),
                    CourseID.get(courseToDisplay.getId()));
        } catch (TeamNotFoundException e) {
            // Student is not in Team
        }

        List<TeamApplication> teamApplications = null;
        List<TeamInvitation> teamInvitations = null;

        if (team == null) {
            teamApplications = new ArrayList<>();
            List<TeamApplication> allTeamApplications = Global.getMembershipRequestManager().getAllTeamApplications();
            for (TeamApplication teamApplication : allTeamApplications) {
                if (teamApplication == null ||
                        teamApplication.getApplicantStudentNumber() == null ||
                        teamApplication.getTeam() == null)
                    continue;
                if (teamApplication.getApplicantStudentNumber().equals(currentUser.getStudentNumber()) &&
                        teamApplication.getTeam().getCourseId() == courseToDisplay.getId()) {
                    teamApplications.add(teamApplication);
                }
            }

            teamInvitations = new ArrayList<>();
            List<TeamInvitation> allTeamInvitations = Global.getTeamInvitationManager().getAllTeamInvitations();
            for (TeamInvitation teamInvitation : allTeamInvitations) {
                if (teamInvitation == null ||
                        teamInvitation.getInviteeStudentNumber() == null ||
                        teamInvitation.getTeam() == null)
                    continue;
                if (teamInvitation.getInviteeStudentNumber().equals(currentUser.getStudentNumber()) &&
                        teamInvitation.getTeam().getCourseId() == courseToDisplay.getId()) {
                    teamInvitations.add(teamInvitation);
                }
            }
        }

        return ok(showCourseDetails.render(team, teamInvitations, teamApplications, courseToDisplay, currentUser, sessionTimeout));
    }
}
