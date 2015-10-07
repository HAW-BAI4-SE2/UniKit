public class CompletedCourseModel {
    private String studentNumber;
    private int courseId;

    public CompletedCourseModel() {}

    public CompletedCourseModel(String studentNumber, int courseId) {
        this.studentNumber = studentNumber;
        this.courseId = courseId;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompletedCourseModel)) return false;

        CompletedCourseModel that = (CompletedCourseModel) o;

        if (getCourseId() != that.getCourseId()) return false;
        return !(getStudentNumber() != null ? !getStudentNumber().equals(that.getStudentNumber()) : that.getStudentNumber() != null);

    }

    @Override
    public int hashCode() {
        int result = getStudentNumber() != null ? getStudentNumber().hashCode() : 0;
        result = 31 * result + getCourseId();
        return result;
    }

    @Override
    public String toString() {
        return "CompletedCourseModel{" +
                "studentNumber='" + studentNumber + '\'' +
                ", courseId=" + courseId +
                '}';
    }
}
