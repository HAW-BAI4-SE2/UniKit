package models.interfaces;

import java.util.Collection;

public interface Group {
    int getId();
    int getGroupNumber();
    Course getCourse();
    int getMaxSize();
    Collection<Appointment> getAppointments();
}
