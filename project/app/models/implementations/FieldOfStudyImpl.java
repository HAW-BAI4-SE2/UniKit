package models.implementations;

import models.interfaces.FieldOfStudy;

import javax.persistence.*;

final class FieldOfStudyImpl implements FieldOfStudy {
    @Transient
    public void setImportData(haw_hamburg.database.interfaces.FieldOfStudy importData) {
        // ...
    }
}
