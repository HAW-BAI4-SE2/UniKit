package models.courseComponent;

/**
 * @author Thomas Bednorz
 */

import assets.Global;
import controllers.courseComponent.CourseRegistrationController;
import net.unikit.database.unikit_.interfaces.CourseRegistration;
import net.unikit.database.unikit_.interfaces.Team;
import net.unikit.database.unikit_.interfaces.TeamRegistration;

import java.util.List;

public class CourseDatabaseConnector {

    /**
     *
     * @param studentNumber the student of the student for which the status will be changed
     * @param courseID the course for which the status will be changed
     * @param status true if student is in a team for this course, else false
     */
    public static void changeTeamRegistrationStatus(String studentNumber, int courseID, boolean status){
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
}
