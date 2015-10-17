package models.interfaces;

import haw_hamburg.database.interfaces.Course;

import java.util.Collection;

public interface Team {
    int getId();
    Course getCourse();
    Collection<Student> getStudents();
}
