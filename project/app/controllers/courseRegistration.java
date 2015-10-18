package controllers;

/**
 * The courseRegistration controller contains the logic for displaying the courses available to the student for
 * registration and persists the registration choices for further processing.
 * @pre currentUser logged-in student
 * @author Thomas Bednorz on 10/8/2015.
 */

import database.common.interfaces.DatabaseConfiguration;
import database.haw_hamburg.implementations.ImportDatabaseManager;
import database.haw_hamburg.interfaces.Student;
import database.unikit.implementations.UnikitDatabaseManager;
import database.unikit.interfaces.CourseRegistration;

import models.courseRegistration.CourseRegistrationFormModel;

import play.Play;
import play.data.Form;
import play.mvc.*;
import views.html.*;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;


import static database.common.implementations.DatabaseConfigurationUtils.createDatabaseConfiguration;
import static play.data.Form.form;

public class courseRegistration extends Controller {

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
        UnikitDatabaseManager.cacheData();

        /*
        The current user for the course registration.
        */
        currentUser = ImportDatabaseManager.getCurrentUser();
    }

    /*
    Displays the registered courses for the student.
     */
    public static Result showOverview() {
        return ok(showOverview.render(UnikitDatabaseManager.getAllCourseRegistrations()));
    }

    /*
    Displays the options for courese registration.
    @param availableCourses A Map containing course-name and course-id of all available courses
    @param courseRegistrationForm The Form object used to bind the registration choices
     */
    public static Result showCourseRegistration() {
        Form<CourseRegistrationFormModel> courseRegistration =
                Form.form(CourseRegistrationFormModel.class)
                        .fill(new CourseRegistrationFormModel(currentUser.getStudentNumber(), currentUser.getAvailableCourses()));

        return ok(showCourseRegistration.render(courseRegistration));
    }

    /*
    Redirects to the course registrations for the current user.
    @param getAllCourseRegistration: List of all course registrations. All? or all for the current user. No doc, no one knows.
     */
    public static Result index() {
        if(UnikitDatabaseManager.getAllCourseRegistrations() != null){
            return ok(showOverview.render(UnikitDatabaseManager.getAllCourseRegistrations()));
        }
        return showCourseRegistration();
    }

    /*
    Receives the registration choices by the current user, persists them to the databank and displays the results.
    @param courseRegistrationForm: Method binds a Form object from the POST request and persists the data
    @return ShowOverview Page: Page displaying all persisted course choices
     */
    public static Result signUpCourses(){
        Form<CourseRegistrationFormModel> courseRegistrationForm =
                Form.form(CourseRegistrationFormModel.class)
                        .bindFromRequest();

        CourseRegistrationFormModel crfm = courseRegistrationForm.get();

        for(String course : crfm.registeredCourses){
            CourseRegistration dbEntry = UnikitDatabaseManager.createCourseRegistration();
            dbEntry.setStudentNumber(crfm.studentNumber);
            dbEntry.setCourseId(Integer.getInteger(course));
            UnikitDatabaseManager.addCourseRegistration(dbEntry);
        }

        return showOverview();
    }
}