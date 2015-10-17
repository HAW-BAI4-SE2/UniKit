package models.interfaces;

import java.util.Collection;

public interface Team {
    public int getId();
    public Course getCourse();
    Collection<Student> getRegistratedStudents();
}
