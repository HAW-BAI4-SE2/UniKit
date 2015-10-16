package models.imports.implementations;

import models.imports.interfaces.Course;
import models.imports.interfaces.Student;

import java.util.Collection;

class StudentImpl implements Student {
    @Override
    public String getStudentNumber() {
        return null;
    }

    @Override
    public String getFirstName() {
        return null;
    }

    @Override
    public String getLastName() {
        return null;
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public int getSemester() {
        return 0;
    }
	
	@Override
	public FieldOfStudy getFieldOfStudy() {
        return null;
    }

    @Override
    public Collection<Course> getAvailableCourses() {
        return null;
    }
}
