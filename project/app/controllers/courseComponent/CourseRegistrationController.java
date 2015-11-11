package controllers.courseComponent;

/**
 * Handles the interaction between views and models associated with course registrations
 * @author Thomas Bednorz
 */

import assets.Global;
import assets.SessionUtils;

import models.courseComponent.CourseDatabaseUtils;
import models.courseComponent.CourseRegistrationDatabaseUtils;
import models.courseComponent.FormModels.CourseRegistrationFormModel;

import net.unikit.database.external.interfaces.Course;
import net.unikit.database.external.interfaces.Student;
import net.unikit.database.unikit_.interfaces.CourseRegistration;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import views.html.*;

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

        List<Course> allCourseRegistrationsForStudent =
                CourseRegistrationDatabaseUtils.getRegisteredCourses(currentUser.getStudentNumber());

        return ok(showCourseOverview.render(allCourseRegistrationsForStudent, currentUser, sessionTimeout));
    }

    /**
     * Displays the options for registering courses.
     * @return showRegisterCourses page
     */
    public static Result showRegisterCourses() {
        Student currentUser = SessionUtils.getCurrentUser(session());
        Date sessionTimeout = SessionUtils.getSessionTimeout(session());

        List<Course> availableCourses =
                CourseRegistrationDatabaseUtils.getAvailableCourses(currentUser.getStudentNumber());

        Form<CourseRegistrationFormModel> courseRegistration =
                Form.form(CourseRegistrationFormModel.class)
                        .fill(new CourseRegistrationFormModel(currentUser.getStudentNumber(), availableCourses));

        return ok(showRegisterCourses.render(courseRegistration, currentUser, sessionTimeout));
    }

    /**
     * Displays the options for canceling course registrations
     * @return showCancelRegistration page
     */
    public static Result showCancelRegistration(){
        Student currentUser = SessionUtils.getCurrentUser(session());
        Date sessionTimeout = SessionUtils.getSessionTimeout(session());

        List<Course> allCourseRegistrations =
                CourseRegistrationDatabaseUtils.getRegisteredCourses(currentUser.getStudentNumber());

        Form<CourseRegistrationFormModel> courseRegistration =
                Form.form(CourseRegistrationFormModel.class)
                        .fill(new CourseRegistrationFormModel(currentUser.getStudentNumber(), allCourseRegistrations));

        return ok(showCancelRegistration.render(courseRegistration, currentUser, sessionTimeout));
    }

    /**
    *Receives the choices by the current user, persists them to the databank and displays the results.
    *@return showCourseOverview page displaying all course registrations for the user
     **/
    public static Result registerCourses(){
        Student currentUser = SessionUtils.getCurrentUser(session());
        Date sessionTimeout = SessionUtils.getSessionTimeout(session());

        //Bind Form-object to Model
        Form<CourseRegistrationFormModel> courseRegistrationForm =
                Form.form(CourseRegistrationFormModel.class)
                        .bindFromRequest();

        CourseRegistrationFormModel crfm = courseRegistrationForm.get();

        //Persist data
        //TODO: form validation
        if(crfm.registeredCourses != null){
            for(String course : crfm.registeredCourses){
                CourseRegistration dbEntry = Global.getCourseRegistrationManager().createCourseRegistration();
                dbEntry.setStudentNumber(currentUser.getStudentNumber());
                dbEntry.setCourseId(Integer.parseInt(course));
                Global.getCourseRegistrationManager().addCourseRegistration(dbEntry);
            }
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

        //Bind Form-object to Model
        Form<CourseRegistrationFormModel> courseRegistrationForm =
                Form.form(CourseRegistrationFormModel.class)
                        .bindFromRequest();

        CourseRegistrationFormModel crfm = courseRegistrationForm.get();

        //Persist data
        //TODO: form validation
        if(crfm.registeredCourses != null){
            List<CourseRegistration> allCourseRegistrations = Global.getCourseRegistrationManager().getAllCourseRegistrations();
            CourseRegistration dbEntry = Global.getCourseRegistrationManager().createCourseRegistration();
            dbEntry.setStudentNumber(currentUser.getStudentNumber());

            for(String course : crfm.registeredCourses){

                dbEntry.setCourseId(Integer.parseInt(course));

                for(CourseRegistration cr : allCourseRegistrations ){
                    if(currentUser.getStudentNumber().equals(cr.getStudentNumber()) && Integer.parseInt(course) == cr.getCourseId()){
                        Global.getCourseRegistrationManager().deleteCourseRegistration(cr);
                    }
                }
            }
        }

        return showCourseOverview();
    }
}
