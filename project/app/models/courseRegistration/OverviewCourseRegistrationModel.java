package models.courseRegistration;

import net.unikit.database.external.interfaces.Course;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Data-model for displaying of course registrations in the showOverview-page
 * @author Thomas Bednorz
 */
public class OverviewCourseRegistrationModel{
    private String studentNumber;
    private Collection<Course> registeredCourses;

    public OverviewCourseRegistrationModel(String studentNumber){
        this.registeredCourses = new ArrayList<>();
        this.studentNumber = studentNumber;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public Collection<Course> getRegisteredCourses() {
        return registeredCourses;
    }

}
