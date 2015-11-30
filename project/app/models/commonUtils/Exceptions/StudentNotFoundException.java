package models.commonUtils.Exceptions;

import models.commonUtils.ID.StudentNumber;

/**
 * @author Thomas Bednorz
 */
public class StudentNotFoundException extends Exception {
    private final StudentNumber studentNumber;

    public StudentNotFoundException(StudentNumber sNumber) {
        super("Student not found");
        this.studentNumber = sNumber;
    }

    public StudentNumber getStudentNumber(){
        return studentNumber;
    }
}
