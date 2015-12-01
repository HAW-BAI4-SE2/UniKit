package models.commonUtils.Exceptions;

import models.commonUtils.ID.StudentNumber;

/**
 * Created by tbu on 11/30/2015.
 */
public class StudentInTeamException extends Exception {
    private final StudentNumber studentNumber;

    public StudentInTeamException(StudentNumber sNumber) {
        super("Student member of a team");
        this.studentNumber = sNumber;
    }

    public StudentNumber getStudentNumber(){
        return studentNumber;
    }
}
