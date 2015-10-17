package models.interfaces;

import java.util.Date;

public interface Appointment {
    Group getGroup();
    Date getStart();
    Date getEnd();
}
