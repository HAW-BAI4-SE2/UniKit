package controllers;

/**
 * @author Thomas Bednorz
 */

import play.mvc.Controller;
import play.mvc.Result;

import views.html.*;

public class CourseController extends Controller {

    /**
     * Creates a team for the course with the user as the first member
     * @return showEditTeam-page displaying options for inviting other students, cancelling existing invitations,
     * accepting/denying MembershipRequests and deleting the team
     */
    public static Result createTeam(){
        //TODO: Receives courseID and studentNumber, creates team in database
        return ok(showEditTeam.render());
    }

    /**
     *
     * @return showCourseDetails-page displaying details for the course
     */
    public static Result deleteTeam(){
        //TODO: Receives courseID and studentNumber, deletes team in database
        return ok(showCourseDetails.render());
    }
}
