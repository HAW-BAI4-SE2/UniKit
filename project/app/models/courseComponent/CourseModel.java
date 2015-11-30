package models.courseComponent;

import models.commonUtils.CommonDatabaseUtils;
import models.commonUtils.Exceptions.CourseNotFoundException;
import models.commonUtils.ID.CourseID;
import net.unikit.database.interfaces.entities.Course;

/**
 * Created by tbu on 11/30/2015.
 */
public class CourseModel {
    public static Course getCourseByID(CourseID cID) throws CourseNotFoundException {
        return CommonDatabaseUtils.getCourseByID(cID);
    }
}
