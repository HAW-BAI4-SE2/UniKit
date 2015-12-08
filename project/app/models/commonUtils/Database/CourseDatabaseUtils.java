package models.commonUtils.Database;

import assets.Global;
import models.commonUtils.Exceptions.CourseNotFoundException;
import models.commonUtils.ID.CourseID;
import net.unikit.database.exceptions.EntityNotFoundException;
import net.unikit.database.interfaces.entities.Course;
import net.unikit.database.interfaces.managers.CourseManager;

/**
 * Handles the database interaction related to courses.
 * @author Thomas Bednorz
 */
class CourseDatabaseUtils {
    public static Course getCourse(CourseID cID) throws CourseNotFoundException {
        CourseManager courseManager = Global.getCourseManager();

        Course course = null;
        try {
            course = courseManager.getEntity(courseManager.createID(cID.value()));
        } catch (EntityNotFoundException e) {
            throw new CourseNotFoundException(cID);
        }

        return course;
    }
}
