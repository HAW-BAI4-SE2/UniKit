package controllers.courseComponent;

/**
 * @author Thomas Bednorz
 */

import assets.SessionUtils;
import models.commonUtils.Exceptions.CourseNotFoundException;
import models.commonUtils.Exceptions.CourseRegistrationNotFoundException;
import models.commonUtils.Exceptions.StudentNotFoundException;
import models.commonUtils.Exceptions.TeamNotFoundException;
import models.commonUtils.ID.CourseID;
import models.commonUtils.ID.StudentNumber;
import models.courseComponent.CourseModel;
import models.courseComponent.FormModels.CourseRegistrationFormModel;
import models.studentComponent.StudentModel;
import models.studentComponent.TeamModel;
import net.unikit.database.interfaces.entities.*;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.showCancelRegistration;
import views.html.showCourseDetails;
import views.html.showCourseOverview;
import views.html.showRegisterCourses;

import java.util.Date;
import java.util.List;

public class CourseController extends Controller {
    /**
     *Redirects to the overview of all registered courses for the current user.
     *@return showCourseOverview page displaying all course registrations for the user
     **/
    public static Result index() {
        return showCourseOverview();
    }

    /**
     *
     * @param courseID
     * @return
     */
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
            e.printStackTrace();
            return CourseRegistrationController.showCourseOverview();
        }

        Team team = null;
        try {
            team = TeamModel.getTeam(sNumber, cID);
        } catch (TeamNotFoundException e) {
            // NOTE: Student is not in a team
            //return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());
        } catch (CourseNotFoundException e) {
            //TODO error message
            e.printStackTrace();
            return CourseRegistrationController.showCourseOverview();

        } catch (StudentNotFoundException e) {
            //TODO error message
            e.printStackTrace();
            return CourseRegistrationController.showCourseOverview();
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
            e.printStackTrace();
            return CourseRegistrationController.showCourseOverview();

        } catch (CourseNotFoundException e) {
            //TODO error message
            e.printStackTrace();
            return CourseRegistrationController.showCourseOverview();
        }
    }

    /**
     * Displays the registered courses for the current user
     * @return showCourseOverview
     */
    public static Result showCourseOverview() {
        Student currentUser = SessionUtils.getCurrentUser(session());
        Date sessionTimeout = SessionUtils.getSessionTimeout(session());

        StudentNumber sNumber = StudentNumber.get(currentUser.getStudentNumber());

        List<Course> allRegisteredCourses = null;
        try {
            allRegisteredCourses = CourseModel.getRegisteredCourses(sNumber);
            return ok(showCourseOverview.render(allRegisteredCourses, currentUser, sessionTimeout));

        } catch (StudentNotFoundException e) {
            //TODO error message
            e.printStackTrace();
            return CourseRegistrationController.showCourseOverview();
        }
    }

    /**
     * Displays the options for registering courses.
     * @return showRegisterCourses page
     */
    public static Result showRegisterCourses() {
        Student currentUser = SessionUtils.getCurrentUser(session());
        Date sessionTimeout = SessionUtils.getSessionTimeout(session());

        StudentNumber sNumber = StudentNumber.get(currentUser.getStudentNumber());

        List<Course> availableCourses = null;

        try {
            availableCourses = CourseModel.getAvailableCourses(sNumber);
        } catch (StudentNotFoundException e) {
            //TODO error message
            e.printStackTrace();
            return CourseRegistrationController.showCourseOverview();
        }

        Form<CourseRegistrationFormModel> courseRegistration =
                Form.form(CourseRegistrationFormModel.class)
                        .fill(new CourseRegistrationFormModel(currentUser.getStudentNumber().getValue(), availableCourses));

        return ok(showRegisterCourses.render(courseRegistration, currentUser, sessionTimeout));
    }

    /**
     * Displays the options for canceling course registrations
     * @return showCancelRegistration page
     */
    public static Result showCancelRegistration(){
        Student currentUser = SessionUtils.getCurrentUser(session());
        Date sessionTimeout = SessionUtils.getSessionTimeout(session());

        StudentNumber sNumber = StudentNumber.get(currentUser.getStudentNumber());

        List<Course> allRegisteredCourses = null;

        try {
            allRegisteredCourses = CourseModel.getRegisteredCourses(sNumber);
        } catch (StudentNotFoundException e) {
            // WTF, student doesnt exist?
        }

        Form<CourseRegistrationFormModel> courseRegistration =
                Form.form(CourseRegistrationFormModel.class)
                        .fill(new CourseRegistrationFormModel(currentUser.getStudentNumber().getValue(), allRegisteredCourses));

        return ok(showCancelRegistration.render(courseRegistration, currentUser, sessionTimeout));
    }

    /**
     *Receives the choices by the current user, persists them to the databank and displays the results.
     *@return showCourseOverview page displaying all course registrations for the user
     **/
    public static Result registerCourses(){
        Student currentUser = SessionUtils.getCurrentUser(session());
        Date sessionTimeout = SessionUtils.getSessionTimeout(session());

        StudentNumber sNumber = StudentNumber.get(currentUser.getStudentNumber());

        //Bind Form-object to Model
        Form<CourseRegistrationFormModel> courseRegistrationForm =
                Form.form(CourseRegistrationFormModel.class)
                        .bindFromRequest();


        try {
            CourseModel.storeCourseRegistrations(sNumber, courseRegistrationForm.get().registeredCourses);
            return CourseRegistrationController.showCourseOverview();

        } catch (CourseNotFoundException e) {
            //TODO error message
            e.printStackTrace();
            return showCourseOverview();

        } catch (StudentNotFoundException e) {
            //TODO error message
            e.printStackTrace();
            return showCourseOverview();

        }
    }

    /**
     *Receives the choices by the current user, deletes the databank entries and displays the results.
     *@return showCourseOverview page displaying all course registrations for the user
     **/
    public static Result cancelCourseRegistration(){
        Student currentUser = SessionUtils.getCurrentUser(session());
        Date sessionTimeout = SessionUtils.getSessionTimeout(session());

        StudentNumber sNumber = StudentNumber.get(currentUser.getStudentNumber());

        //Bind Form-object to Model
        Form<CourseRegistrationFormModel> courseRegistrationForm =
                Form.form(CourseRegistrationFormModel.class)
                        .bindFromRequest();

        CourseRegistrationFormModel crfm = courseRegistrationForm.get();

        try {
            CourseModel.cancelCourseRegistrations(sNumber, crfm.registeredCourses);
            return CourseRegistrationController.showCourseOverview();

        } catch (CourseNotFoundException e) {
            //TODO error message
            e.printStackTrace();
            return showCourseOverview();

        } catch (StudentNotFoundException e) {
            //TODO error message
            e.printStackTrace();
            return showCourseOverview();

        } catch (CourseRegistrationNotFoundException e) {
            //TODO error message
            e.printStackTrace();
            return showCourseOverview();
        }
    }
}
