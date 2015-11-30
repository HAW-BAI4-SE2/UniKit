package models.commonUtils.ID;

import net.unikit.database.interfaces.entities.Student;

/**
 * @author Thomas Bednorz
 */
public class StudentNumber {
    private String studentNumber;

    public static StudentNumber get(String studentNumber) throws IllegalArgumentException{
        return new StudentNumber(studentNumber);
    }

    public static StudentNumber get(Student.StudentNumber studentNumber) {
        return new StudentNumber(studentNumber.getValue());
    }

    private StudentNumber() {}

    private StudentNumber(String studentNumber) throws IllegalArgumentException{
        if (studentNumber == null || studentNumber.equals("")) {
            throw new IllegalArgumentException("Invalid student number");
        } else {
            this.studentNumber = studentNumber;
        }
    }

    public String value(){
        return studentNumber;
    }


}
