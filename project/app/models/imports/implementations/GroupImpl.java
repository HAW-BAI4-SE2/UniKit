package models.imports.implementations;

import models.imports.interfaces.Appointment;
import models.imports.interfaces.Course;
import models.imports.interfaces.Group;

import java.util.Collection;

public class GroupImpl implements Group {
    @Override
    public int getGroupNumber() {
        return 0;
    }

    @Override
    public Course getCourse() {
        return null;
    }

    @Override
    public int getMaxSize() {
        return 0;
    }

    @Override
    public Collection<Appointment> getAppointments() {
        return null;
    }
}
