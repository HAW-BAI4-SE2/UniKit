package assets;


import net.unikit.database.implementations.DatabaseManagerFactory;
import net.unikit.database.interfaces.DatabaseConfiguration;
import net.unikit.database.interfaces.DatabaseManager;
import net.unikit.database.interfaces.managers.*;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.Play;

import java.io.IOException;
import java.io.InputStream;

import static net.unikit.database.implementations.DatabaseConfigurationUtils.createDatabaseConfigurationFromProperties;

public final class Global extends GlobalSettings {
    // MISSING?
//    private static AppointmentManager appointmentManager;
    private static CourseGroupManager courseGroupManager;
    private static CourseManager courseManager;
    private static FieldOfStudyManager fieldOfStudyManager;
    private static StudentManager studentManager;
    private static CourseRegistrationManager courseRegistrationManager;
    private static MembershipRequestManager membershipRequestManager;
    private static TeamInvitationManager teamInvitationManager;
    private static TeamManager teamManager;
    private static TeamRegistrationManager teamRegistrationManager;

    @Override
    public void beforeStart(Application application) {
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
//        ImportDatabaseManager externalDatabaseManager = ImportDatabaseManagerFactory.createDatabaseManager(databaseConfigurationExternal);


        // Load internal database
        Logger.info("Loading internal database...");
        InputStream inputStreamInternal = Play.application().resourceAsStream("database_internal.properties");
        DatabaseConfiguration databaseConfigurationInternal = null;
        try {
            databaseConfigurationInternal = createDatabaseConfigurationFromProperties(inputStreamInternal);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        UniKitDatabaseManager internalDatabaseManager = UniKitDatabaseManagerFactory.createDatabaseManager(databaseConfigurationInternal);

        DatabaseManager DatabaseManager = DatabaseManagerFactory.createDatabaseManager(databaseConfigurationInternal,  databaseConfigurationExternal);

        // Store database managers in global values
        Logger.info("Initializing database managers...");
//        Global.appointmentManager = externalDatabaseManager.getAppointmentManager();
//        Global.courseGroupManager = externalDatabaseManager.getCourseGroupManager();
//        Global.courseManager = externalDatabaseManager.getCourseManager();
//        Global.fieldOfStudyManager = externalDatabaseManager.getFieldOfStudyManager();
//        Global.studentManager = externalDatabaseManager.getStudentManager();
//        Global.courseRegistrationManager = internalDatabaseManager.getCourseRegistrationManager();
//        Global.teamApplicationManager = internalDatabaseManager.getMembershipRequestManager();
//        Global.teamInvitationManager = internalDatabaseManager.getTeamInvitationManager();
//        Global.teamManager = internalDatabaseManager.getTeamManager();
//        Global.teamRegistrationManager = internalDatabaseManager.getTeamRegistrationManager();

        // ERROR: ApplicationManager missing?
        Global.courseGroupManager = DatabaseManagerFactory.getDatabaseManager().getCourseGroupManager();
        Global.courseManager = DatabaseManagerFactory.getDatabaseManager().getCourseManager();
        Global.fieldOfStudyManager = DatabaseManagerFactory.getDatabaseManager().getFieldOfStudyManager();
        Global.studentManager = DatabaseManagerFactory.getDatabaseManager().getStudentManager();
        Global.courseRegistrationManager = DatabaseManagerFactory.getDatabaseManager().getCourseRegistrationManager();
        Global.membershipRequestManager = DatabaseManagerFactory.getDatabaseManager().getMembershipRequestManager();
        Global.teamInvitationManager = DatabaseManagerFactory.getDatabaseManager().getTeamInvitationManager();
        Global.teamManager = DatabaseManagerFactory.getDatabaseManager().getTeamManager();
        Global.teamRegistrationManager = DatabaseManagerFactory.getDatabaseManager().getTeamRegistrationManager();

        Logger.info("Application initialized!");
    }

//    public static AppointmentManager getAppointmentManager() {
//        return appointmentManager;
//    }

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

    public static MembershipRequestManager getMembershipRequestManager() {
        return membershipRequestManager;
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
