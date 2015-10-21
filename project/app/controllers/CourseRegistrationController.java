package controllers;

/**
 * The CourseRegistrationController controller contains the logic for displaying the courses available to the student for
 * registration and persists the registration choices for further processing.
 * @pre currentUser logged-in student
 * @author Thomas Bednorz on 10/8/2015.
 */

import database.common.interfaces.DatabaseConfiguration;

import database.haw_hamburg.implementations.ImportDatabaseManager;
import database.haw_hamburg.interfaces.Course;
import database.haw_hamburg.interfaces.Student;

import database.unikit.implementations.UnikitDatabaseManager;
import database.unikit.interfaces.CourseRegistration;

import models.courseRegistration.CourseRegistrationFormModel;

import models.courseRegistration.OverviewCourseRegistrationModel;

import play.Play;
import play.data.Form;
import play.mvc.*;
import views.html.*;

import java.io.InputStream;
import java.util.*;

import static database.common.implementations.DatabaseConfigurationUtils.createDatabaseConfiguration;


public class CourseRegistrationController extends Controller {

    private static Student currentUser;

    static {
        /*
        Init database connection
         */
        InputStream inputStreamImport = Play.application().resourceAsStream("hibernate_import.properties");
        DatabaseConfiguration databaseConfigurationImport = createDatabaseConfiguration(inputStreamImport);
        ImportDatabaseManager.init(databaseConfigurationImport);
        ImportDatabaseManager.cacheData();

        InputStream inputStreamUnikit = Play.application().resourceAsStream("hibernate_unikit.properties");
        DatabaseConfiguration databaseConfigurationUnikit = createDatabaseConfiguration(inputStreamUnikit);
        UnikitDatabaseManager.init(databaseConfigurationUnikit);
        //UnikitDatabaseManager.cacheData();

        /*
        The current user for the course registration.
        */
        currentUser = ImportDatabaseManager.getCurrentUser();
    }

    /*
    Displays the registered courses for the student.
    @param currentUser: a logged in user
    @return showOverview page displaying all registered courses for the user
     */
    public static Result showOverview() {
        OverviewCourseRegistrationModel allRegistrationsCurrentUser = new OverviewCourseRegistrationModel(currentUser.getStudentNumber());

        List<CourseRegistration> allCourseRegistrations = UnikitDatabaseManager.getAllCourseRegistrations();

        //If entry in the table of all registered courses matches student number of the current user and isn't already in the list, the course name is added the the OverviewList
        for(CourseRegistration cr : allCourseRegistrations){
            Course currentCourse = ImportDatabaseManager.getCourse(cr.getCourseId());
            if(cr.getStudentNumber().equals(allRegistrationsCurrentUser.getStudentNumber()) && !allRegistrationsCurrentUser.getRegisteredCourses().contains(currentCourse)){
                allRegistrationsCurrentUser.getRegisteredCourses().add(currentCourse);
            }
        }

        return ok(showOverview.render(allRegistrationsCurrentUser));
    }

    /**
    * Displays the options for courese registration.
    * @pre currentUser: a logged in user
    * @return showRegisterCourses page displaying all available courses for regsitration
     */
    public static Result showRegisterCourses() {
        Form<CourseRegistrationFormModel> courseRegistration =
                Form.form(CourseRegistrationFormModel.class)
                        .fill(new CourseRegistrationFormModel(currentUser.getStudentNumber(), currentUser.getAvailableCourses()));

        return ok(showRegisterCourses.render(courseRegistration));
    }

    public static Result showCancelRegistration(){
        List<Course> allCourseRegistrations = new ArrayList<>();

        for(CourseRegistration courseRegistration : UnikitDatabaseManager.getAllCourseRegistrations()){
            if(courseRegistration.getStudentNumber().equals(currentUser.getStudentNumber())){
                allCourseRegistrations.add(ImportDatabaseManager.getCourse(courseRegistration.getCourseId()));
            }
        }

        Form<CourseRegistrationFormModel> courseRegistration =
                Form.form(CourseRegistrationFormModel.class)
                        .fill(new CourseRegistrationFormModel(currentUser.getStudentNumber(), allCourseRegistrations));

        return ok(showCancelRegistration.render(courseRegistration));
    }
    /**
    * Redirects to the course registrations for the current user.
    * @pre currentUser: a logged in user
    * @return redirects to showRegisterCourses
     */
    public static Result index() {
        return showOverview();
    }

    /**
    * Receives the registration choices by the current user, persists them to the databank and displays the results.
    * @param courseRegistrationForm: Form object from the POST request of the showRegisterCourses page
    * @return showOverview page displaying all registered courses for the user
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
                CourseRegistration dbEntry = UnikitDatabaseManager.createCourseRegistration();
                dbEntry.setStudentNumber(crfm.studentNumber);
                dbEntry.setCourseId(Integer.parseInt(course));
                UnikitDatabaseManager.addCourseRegistration(dbEntry);
            }
        }

        return showOverview();
    }

    /**
    *Receives the registration choices by the current user, persists them to the databank and displays the results.
    * @param courseRegistrationForm: Form object from the POST request of the showRegisterCourses page
    * @return showOverview page displaying all registered courses for the user
     **/
    public static Result cancelCourseRegistration(){
        //Bind Form-object to Model
        Form<CourseRegistrationFormModel> courseRegistrationForm =
                Form.form(CourseRegistrationFormModel.class)
                        .bindFromRequest();

        CourseRegistrationFormModel crfm = courseRegistrationForm.get();

        //Persist data
        if(crfm.registeredCourses != null){
            List<CourseRegistration> allCourseRegistrations = UnikitDatabaseManager.getAllCourseRegistrations();
            CourseRegistration dbEntry = UnikitDatabaseManager.createCourseRegistration();
            dbEntry.setStudentNumber(crfm.studentNumber);

            for(String course : crfm.registeredCourses){

                dbEntry.setCourseId(Integer.parseInt(course));

                for(CourseRegistration cr : allCourseRegistrations ){
                    if(crfm.studentNumber.equals(cr.getStudentNumber()) && Integer.parseInt(course) == cr.getCourseId()){
                        dbEntry.setId(cr.getId());
                    }
                }

                UnikitDatabaseManager.deleteCourseRegistration(dbEntry);
            }
        }

        return showOverview();
    }
}
