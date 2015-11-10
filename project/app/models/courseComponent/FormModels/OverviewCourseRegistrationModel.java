package models.courseComponent.FormModels;

import net.unikit.database.external.interfaces.Course;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Data-model for displaying of course registrations in the showCourseOverview-page
 * @author Thomas Bednorz
 */
public class OverviewCourseRegistrationModel{
    private String studentNumber;
    private List<Course> registeredCourses;

    public OverviewCourseRegistrationModel(String studentNumber){
        this.registeredCourses = new ArrayList<>();
        this.studentNumber = studentNumber;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public List<Course> getRegisteredCourses() {
        return registeredCourses;
    }

}
