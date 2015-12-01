package models.commonUtils.Exceptions;

import models.commonUtils.ID.CourseID;
import models.commonUtils.ID.StudentNumber;
import models.commonUtils.ID.TeamID;

/**
 * This exception gets thrown when a membership request could not be found in the database.
 * It contains the TeamID of the associated team and the student number of the requesting student
 * @author Thomas Bednorz
 */
public class CourseRegistrationNotFoundException extends Exception{
    private final StudentNumber studentNumber;
    private final CourseID courseID;

    public CourseRegistrationNotFoundException(StudentNumber studentNumber, CourseID courseID){
        super("Course registration not found");
        this.studentNumber = studentNumber;
        this.courseID = courseID;
    }

    public StudentNumber getStudentNumber() {
        return studentNumber;
    }

    public CourseID getCourseID() {
        return courseID;
    }
}
