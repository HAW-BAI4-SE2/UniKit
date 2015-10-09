package controllers;

/**
 * Created by Thomas Bednorz on 10/8/2015.
 */

import models.dummies.DummyStudent;
import models.imports.interfaces.Student;
import play.mvc.*;
import views.html.*;

public class courseEnrollment extends Controller {
    /*
    Until the import interfaces are operational, Horst Local will pose as the dummy "Student" Object to enroll in courses.
    Hardcoded is all relevant information on him (name, semester etc...), as well as two courses he can enroll in.
    Not very elegant, but it should suffice.
     */
    private static Student currentUser = new DummyStudent();


    /*
    Displays the enrolled courses for the student.
     */
    public static Result showOverview() {
        return ok(showOverview.render("ToDo: Implemented dat shit!"));
    }

    /*
    Displays the choices the student can make regarding enrollment for courses.
     */
    public static Result showAvailableCourses() {
        return ok(showAvailableCourses.render(currentUser));
    }

    /*
    DEPRECATED FOR DEPLOYEMENT:
    Displays an index page in which the user can choose what to do
     */
    public static Result index() {
        return ok(index.render(currentUser));
    }
}
