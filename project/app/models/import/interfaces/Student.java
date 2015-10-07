import java.util.Collection;

public interface Student {
    public String getStudentNumber();
    public String getFirstName();
    public String getLastName();
    public String getEmail();
    public int getSemester();
    public Collection<Course> getAvailableCourses();
}
