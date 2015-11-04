package controllers;

/**
 * @author Thomas Bednorz
 */

import assets.Global;

import net.unikit.database.external.interfaces.Course;
import net.unikit.database.external.interfaces.Student;
import net.unikit.database.unikit_.interfaces.CourseRegistration;

import play.mvc.Controller;
import play.mvc.Result;

import views.html.*;

public class CourseController extends Controller {

    /**
     * Creates a team associated with the courseID and studentID
     * @return showEditTeam-page displaying options for inviting other students, cancelling existing invitations,
     * accepting/denying MembershipRequests and deleting the team
     */
    public static Result createTeam(){
        /* TODO: 
                create team using courseID and studentNumber
                display showEditTeam-page

        */
        return ok(showEditTeam.render());
    }

    /**
     * Deletes the team associated with the studentNumber and courseID
     * @return showCourseDetails-page displaying details for the course
     */
    public static Result deleteTeam(){
        //TODO: Receives courseID and studentNumber, deletes team in database
        return ok(showCourseDetails.render());
    }
}
