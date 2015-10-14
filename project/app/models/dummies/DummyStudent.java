package models.dummies;

import models.imports.interfaces.Course;
import models.imports.interfaces.Student;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Thomas Bednorz on 10/8/2015.
 */
public class DummyStudent implements Student {

    Collection<Course> availableCourses = new ArrayList<Course>();

    /*
    All information is hardcoded
     */
    public DummyStudent() {
        Course rn = new DummyCourse(42,"Rechnernetze","RN",2,3);
        Course se2 = new DummyCourse(23,"Software Engineering 2","SE2",2,3);
        availableCourses.add(rn);
        availableCourses.add(se2);
    }

    @Override
    public String getStudentNumber() {
        return "666";
    }

    @Override
    public String getFirstName() {
        return "Horst";
    }

    @Override
    public String getLastName() {
        return "Local";
    }

    @Override
    public String getEmail() {
        return "localhorst@unikit.de";
    }

    @Override
    public int getSemester() {
        return 4;
    }

    @Override
    public Collection<Course> getAvailableCourses() {
        return availableCourses;
    }
}
