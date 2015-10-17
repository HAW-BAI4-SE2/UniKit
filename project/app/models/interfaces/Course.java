package models.interfaces;

import java.util.Collection;

public interface Course {
    int getId();
    String getName();
    String getAbbreviation();
    int getMinTeamSize();
    int getMaxTeamSize();
    Integer getMaxParticipants();
    Collection<FieldOfStudy> getFieldOfStudies();
    Collection<Group> getGroups();
    Collection<Student> getRegistratedStudents();
}
