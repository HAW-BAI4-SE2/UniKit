package controllers.courseComponent;

/**
 * @author Thomas Bednorz
 */

import assets.SessionUtils;
import models.commonUtils.Exceptions.CourseNotFoundException;
import models.commonUtils.Exceptions.StudentNotFoundException;
import models.commonUtils.Exceptions.TeamNotFoundException;
import models.commonUtils.ID.CourseID;
import models.commonUtils.ID.StudentNumber;
import models.courseComponent.CourseModel;
import models.studentComponent.StudentModel;
import models.studentComponent.TeamModel;
import net.unikit.database.interfaces.entities.*;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.showCourseDetails;

import java.util.Date;
import java.util.List;

public class CourseController extends Controller {
    public static Result showCourseDetails(int courseID){
        Student currentUser = SessionUtils.getCurrentUser(session());
        Date sessionTimeout = SessionUtils.getSessionTimeout(session());

        CourseID cID = CourseID.get(courseID);
        StudentNumber sNumber = StudentNumber.get(currentUser.getStudentNumber());

        Course courseToDisplay = null;
        try {
            courseToDisplay = CourseModel.getCourse(cID);

        } catch (CourseNotFoundException e) {
            //TODO error message
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());
        }

        Team team = null;
        try {
            team = TeamModel.getTeam(sNumber, cID);
        } catch (TeamNotFoundException e) {
            //TODO error message
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());

        } catch (CourseNotFoundException e) {
            //TODO error message
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());

        } catch (StudentNotFoundException e) {
            //TODO error message
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());
        }

        // Get all membership request the student has issued for the course
        List<MembershipRequest> membershipRequests = null;

        // Get all invitations the student has received for the course
        List<TeamInvitation> teamInvitations = null;
        try {
            membershipRequests = StudentModel.getMembershipRequests(sNumber, cID);
            teamInvitations = StudentModel.getInvitations(sNumber,cID);
            return ok(showCourseDetails.render(team, teamInvitations, membershipRequests, courseToDisplay, currentUser, sessionTimeout));

        } catch (StudentNotFoundException e) {
            //TODO error message
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());

        } catch (CourseNotFoundException e) {
            //TODO error message
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());
        }
    }
}
