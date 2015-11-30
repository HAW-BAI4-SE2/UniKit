package models.courseComponent;

import models.commonUtils.CommonDatabaseUtils;
import models.commonUtils.Exceptions.CourseNotFoundException;
import models.commonUtils.Exceptions.StudentNotFoundException;
import models.commonUtils.Exceptions.TeamNotFoundException;
import models.commonUtils.ID.CourseID;
import models.commonUtils.ID.StudentNumber;
import net.unikit.database.interfaces.entities.Course;
import net.unikit.database.interfaces.entities.MembershipRequest;
import net.unikit.database.interfaces.entities.Team;
import net.unikit.database.interfaces.entities.TeamInvitation;
import play.api.i18n.Messages;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tbu on 11/30/2015.
 */
public class CourseModel {
    public static Course getCourseByID(CourseID cID) throws CourseNotFoundException {
        return CommonDatabaseUtils.getCourseByID(cID);
    }

    public static Team getTeam(StudentNumber sNumber, CourseID cID) throws TeamNotFoundException {
        return CommonDatabaseUtils.getTeamByStudentAndCourse(sNumber,cID);
    }

    public static List<MembershipRequest> getMembershipRequests(StudentNumber sNumber, CourseID cID) throws CourseNotFoundException, StudentNotFoundException {
        return CommonDatabaseUtils.getAllMembershipRequests(sNumber, cID);
    }

    public static List<TeamInvitation> getInvitations(StudentNumber sNumber, CourseID cID) throws CourseNotFoundException, StudentNotFoundException {
        return CommonDatabaseUtils.getAllInvitations(sNumber, cID);
    }

    public static List<Course> getRegisteredCourses(StudentNumber sNumber) throws StudentNotFoundException {
        return CommonDatabaseUtils.getRegisteredCourses(sNumber);
    }

    public static List<Course> getAvailableCourses(StudentNumber sNumber) throws StudentNotFoundException {
        return CommonDatabaseUtils.getAvailableCourses(sNumber);
    }

    public static void storeCourseRegistrations(StudentNumber sNumber, List<String> courseIDs) throws CourseNotFoundException, StudentNotFoundException {
        for(String courseID : courseIDs){
            CommonDatabaseUtils.storeCourseRegistration(sNumber,CourseID.get(Integer.parseInt(courseID)));
        }
    }

    public static void cancelCourseRegistrations(StudentNumber sNumber, List<String> courseIDs) throws CourseNotFoundException, StudentNotFoundException {
        for(String courseID : courseIDs){
            CommonDatabaseUtils.deleteCourseRegistration(sNumber,CourseID.get(Integer.parseInt(courseID)));
        }
    }
}
