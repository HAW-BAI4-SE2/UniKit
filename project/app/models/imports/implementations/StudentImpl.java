package models.imports.implementations;

import models.imports.implementations.hibernate.StudentModel;
import models.imports.interfaces.Course;
import models.imports.interfaces.Student;

import java.util.Collection;

class StudentImpl implements Student {

    StudentModel student;

    public StudentImpl(StudentModel studentModel){
        student = studentModel;
    }

    @Override
    public String getStudentNumber() {
        return student.getStudentNumber();
    }

    @Override
    public String getFirstName() {
        return student.getFirstName();
    }

    @Override
    public String getLastName() {
        return student.getLastName();
    }

    @Override
    public String getEmail() {
        return student.getEmail();
    }

    @Override
    public int getSemester() {
        return student.getSemester();
    }

    @Override
    public Collection<Course> getAvailableCourses() {
        return null;
    }

    @Override
    public boolean equals(Object o){ return student.equals(o); }

    @Override
    public int hashCode(){ return student.hashCode(); }

    @Override
    public String toString(){ return student.toString(); }
}
