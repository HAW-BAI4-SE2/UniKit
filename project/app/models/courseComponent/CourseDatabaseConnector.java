package models.courseComponent;

/**
 * @author Thomas Bednorz
 */

import assets.Global;
import models.UnikitDatabaseHelper;
import net.unikit.database.external.interfaces.Course;
import net.unikit.database.unikit_.interfaces.CourseRegistration;
import net.unikit.database.unikit_.interfaces.Team;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class CourseDatabaseConnector {

    /**
     * Changes the status of a course registration for a given student.
     * @param studentNumber the student of the student for which the status will be changed
     * @param courseID the course for which the status will be changed
     * @param status true if student is in a team for this course, else false
     */
    public static void changeTeamRegistrationStatus(String studentNumber, int courseID, boolean status){
        checkNotNull(studentNumber);
        List<CourseRegistration> allCourseRegistrations = Global.getCourseRegistrationManager().getAllCourseRegistrations();
        CourseRegistration courseRegistrationToBUpdated = null;

        for(CourseRegistration currentCourseRegistration : allCourseRegistrations){
            if(currentCourseRegistration.getCourseId() == courseID && currentCourseRegistration.getStudentNumber().equals(studentNumber)){
                courseRegistrationToBUpdated = currentCourseRegistration;
                courseRegistrationToBUpdated.setCurrentlyAssignedToTeam(status);
            }
        }

        Global.getCourseRegistrationManager().updateCourseRegistration(courseRegistrationToBUpdated);
    }

    /**
     * Returns the course for the queried course ID. Delegates the call to the UnikitDatabaseHelper.
     * @param courseID the ID of the queried course
     * @return course the queried course
     */
    public static Course getCourseByID(int courseID) {
        return UnikitDatabaseHelper.getCourseByID(courseID);
    }

    public static List<Team> getAvailableTeamsForCourse(int courseID) {
        List<Team> availableTeams = new ArrayList<>();
        List<Team> allTeams = Global.getTeamManager().getAllTeams();

        //TODO: Implement list of available teams. Need: max size for team

        return availableTeams;
    }
}
