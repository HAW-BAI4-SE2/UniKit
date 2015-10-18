package models.interfaces;

import haw_hamburg.database.interfaces.Course;
import haw_hamburg.database.interfaces.FieldOfStudy;

import java.util.Collection;

public interface Student extends haw_hamburg.database.interfaces.Student {
    @Override
    String getStudentNumber();

    @Override
    String getFirstName();

    @Override
    String getLastName();

    @Override
    String getEmail();

    @Override
    int getSemester();

    @Override
    FieldOfStudy getFieldOfStudy();

    @Override
    Collection<Course> getAvailableCourses();

    Collection<Course> getRegistratedCourses();

    Collection<Team> getRegistratedTeams();
}
