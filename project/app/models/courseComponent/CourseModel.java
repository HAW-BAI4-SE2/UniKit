package models.courseComponent;

import models.commonUtils.Database.DatabaseUtils;
import models.commonUtils.Exceptions.CourseNotFoundException;
import models.commonUtils.Exceptions.CourseRegistrationNotFoundException;
import models.commonUtils.Exceptions.StudentNotFoundException;
import models.commonUtils.Exceptions.TeamNotFoundException;
import models.commonUtils.ID.CourseID;
import models.commonUtils.ID.StudentNumber;
import models.commonUtils.ID.TeamID;
import models.studentComponent.TeamModel;
import net.unikit.database.interfaces.entities.Course;
import net.unikit.database.interfaces.entities.MembershipRequest;
import net.unikit.database.interfaces.entities.Team;
import net.unikit.database.interfaces.entities.TeamInvitation;

import java.util.List;

/**
 * Receives calls by the controllers and delegates to the respective models
 * @author Thomas Bednorz
 */
public class CourseModel {
    public static Course getCourse(CourseID cID) throws CourseNotFoundException {
        return DatabaseUtils.getCourse(cID);
    }

    /**
     *
     * @param sNumber
     * @return
     * @throws StudentNotFoundException
     */
    public static List<Course> getRegisteredCourses(StudentNumber sNumber) throws StudentNotFoundException {
        return DatabaseUtils.getRegisteredCourses(sNumber);
    }

    /**
     *
     * @param sNumber
     * @return
     * @throws StudentNotFoundException
     */
    public static List<Course> getAvailableCourses(StudentNumber sNumber) throws StudentNotFoundException {
        return DatabaseUtils.getAvailableCourses(sNumber);
    }

    /**
     *
     * @param sNumber
     * @param courseIDs
     * @throws CourseNotFoundException
     * @throws StudentNotFoundException
     */
    public static void storeCourseRegistrations(StudentNumber sNumber, List<String> courseIDs) throws CourseNotFoundException, StudentNotFoundException {
        for(String courseID : courseIDs){
            DatabaseUtils.storeCourseRegistration(sNumber,CourseID.get(Integer.parseInt(courseID)));
        }
    }

    /**
     *
     * @param sNumber
     * @param courseIDs
     * @throws CourseNotFoundException
     * @throws CourseRegistrationNotFoundException
     * @throws StudentNotFoundException
     */
    public static void cancelCourseRegistrations(StudentNumber sNumber, List<String> courseIDs) throws CourseNotFoundException, CourseRegistrationNotFoundException, StudentNotFoundException {
        for(String courseID : courseIDs){
            DatabaseUtils.deleteCourseRegistration(sNumber,CourseID.get(Integer.parseInt(courseID)));
            try {
                // Remove student from team if he's in one
                Team team = DatabaseUtils.getTeam(sNumber,CourseID.get(Integer.parseInt(courseID)));
                TeamID teamID = TeamID.get(team.getId());
                DatabaseUtils.removeStudent(sNumber,teamID);
            } catch (TeamNotFoundException e) {
                // Student wasn't in a team, do nothing
                e.printStackTrace();
            }
        }
    }
}
