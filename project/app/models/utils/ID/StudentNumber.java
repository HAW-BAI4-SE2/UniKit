package models.utils.ID;

/**
 * @author Thomas Bednorz
 */
public class StudentNumber {
    private String studentNumber;

    public StudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String value(){
        return studentNumber;
    }
}
