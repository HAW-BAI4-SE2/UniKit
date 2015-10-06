public class GroupModel {
    private final int id;
    private final int groupNumber;
    private final int courseId;
    private final int maxSize;

    public GroupModel(int id, int groupNumber, int courseId, int maxSize) {
        this.id = id;
        this.groupNumber = groupNumber;
        this.courseId = courseId;
        this.maxSize = maxSize;
    }

    public int getId() {
        return id;
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public int getCourseId() {
        return courseId;
    }

    public int getMaxSize() {
        return maxSize;
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
