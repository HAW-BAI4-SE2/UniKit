public class TeamModel {
    private final int id;
    private final int courseId;

    public TeamModel(int id, int courseId) {
        this.id = id;
        this.courseId = courseId;
    }

    public int getId() {
        return id;
    }

    public int getCourseId() {
        return courseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TeamModel)) return false;

        TeamModel teamModel = (TeamModel) o;

        if (getId() != teamModel.getId()) return false;
        return getCourseId() == teamModel.getCourseId();

    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getCourseId();
        return result;
    }

    @Override
    public String toString() {
        return "TeamModel{" +
                "id=" + id +
                ", courseId=" + courseId +
                '}';
    }
}
