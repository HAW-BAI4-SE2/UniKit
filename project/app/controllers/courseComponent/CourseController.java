package controllers.courseComponent;

/**
 * @author Thomas Bednorz
 */

import assets.Global;
import assets.SessionUtils;
import models.courseComponent.CourseDatabaseConnector;

import net.unikit.database.external.interfaces.Course;

import net.unikit.database.external.interfaces.Student;
import net.unikit.database.unikit_.interfaces.Team;
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

        Course course = CourseDatabaseConnector.getCourseByID(courseID);
//      List<Team> availableTeamsForCourse = CourseDatabaseConnector.getAvailableTeamsForCourse(courseID);


        Team currentTeam = null;
        List<Team> currentInvitations = new ArrayList<>();
        List<Team> currentMembershipRequests = new ArrayList<>();

        return ok(showCourseDetails.render(currentTeam, currentInvitations, currentMembershipRequests, course, currentUser, sessionTimeout));
    }
}
