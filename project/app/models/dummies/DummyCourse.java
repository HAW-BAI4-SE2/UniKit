package models.dummies;

import database.haw_hamburg.interfaces.Course;
import database.haw_hamburg.interfaces.FieldOfStudy;
import database.haw_hamburg.interfaces.Group;

import java.util.Set;

/**
 * Created by Thomas Bednorz on 10/8/2015.
 */
public class DummyCourse implements Course {

    public DummyCourse(int id, String name, String shortcut, int minTeamSize, int maxTeamSize) {
        this.id = id;
        this.name = name;
        this.shortcut = shortcut;
        this.minTeamSize = minTeamSize;
        this.maxTeamSize = maxTeamSize;
    }

    private int id;
    private String name;
    private String shortcut;
    private int minTeamSize;
    private int maxTeamSize;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAbbreviation() {
        return shortcut;
    }

    @Override
    public int getMinTeamSize() {
        return minTeamSize;
    }

    @Override
    public int getMaxTeamSize() {
        return maxTeamSize;
    }

    @Override
    public Integer getMaxParticipants() {
        return 7;
    }

    @Override
    public Set<FieldOfStudy> getFieldOfStudies() {
        return null;
    }

    @Override
    public Set<Group> getGroups() {
        return null;
    }
}
