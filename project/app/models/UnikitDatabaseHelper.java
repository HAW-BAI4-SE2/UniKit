package models;

/**
 * @author Thomas Bednorz
 */

import assets.Global;
import controllers.courseComponent.CourseRegistrationController;
import net.unikit.database.external.interfaces.Course;
import net.unikit.database.unikit_.interfaces.Team;
import net.unikit.database.unikit_.interfaces.TeamRegistration;

import javax.swing.text.TabableView;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class UnikitDatabaseHelper {

    /**
     * Returns the team for the student number and the course ID
     * @param studentNumber the student number for the team
     * @param courseID the course ID for the team
     * @return team the team for the student number and course ID
     */
    public static Team getTeam(String studentNumber, int courseID){
        checkNotNull(studentNumber);
        Team teamByStudentNumberAndCourseID = null;

        List<TeamRegistration> allTeamRegistrations = Global.getTeamRegistrationManager().getAllTeamRegistrations();
        List<TeamRegistration> allTeamRegistrationsForStudent = null;

        for(TeamRegistration currentTeamRegistration : allTeamRegistrations){
            if(currentTeamRegistration.getStudentNumber().equals(studentNumber) && currentTeamRegistration.getTeam().getCourseId() == courseID){
                teamByStudentNumberAndCourseID = currentTeamRegistration.getTeam();
            }
        }

        checkNotNull(teamByStudentNumberAndCourseID);
        return teamByStudentNumberAndCourseID;
    }

    /**
     * Return a Course-object found by the ID
     * @param courseId the course ID for which the course is queried
     * @return course the course for the given ID
     */
    public static Course getCourseByID(int courseId) {
        Course course = null;

        List<Course> allCourses = Global.getCourseManager().getAllCourses();

        for(Course currentCourse : allCourses){
            if(currentCourse.getId() == courseId){
                course = currentCourse;
            }
        }

        checkNotNull(course);
        return course;
    }
}
