package models.courseComponent.FormModels;

import net.unikit.database.external.interfaces.Course;

import java.util.Collection;
import java.util.List;

/**
 * Model for the CourseRegistrationForm-form during course registration (Phase 1)
 * @author Thomas Bednorz
 **/
public class CourseRegistrationFormModel {
    //Accoridng to sources the fields of a data models to wrapped by a Form have to be public
    public String studentNumber;

    //Attribute type must be List, otherwise Play bitches around
    public List<String> registeredCourses;

    public Collection<Course> courses;

    //Default constructor needed, even if unused
    public CourseRegistrationFormModel(){}

    public CourseRegistrationFormModel(String studentNumber, Collection<Course> courses){
        this.studentNumber = studentNumber;
        this.courses = courses;
    }
}
