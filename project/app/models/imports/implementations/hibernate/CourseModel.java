package models.imports.implementations.hibernate;

public class CourseModel {
    private final int id;
    private final String name;
    private final int minTeamSize;
    private final int maxTeamSize;

    public CourseModel(int id, String name, int minTeamSize, int maxTeamSize) {
        this.id = id;
        this.name = name;
        this.minTeamSize = minTeamSize;
        this.maxTeamSize = maxTeamSize;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMinTeamSize() {
        return minTeamSize;
    }

    public int getMaxTeamSize() {
        return maxTeamSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CourseModel)) return false;

        CourseModel that = (CourseModel) o;

        if (getId() != that.getId()) return false;
        if (getMinTeamSize() != that.getMinTeamSize()) return false;
        if (getMaxTeamSize() != that.getMaxTeamSize()) return false;
        return !(getName() != null ? !getName().equals(that.getName()) : that.getName() != null);

    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + getMinTeamSize();
        result = 31 * result + getMaxTeamSize();
        return result;
    }

    @Override
    public String toString() {
        return "CourseModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", minTeamSize=" + minTeamSize +
                ", maxTeamSize=" + maxTeamSize +
                '}';
    }
}
