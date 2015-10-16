package controllers;

/**
 * The courseRegistration controller contains the logic for displaying the courses available to the student for
 * registration and persists the registration choices for further processing.
 * @pre currentUser logged-in student
 * @author Thomas Bednorz on 10/8/2015.
 */

import models.courseRegistration.CourseRegistrationFormModel;
import models.dummies.DummyStudent;
import models.hibernate.CourseRegistrationModel;
import models.hibernate.SessionFactoryGenerator;
import models.imports.implementations.hibernate.CourseModel;
import models.imports.interfaces.Course;
import models.imports.interfaces.Student;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import play.data.Form;
import play.mvc.*;
import views.html.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static play.data.Form.form;

public class courseRegistration extends Controller {
    /*
    Dummy Student-Object. Used until import interfaces are operational.
     */
    private static Student currentUser = new DummyStudent();

    /*
    The SessionFactoryGenerator object used for Hibernate persistence actions.
     */
    private static SessionFactory sessionFactory = SessionFactoryGenerator.buildSessionFactory();


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
    @param courseRegistrationForm: Method binds a Form object from the POST request and persists the data
    @return ShowOverview Page: Page displaying all persisted course choices
     */
    public static Result signUpCourses(){
        Form<CourseRegistrationFormModel> courseRegistrationForm =
                Form.form(CourseRegistrationFormModel.class)
                        .bindFromRequest();

        CourseRegistrationFormModel crm = courseRegistrationForm.get();

        for(Integer course : crm.registeredCourses){
            persistCourseRegistration(new CourseRegistrationModel(crm.studentNumber,course));
        }

        Collection<CourseRegistrationModel> courseRegistrationsForCurrentUser = getCourseRegistrationsByStudentId(crm.studentNumber);


        return ok(showOverview.render(courseRegistrationsForCurrentUser));
    }

    /*
    Persists a course registration choice to the database
    @param courseRegistration: A course registration tuple consisting of a student number and the registered course
     */
    private static void persistCourseRegistration(CourseRegistrationModel courseRegistration){
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.save(courseRegistration);
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
    }

    /*
    Finds all persisted course registrations by the student number
     */
    private static Collection<CourseRegistrationModel> getCourseRegistrationsByStudentId(String studentNumber){
        Collection<CourseRegistrationModel> courseRegistrations = new ArrayList<>();

        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            courseRegistrations = session.createQuery("FROM COURSE_REGISTRATIONS WHERE student_id = " +studentNumber).list();
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }

        return courseRegistrations;
    }
}