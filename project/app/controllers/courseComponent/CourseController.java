package controllers.courseComponent;

/**
 * @author Thomas Bednorz
 */

import models.courseComponent.CourseDatabaseUtils;

import net.unikit.database.external.interfaces.Course;

import play.mvc.Controller;
import play.mvc.Result;

import views.html.*;

public class CourseController extends Controller {
    public static Result showCourseDetails(int courseID){
        Course courseToDisplay = CourseDatabaseUtils.getCourseByID(courseID);
//        List<Team> availableTeamsForCourse = CourseDatabaseUtils.getAvailableTeamsForCourse(courseID);
        return ok(showCourseDetails.render(courseToDisplay));
    }
}
