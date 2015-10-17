package models.implementations;

import models.interfaces.Group;

import javax.persistence.*;

final class GroupImpl implements Group {
    @Transient
    public void setImportData(haw_hamburg.database.interfaces.Group importData) {
        // ...
    }
}
