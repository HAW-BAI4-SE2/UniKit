public class AvailableCourseModel {
    private final String studentNumber;
    private final int courseId;

    public AvailableCourseModel(String studentNumber, int courseId) {
        this.studentNumber = studentNumber;
        this.courseId = courseId;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public int getCourseId() {
        return courseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AvailableCourseModel)) return false;

        AvailableCourseModel that = (AvailableCourseModel) o;

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
        return "AvailableCourseModel{" +
                "studentNumber='" + studentNumber + '\'' +
                ", courseId=" + courseId +
                '}';
    }
}
