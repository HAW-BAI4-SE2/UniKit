package controllers.courseComponent;

/**
 * Handles the interaction between views and models associated with course registrations
 * @author Thomas Bednorz
 */

import assets.SessionUtils;
import models.commonUtils.Exceptions.CourseNotFoundException;
import models.commonUtils.Exceptions.CourseRegistrationNotFoundException;
import models.commonUtils.Exceptions.StudentNotFoundException;
import models.commonUtils.ID.StudentNumber;
import models.courseComponent.CourseModel;
import models.courseComponent.FormModels.CourseRegistrationFormModel;
import net.unikit.database.interfaces.entities.Course;
import net.unikit.database.interfaces.entities.Student;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.showCancelRegistration;
import views.html.showCourseOverview;
import views.html.showRegisterCourses;

import java.util.Date;
import java.util.List;

public class CourseRegistrationController extends Controller {

    /**
     *Redirects to the overview of all registered courses for the current user.
     *@pre currentUser: a logged in user
     *@return showCourseOverview page displaying all course registrations for the user
     **/
    public static Result index() {
        return showCourseOverview();
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
        } catch (StudentNotFoundException e) {

            // WTF, student doesnt exist?
        }

        return ok(showCourseOverview.render(allRegisteredCourses, currentUser, sessionTimeout));
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
            // Student doesnt exist, wtf
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
        } catch (CourseNotFoundException e) {
            e.printStackTrace();
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        }

        return showCourseOverview();
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

        } catch (CourseNotFoundException e) {
            //TODO Error handling
            e.printStackTrace();

        } catch (StudentNotFoundException e) {
            //TODO Error handling
            e.printStackTrace();

        } catch (CourseRegistrationNotFoundException e) {
            //TODO Error handling
            e.printStackTrace();
        }

        return showCourseOverview();
    }
}
