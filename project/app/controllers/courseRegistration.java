package controllers;

/**
 * Created by Thomas Bednorz on 10/8/2015.
 */

import models.courseRegistration.CourseRegistrationFormModel;
import models.dummies.DummyStudent;
import models.hibernate.CourseRegistrationModel;
import models.imports.implementations.hibernate.CourseModel;
import models.imports.interfaces.Course;
import models.imports.interfaces.Student;
import play.data.Form;
import play.mvc.*;
import views.html.*;

import java.util.ArrayList;
import java.util.List;

import static play.data.Form.form;

public class courseRegistration extends Controller {
    /*
    Dummy Student-Object. Used until import interfaces are operational.
     */
    private static Student currentUser = new DummyStudent();

    /*
    Displays the registered courses for the student.
     */
    public static Result showOverview() {
        return ok(showOverview.render("ToDo: Implemented dat shit!"));
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
    Displays an index page in which the user can choose what to do
     */
    public static Result index() {
        return ok(index.render(currentUser));
    }

    /*
    Receives the signup choices by the current user, persists them to the databank and displays the results
     */
    public static Result signUpCourses(){
        Form<CourseRegistrationFormModel> courseRegistrationForm =
                Form.form(CourseRegistrationFormModel.class)
                        .bindFromRequest();

        CourseRegistrationFormModel crm = courseRegistrationForm.get();

        return ok(showOverview.render(crm.studentNumber));
    }
}