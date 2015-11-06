package controllers.courseComponent;

/**
 * @author Thomas Bednorz
 */

import assets.Global;

import play.mvc.Controller;
import play.mvc.Result;

import views.html.*;

public class CourseController extends Controller {

    public static Result showCourseDetails(int teamID){

        /* TODO: CourseDataBaseConnector: Get course for courseID
         *
         */
        return ok(showCourseDetails.render());
    }
}
