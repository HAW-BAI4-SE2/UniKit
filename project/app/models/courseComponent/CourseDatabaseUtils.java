package models.courseComponent;

/**
 * @author Thomas Bednorz
 */

import assets.Global;
import models.commonUtils.UnikitDatabaseUtils;
import net.unikit.database.external.interfaces.Course;
import net.unikit.database.unikit_.interfaces.Team;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class CourseDatabaseUtils {

    /**
     * Changes the status of a course registration for a given student.
     * @param studentNumber the student of the student for which the status will be changed
     * @param courseID the course for which the status will be changed
     * @param status true if student is in a team for this course, else false
     */
    public static void changeTeamRegistrationStatus(String studentNumber, int courseID, boolean status){
        UnikitDatabaseUtils.changeTeamRegistrationStatus(studentNumber,courseID,status);
    }

    /**
     * Returns the course for the queried course ID. Delegates the call to the UnikitDatabaseUtils.
     * @param courseID the ID of the queried course
     * @return course the queried course
     */
    public static Course getCourseByID(int courseID) {
        return UnikitDatabaseUtils.getCourseByID(courseID);
    }

    /**
     * Returns all teams for the given course that are not full
     * @param courseID the ID of the course
     * @return List of all available teams
     */
    public static List<Team> getAvailableTeamsForCourse(int courseID) {
        List<Team> availableTeams = new ArrayList<>();
        List<Team> allTeams = Global.getTeamManager().getAllTeams();

        //Gets the course associated with the courseID
        Course associatedCourse = null;
        associatedCourse = UnikitDatabaseUtils.getCourseByID(courseID);
        checkNotNull(associatedCourse);

        //Gets all teams with free slots associated with the course
        for(Team currentTeam : allTeams){
            if(currentTeam.getCourseId() == associatedCourse.getId() && currentTeam.getTeamRegistrations().size() < associatedCourse.getMaxTeamSize()){
               availableTeams.add(currentTeam);
           }
        }

        return availableTeams;
    }
}
