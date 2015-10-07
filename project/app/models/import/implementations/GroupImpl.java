import java.util.Collection;

public class GroupImpl implements Group {
    @Override
    public int getGroupNumber() {
        return 0;
    }

    @Override
    public Course getCourse() {
        return null;
    }

    @Override
    public int getMaxSize() {
        return 0;
    }

    @Override
    public Collection<Appointment> getAppointments() {
        return null;
    }
}
