package models.courseRegistration;

import models.imports.interfaces.Course;

import java.util.Collection;
import java.util.List;

/**
 * Model to be wrapped by a Form during course registration (Phase 1)
 * Created by Thomas Bednorz on 10/14/2015.
 */
public class CourseRegistrationFormModel {
    //Accoridng to sources the fields of a data models to wrapped by a Form have to be public
    public String studentNumber;
    public List<Integer> registeredCourses;
    public Collection<Course> availableCourse;

    //Default constructor needed, even if unused
    public CourseRegistrationFormModel(){}

    public CourseRegistrationFormModel(String studentNumber, Collection<Course> availableCourses){
        this.studentNumber = studentNumber;
        this.availableCourse = availableCourses;
    }
}
