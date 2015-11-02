package assets;

import net.unikit.database.common.interfaces.DatabaseConfiguration;
import net.unikit.database.external.implementations.ImportDatabaseManagerFactory;
import net.unikit.database.external.interfaces.*;
import net.unikit.database.unikit_.implementations.UniKitDatabaseManagerFactory;
import net.unikit.database.unikit_.interfaces.*;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.Play;

import java.io.IOException;
import java.io.InputStream;

import static net.unikit.database.common.implementations.DatabaseConfigurationUtils.createDatabaseConfigurationFromProperties;

public final class Global extends GlobalSettings {
    private static AppointmentManager appointmentManager;
    private static CourseGroupManager courseGroupManager;
    private static CourseManager courseManager;
    private static FieldOfStudyManager fieldOfStudyManager;
    private static StudentManager studentManager;
    private static CourseRegistrationManager courseRegistrationManager;
    private static TeamApplicationManager teamApplicationManager;
    private static TeamInvitationManager teamInvitationManager;
    private static TeamManager teamManager;
    private static TeamRegistrationManager teamRegistrationManager;

    @Override
    public void onStart(Application app) {
        Logger.info("Initializing application...");

        // Load external database
        Logger.info("Loading external database...");
        InputStream inputStreamExternal = Play.application().resourceAsStream("database_external.properties");
        DatabaseConfiguration databaseConfigurationExternal = null;
        try {
            databaseConfigurationExternal = createDatabaseConfigurationFromProperties(inputStreamExternal);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ImportDatabaseManager externalDatabaseManager = ImportDatabaseManagerFactory.createDatabaseManager(databaseConfigurationExternal);

        // Load internal database
        Logger.info("Loading internal database...");
        InputStream inputStreamInternal = Play.application().resourceAsStream("database_internal.properties");
        DatabaseConfiguration databaseConfigurationInternal = null;
        try {
            databaseConfigurationInternal = createDatabaseConfigurationFromProperties(inputStreamInternal);
        } catch (IOException e) {
            e.printStackTrace();
        }
        UniKitDatabaseManager internalDatabaseManager = UniKitDatabaseManagerFactory.createDatabaseManager(databaseConfigurationInternal);

        // Store database managers in global values
        Logger.info("Initializing global values...");
        Global.appointmentManager = externalDatabaseManager.getAppointmentManager();
        Global.courseGroupManager = externalDatabaseManager.getCourseGroupManager();
        Global.courseManager = externalDatabaseManager.getCourseManager();
        Global.fieldOfStudyManager = externalDatabaseManager.getFieldOfStudyManager();
        Global.studentManager = externalDatabaseManager.getStudentManager();
        Global.courseRegistrationManager = internalDatabaseManager.getCourseRegistrationManager();
        Global.teamApplicationManager = internalDatabaseManager.getTeamApplicationManager();
        Global.teamInvitationManager = internalDatabaseManager.getTeamInvitationManager();
        Global.teamManager = internalDatabaseManager.getTeamManager();
        Global.teamRegistrationManager = internalDatabaseManager.getTeamRegistrationManager();

        Logger.info("Application initialized!");
    }

    @Override
    public void onStop(Application app) {
        Logger.info("Application shutdown...");
    }

    public static AppointmentManager getAppointmentManager() {
        return appointmentManager;
    }

    public static CourseGroupManager getCourseGroupManager() {
        return courseGroupManager;
    }

    public static CourseManager getCourseManager() {
        return courseManager;
    }

    public static FieldOfStudyManager getFieldOfStudyManager() {
        return fieldOfStudyManager;
    }

    public static StudentManager getStudentManager() {
        return studentManager;
    }

    public static CourseRegistrationManager getCourseRegistrationManager() {
        return courseRegistrationManager;
    }

    public static TeamApplicationManager getTeamApplicationManager() {
        return teamApplicationManager;
    }

    public static TeamInvitationManager getTeamInvitationManager() {
        return teamInvitationManager;
    }

    public static TeamManager getTeamManager() {
        return teamManager;
    }

    public static TeamRegistrationManager getTeamRegistrationManager() {
        return teamRegistrationManager;
    }
}
