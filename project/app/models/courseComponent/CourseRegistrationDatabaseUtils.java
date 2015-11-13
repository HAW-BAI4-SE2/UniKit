package models.courseComponent;

import assets.Global;
import models.utils.UnikitDatabaseUtils;

import net.unikit.database.external.interfaces.Course;
import net.unikit.database.unikit_.interfaces.CourseRegistration;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the database actions associated with course registrations. Used by the CourseRegistrationController
 * @author Thomas Bednorz
 */

public class CourseRegistrationDatabaseUtils {

    /**
     * Returns a List of all courses for which the student is registered
     * @param studentNumber the student number of the student
     * @return List of Course-object for which the student is registered
     */
    public static List<Course> getRegisteredCourses(String studentNumber) {
        List<Course> allRegisteredCoursesStudent = new ArrayList<>();

        List<CourseRegistration> allCourseRegistrations = Global.getCourseRegistrationManager().getAllCourseRegistrations();
        List<CourseRegistration> allCourseRegistrationsStudent = new ArrayList<>();

        for(CourseRegistration currentCourseRegistration : allCourseRegistrations){
            if(currentCourseRegistration.getStudentNumber().equals(studentNumber)){
                allRegisteredCoursesStudent.add(
                        UnikitDatabaseUtils.getCourseByID(
                                currentCourseRegistration.getCourseId()));
            }
        }

        return allRegisteredCoursesStudent;
    }

    /**
     * Returns a List of all courses for which the student is registered
     * @param studentNumber the student number of the student
     * @return List of Course-objects for which the student can register
     */
    public static List<Course> getAvailableCourses(String studentNumber) {
        List<Course> availableCourses = Global.getCourseManager().getAllCourses();
        availableCourses.removeAll(getRegisteredCourses(studentNumber));

        return availableCourses;
    }
}
