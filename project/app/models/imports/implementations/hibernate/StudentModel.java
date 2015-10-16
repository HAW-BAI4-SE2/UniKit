package models.imports.implementations.hibernate;

public class StudentModel {
    private final String studentNumber;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final int semester;
	private final int fieldOfStudyId;

    public StudentModel(String studentNumber, String firstName, String lastName, String email, int semester) {
        this.studentNumber = studentNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.semester = semester;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public int getSemester() {
        return semester;
    }
	
	public int getFieldOfStudyId() {
        return fieldOfStudyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentModel)) return false;

        StudentModel that = (StudentModel) o;

        if (getSemester() != that.getSemester()) return false;
		if (getFieldOfStudyId() != that.getFieldOfStudyId()) return false;
        if (getStudentNumber() != null ? !getStudentNumber().equals(that.getStudentNumber()) : that.getStudentNumber() != null)
            return false;
        if (getFirstName() != null ? !getFirstName().equals(that.getFirstName()) : that.getFirstName() != null)
            return false;
        if (getLastName() != null ? !getLastName().equals(that.getLastName()) : that.getLastName() != null)
            return false;
        return !(getEmail() != null ? !getEmail().equals(that.getEmail()) : that.getEmail() != null);

    }

    @Override
    public int hashCode() {
        int result = getStudentNumber() != null ? getStudentNumber().hashCode() : 0;
        result = 31 * result + (getFirstName() != null ? getFirstName().hashCode() : 0);
        result = 31 * result + (getLastName() != null ? getLastName().hashCode() : 0);
        result = 31 * result + (getEmail() != null ? getEmail().hashCode() : 0);
        result = 31 * result + getSemester();
		result = 31 * result + getFieldOfStudyId();
        return result;
    }

    @Override
    public String toString() {
        return "StudentModel{" +
                "studentNumber='" + studentNumber + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", semester=" + semester +
				", fieldOfStudyId=" + fieldOfStudyId +
                '}';
    }
}
