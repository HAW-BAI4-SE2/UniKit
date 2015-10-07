import java.util.Collection;

public interface Group {
    public int getGroupNumber();
    public Course getCourse();
    public int getMaxSize();
    public Collection<Appointment> getAppointments();
}
