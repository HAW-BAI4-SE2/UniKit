package controllers.courseComponent;

/**
 * The CourseRegistrationController controller contains the logic for displaying the courses available to the student for
 * registration and delegated any persisting operations to the DB-manager.
 * @pre currentUser logged-in student
 * @author Thomas Bednorz on 10/8/2015.
 */

import assets.Global;
import assets.SessionUtils;
import models.courseComponent.CourseDatabaseConnector;
import models.courseComponent.FormModels.CourseRegistrationFormModel;
import net.unikit.database.external.interfaces.Course;
import net.unikit.database.external.interfaces.Student;
import net.unikit.database.unikit_.interfaces.CourseRegistration;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CourseRegistrationController extends Controller {
    /**
    *Displays the registered courses for the current user
    *@pre currentUser: a logged in user
    *@return showCourseOverview page displaying all course registrations for the user
     **/
    public static Result showCourseOverview() {
        Student currentUser = SessionUtils.getCurrentUser(session());
        Date sessionTimeout = SessionUtils.getSessionTimeout(session());

        String studentNumber = currentUser.getStudentNumber();
        List<Course> registeredCourses = new ArrayList<>();

        List<CourseRegistration> allCourseRegistrations = Global.getCourseRegistrationManager().getAllCourseRegistrations();

        //If entry in the table of all registered courses matches student number of the current user and isn't already in the list, the course name is added the the OverviewList
        for(CourseRegistration cr : allCourseRegistrations){
            Course currentCourse = Global.getCourseManager().getCourse(cr.getCourseId());
            if(cr.getStudentNumber().equals(studentNumber) && !registeredCourses.contains(currentCourse)){
                registeredCourses.add(currentCourse);
            }
        }

        return ok(showCourseOverview.render(registeredCourses, currentUser, sessionTimeout));
    }

    /**
    *Displays the options for course registration.
    *@pre currentUser: a logged in user
    *@return showRegisterCourses page displaying all available courses for regsitration
     **/
    public static Result showRegisterCourses() {
        Student currentUser = SessionUtils.getCurrentUser(session());
        Date sessionTimeout = SessionUtils.getSessionTimeout(session());

        List<Course> availableCourses = new ArrayList<>(Global.getCourseManager().getAllCourses());
        availableCourses.removeAll(currentUser.getCompletedCourses());


        Form<CourseRegistrationFormModel> courseRegistration =
                Form.form(CourseRegistrationFormModel.class)
                        .fill(new CourseRegistrationFormModel(currentUser.getStudentNumber(), availableCourses));

        return ok(showCourseRegister.render(courseRegistration));
    }

    public static Result showCancelRegistration(){
        Student currentUser = SessionUtils.getCurrentUser(session());
        Date sessionTimeout = SessionUtils.getSessionTimeout(session());

        List<Course> allCourseRegistrations = new ArrayList<>();

        for(CourseRegistration courseRegistration : Global.getCourseRegistrationManager().getAllCourseRegistrations()){
            if(courseRegistration.getStudentNumber().equals(currentUser.getStudentNumber())){
                allCourseRegistrations.add(Global.getCourseManager().getCourse(courseRegistration.getCourseId()));
            }
        }

        Form<CourseRegistrationFormModel> courseRegistration =
                Form.form(CourseRegistrationFormModel.class)
                        .fill(new CourseRegistrationFormModel(currentUser.getStudentNumber(), allCourseRegistrations));

        return ok(showCourseUnregister.render(courseRegistration));
    }
    /**
    *Redirects to the overview of all registered courses for the current user.
    *@pre currentUser: a logged in user
    *@return showCourseOverview page displaying all course registrations for the user
     **/
    public static Result index() {
        return showCourseOverview();
    }

    /**
    *Receives the choices by the current user, persists them to the databank and displays the results.
    *@param courseRegistrationForm: Form object from the POST request of the showRegisterCourses page
    *@return showCourseOverview page displaying all course registrations for the user
     **/
    public static Result registerCourses(){
        //Bind Form-object to Model
        Form<CourseRegistrationFormModel> courseRegistrationForm =
                Form.form(CourseRegistrationFormModel.class)
                        .bindFromRequest();

        CourseRegistrationFormModel crfm = courseRegistrationForm.get();

        //Persist data
        if(crfm.registeredCourses != null){
            for(String course : crfm.registeredCourses){
                CourseRegistration dbEntry = Global.getCourseRegistrationManager().createCourseRegistration();
                dbEntry.setStudentNumber(crfm.studentNumber);
                dbEntry.setCourseId(Integer.parseInt(course));
                Global.getCourseRegistrationManager().addCourseRegistration(dbEntry);
            }
        }

        return showCourseOverview();
    }

    /**
    *Receives the choices by the current user, deletes the databank entries and displays the results.
    *@param courseRegistrationForm: Form object from the POST request of the showRegisterCourses page
    *@return showCourseOverview page displaying all course registrations for the user
     **/
    public static Result cancelCourseRegistration(){
        //Bind Form-object to Model
        Form<CourseRegistrationFormModel> courseRegistrationForm =
                Form.form(CourseRegistrationFormModel.class)
                        .bindFromRequest();

        CourseRegistrationFormModel crfm = courseRegistrationForm.get();

        //Persist data
        if(crfm.registeredCourses != null){
            List<CourseRegistration> allCourseRegistrations = Global.getCourseRegistrationManager().getAllCourseRegistrations();
            CourseRegistration dbEntry = Global.getCourseRegistrationManager().createCourseRegistration();
            dbEntry.setStudentNumber(crfm.studentNumber);

            for(String course : crfm.registeredCourses){

                dbEntry.setCourseId(Integer.parseInt(course));

                for(CourseRegistration cr : allCourseRegistrations ){
                    if(crfm.studentNumber.equals(cr.getStudentNumber()) && Integer.parseInt(course) == cr.getCourseId()){
                        Global.getCourseRegistrationManager().deleteCourseRegistration(cr);
                    }
                }
            }
        }

        return showCourseOverview();
    }

    /**
     * Changes the status of a student for a course to either true (is in the team pool) or false (is in the single pool)
     * @param studentNumber the student for which the status will be changed
     * @param courseID the course for which the status of the student will be changed
     * @param status true if student is in team, else false
     */
    public static void changeTeamRegistrationStatus(String studentNumber, int courseID, boolean status){
        CourseDatabaseConnector.changeTeamRegistrationStatus(studentNumber, courseID, status);
    }
}
