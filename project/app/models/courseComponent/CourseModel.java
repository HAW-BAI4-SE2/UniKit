package models.courseComponent;

import models.commonUtils.Database.DatabaseUtils;
import models.commonUtils.Exceptions.CourseNotFoundException;
import models.commonUtils.Exceptions.CourseRegistrationNotFoundException;
import models.commonUtils.Exceptions.StudentNotFoundException;
import models.commonUtils.Exceptions.TeamNotFoundException;
import models.commonUtils.ID.CourseID;
import models.commonUtils.ID.StudentNumber;
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

    public static List<Course> getRegisteredCourses(StudentNumber sNumber) throws StudentNotFoundException {
        return DatabaseUtils.getRegisteredCourses(sNumber);
    }

    public static List<Course> getAvailableCourses(StudentNumber sNumber) throws StudentNotFoundException {
        return DatabaseUtils.getAvailableCourses(sNumber);
    }

    public static void storeCourseRegistrations(StudentNumber sNumber, List<String> courseIDs) throws CourseNotFoundException, StudentNotFoundException {
        for(String courseID : courseIDs){
            DatabaseUtils.storeCourseRegistration(sNumber,CourseID.get(Integer.parseInt(courseID)));
        }
    }

    public static void cancelCourseRegistrations(StudentNumber sNumber, List<String> courseIDs) throws CourseNotFoundException, CourseRegistrationNotFoundException, StudentNotFoundException {
        for(String courseID : courseIDs){
            DatabaseUtils.deleteCourseRegistration(sNumber,CourseID.get(Integer.parseInt(courseID)));
        }
    }
}
