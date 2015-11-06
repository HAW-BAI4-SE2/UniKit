package controllers.courseComponent;

/**
 * @author Thomas Bednorz
 */

import models.courseComponent.CourseDatabaseConnector;

import net.unikit.database.external.interfaces.Course;

import play.mvc.Controller;
import play.mvc.Result;

import views.html.*;

import java.util.List;

public class CourseController extends Controller {
    public static Result showCourseDetails(int courseID){
        Course courseToDisplay = CourseDatabaseConnector.getCourseByID(courseID);
//        List<Team> availableTeamsForCourse = CourseDatabaseConnector.getAvailableTeamsForCourse(courseID);
        return ok(showCourseDetails.render(courseToDisplay));
    }
}
