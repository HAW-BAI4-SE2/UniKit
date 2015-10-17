package models.implementations;

import models.interfaces.Student;

import javax.persistence.*;

final class StudentImpl implements Student {
    @Transient
    public void setImportData(haw_hamburg.database.interfaces.Student importData) {
        // ...
    }
}
