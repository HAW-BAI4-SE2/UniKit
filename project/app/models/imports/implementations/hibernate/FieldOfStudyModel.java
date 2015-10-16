package models.imports.implementations.hibernate;

public class FieldOfStudyModel {
    private final int id;
    private final String name;

    public FieldOfStudyModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FieldOfStudyModel)) return false;

        FieldOfStudyModel that = (FieldOfStudyModel) o;

        if (getId() != that.getId()) return false;
        return !(getName() != null ? !getName().equals(that.getName()) : that.getName() != null);

    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FieldOfStudyModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
