package models.commonUtils.ID;

/**
 * @author Thomas Bednorz
 */
public class StudentNumber {
    private String studentNumber;

    public static StudentNumber get(String studentNumber) throws IllegalArgumentException{
        return new StudentNumber(studentNumber);
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
