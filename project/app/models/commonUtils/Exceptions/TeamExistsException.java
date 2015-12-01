package models.commonUtils.Exceptions;

import models.commonUtils.ID.CourseID;
import models.commonUtils.ID.StudentNumber;

/**
 * Created by tbu on 12/1/2015.
 */
public class TeamExistsException extends Exception {
    private final StudentNumber studentNumber;
    private final CourseID courseID;

    public TeamExistsException(StudentNumber sNumber, CourseID cID){
        super("Team already exists");
        this.studentNumber = sNumber;
        this.courseID = cID;
    }

    public StudentNumber getStudentNumber() {
        return studentNumber;
    }

    public CourseID getCourseID() {
        return courseID;
    }
}
