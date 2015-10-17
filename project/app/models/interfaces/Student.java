package models.interfaces;

import java.util.Collection;

public interface Student {
    String getStudentNumber();
    String getFirstName();
    String getLastName();
    String getEmail();
    int getSemester();
    FieldOfStudy getFieldOfStudy();
    Collection<Course> getAvailableCourses();
    Collection<Course> getRegistratedCourses();
    Collection<Team> getRegistratedTeams();
}
