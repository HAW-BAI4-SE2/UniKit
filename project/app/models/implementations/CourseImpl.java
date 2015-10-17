package models.implementations;

import models.interfaces.Course;

import javax.persistence.*;

final class CourseImpl implements Course {
    @Transient
    public void setImportData(haw_hamburg.database.interfaces.Course importData) {
        // ...
    }
}
