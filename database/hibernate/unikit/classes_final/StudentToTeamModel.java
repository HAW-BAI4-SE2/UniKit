public class StudentToTeamModel {
    private final String studentNumber;
    private final int teamId;

    public StudentToTeamModel(String studentNumber, int teamId) {
        this.studentNumber = studentNumber;
        this.teamId = teamId;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public int getTeamId() {
        return teamId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentToTeamModel)) return false;

        StudentToTeamModel that = (StudentToTeamModel) o;

        if (getTeamId() != that.getTeamId()) return false;
        return !(getStudentNumber() != null ? !getStudentNumber().equals(that.getStudentNumber()) : that.getStudentNumber() != null);

    }

    @Override
    public int hashCode() {
        int result = getStudentNumber() != null ? getStudentNumber().hashCode() : 0;
        result = 31 * result + getTeamId();
        return result;
    }

    @Override
    public String toString() {
        return "StudentToTeamModel{" +
                "studentNumber='" + studentNumber + '\'' +
                ", teamId=" + teamId +
                '}';
    }
}
