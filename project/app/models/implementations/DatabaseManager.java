package models.implementations;

import haw_hamburg.database.interfaces.DatabaseConfiguration;
import models.interfaces.Course;
import models.interfaces.Student;
import models.interfaces.Team;
import play.Play;

import java.io.InputStream;
import java.util.Collection;

public final class DatabaseManager {
    private DatabaseManager() {
    }

    private static DatabaseConfiguration createConfiguration(InputStream configuration) throws NullPointerException, IllegalStateException, IllegalArgumentException {
        return DatabaseConfigurationImpl.create(configuration);
    }

    public static void initImportData() {
        InputStream inputStream = Play.application().resourceAsStream("hibernate_import.properties");
        DatabaseConfiguration configuration = createConfiguration(inputStream);
        haw_hamburg.database.implementations.DatabaseManager.init(configuration);
    }

    public static void initUnikitData() {
        InputStream inputStream = Play.application().resourceAsStream("hibernate_unikit.properties");
        DatabaseConfiguration configuration = createConfiguration(inputStream);
        // TODO: ...
    }

    public static void cacheImportData() {
        haw_hamburg.database.implementations.DatabaseManager.cacheData();
    }

    public static void cacheUnikitData() {
        // TODO: ...
    }

    public static Student getCurrentUser() {
        // TODO: ...
        //StudentImpl student = new StudentImpl();
        //haw_hamburg.database.interfaces.Student importStudent = haw_hamburg.database.implementations.DatabaseManager.getCurrentUser();
        //student.setData(importStudent);
        return null;
    }

    public static Collection<Student> getStudents() {
        //return haw_hamburg.database.implementations.DatabaseManager.getStudents();
        // TODO: ...
        return null;
    }

    public static Collection<Course> getCourses() {
        //return haw_hamburg.database.implementations.DatabaseManager.getCourses();
        // TODO: ...
        return null;
    }

    public static Collection<Team> getTeams() {
        // TODO: ...
        return null;
    }
}
