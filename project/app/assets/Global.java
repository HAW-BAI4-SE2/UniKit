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
    private static CourseGroupAppointmentManager courseGroupAppointmentManager;
    private static CourseGroupManager courseGroupManager;
    private static CourseLectureAppointmentManager courseLectureAppointmentManager;
    private static CourseLectureManager courseLectureManager;
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

        // Create external database configuration
        InputStream inputStreamExternal = Play.application().resourceAsStream("database_external.properties");
        DatabaseConfiguration databaseConfigurationExternal = null;
        try {
            databaseConfigurationExternal = createDatabaseConfigurationFromProperties(inputStreamExternal);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create internal database configuration
        InputStream inputStreamInternal = Play.application().resourceAsStream("database_internal.properties");
        DatabaseConfiguration databaseConfigurationInternal = null;
        try {
            databaseConfigurationInternal = createDatabaseConfigurationFromProperties(inputStreamInternal);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Load database
        Logger.info("Initializing database...");
        DatabaseManager DatabaseManager = DatabaseManagerFactory.createDatabaseManager(databaseConfigurationInternal,  databaseConfigurationExternal);

        // Store database managers in global values
        Logger.info("Initializing database managers...");
        Global.courseGroupAppointmentManager = DatabaseManagerFactory.getDatabaseManager().getCourseGroupAppointmentManager();
        Global.courseGroupManager = DatabaseManagerFactory.getDatabaseManager().getCourseGroupManager();
        Global.courseLectureAppointmentManager = DatabaseManagerFactory.getDatabaseManager().getCourseLectureAppointmentManager();
        Global.courseLectureManager = DatabaseManagerFactory.getDatabaseManager().getCourseLectureManager();
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

    public static CourseGroupAppointmentManager getCourseGroupAppointmentManager() {
        return courseGroupAppointmentManager;
    }

    public static CourseGroupManager getCourseGroupManager() {
        return courseGroupManager;
    }

    public static CourseLectureAppointmentManager getCourseLectureAppointmentManager() {
        return courseLectureAppointmentManager;
    }

    public static CourseLectureManager getCourseLectureManager() {
        return courseLectureManager;
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
