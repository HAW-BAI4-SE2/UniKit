public class GroupModel {
    private int id;
    private int groupNumber;
    private int courseId;
    private int maxSize;

    public GroupModel() {}

    public GroupModel(int id, int groupNumber, int courseId, int maxSize) {
        this.id = id;
        this.groupNumber = groupNumber;
        this.courseId = courseId;
        this.maxSize = maxSize;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GroupModel)) return false;

        GroupModel that = (GroupModel) o;

        if (getId() != that.getId()) return false;
        if (getGroupNumber() != that.getGroupNumber()) return false;
        if (getCourseId() != that.getCourseId()) return false;
        return getMaxSize() == that.getMaxSize();

    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getGroupNumber();
        result = 31 * result + getCourseId();
        result = 31 * result + getMaxSize();
        return result;
    }

    @Override
    public String toString() {
        return "GroupModel{" +
                "id=" + id +
                ", groupNumber=" + groupNumber +
                ", courseId=" + courseId +
                ", maxSize=" + maxSize +
                '}';
    }
}
