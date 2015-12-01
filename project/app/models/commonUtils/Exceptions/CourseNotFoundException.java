package models.commonUtils.Exceptions;

import models.commonUtils.ID.CourseID;

/**
 * This exception gets thrown when a course couldn't be found in the database.
 * It contains a generic error message an die CourseID of the course
 * @author Thomas Bednorz
 */
public class CourseNotFoundException extends Throwable {
    private final CourseID courseID;

    public CourseNotFoundException(CourseID cID) {
        super("Course not found");
        this.courseID = cID;
    }

    public CourseNotFoundException() {
        this(null);
    }

    public CourseID getCourseID() {
        return courseID;
    }
}
