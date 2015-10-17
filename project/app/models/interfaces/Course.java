package models.interfaces;

import haw_hamburg.database.interfaces.FieldOfStudy;
import haw_hamburg.database.interfaces.Group;

import java.util.Set;

public interface Course extends haw_hamburg.database.interfaces.Course {
    @Override
    int getId();

    @Override
    String getName();

    @Override
    String getAbbreviation();

    @Override
    int getMinTeamSize();

    @Override
    int getMaxTeamSize();

    @Override
    Integer getMaxParticipants();

    @Override
    Set<FieldOfStudy> getFieldOfStudies();

    @Override
    Set<Group> getGroups();

    Set<Student> getRegistratedStudents();
}
