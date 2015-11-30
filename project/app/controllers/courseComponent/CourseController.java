package controllers.courseComponent;

/**
 * @author Thomas Bednorz
 */

import assets.Global;
import assets.SessionUtils;
import models.commonUtils.CommonDatabaseUtils;
import models.commonUtils.Exceptions.CourseNotFoundException;
import models.commonUtils.Exceptions.StudentNotFoundException;
import models.commonUtils.Exceptions.TeamNotFoundException;
import models.commonUtils.ID.CourseID;
import models.commonUtils.ID.StudentNumber;
import models.courseComponent.CourseModel;
import net.unikit.database.interfaces.entities.*;

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
        StudentNumber sNumber = StudentNumber.get(currentUser.getStudentNumber());

        Course courseToDisplay = null;
        try {
            courseToDisplay = CourseModel.getCourseByID(cID);

        } catch (CourseNotFoundException e) {
            //TODO error message
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());
        }

        Team team = null;
        try {
            team = CourseModel.getTeam(sNumber, cID);
        } catch (TeamNotFoundException e) {
            // Student is not in Team
        }

        // Get all membership request the student has issued for the course
        List<MembershipRequest> membershipRequests = null;

        // Get all invitations the student has received for the course
        List<TeamInvitation> teamInvitations = null;
        try {
            membershipRequests = CourseModel.getMembershipRequests(sNumber, cID);
            teamInvitations = CourseModel.getInvitations(sNumber,cID);
        } catch (StudentNotFoundException e) {
            //TODO: WTF, student doesn't exist?!
        } catch (CourseNotFoundException e) {
            //TODO: WTF, course doesn't exist?!
        }

        return ok(showCourseDetails.render(team, teamInvitations, membershipRequests, courseToDisplay, currentUser, sessionTimeout));
    }
}
