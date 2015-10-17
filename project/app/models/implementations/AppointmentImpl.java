package models.implementations;

import models.interfaces.Appointment;

import javax.persistence.*;

final class AppointmentImpl implements Appointment {
    @Transient
    public void setImportData(haw_hamburg.database.interfaces.Appointment importData) {
        // ...
    }
}
