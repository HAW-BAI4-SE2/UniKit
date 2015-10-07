import java.util.Date;

public class AppointmentModel {
    private final int gruopId;
    private final Date start;
    private final Date end;

    public AppointmentModel(int gruopId, Date start, Date end) {
        this.gruopId = gruopId;
        this.start = start;
        this.end = end;
    }

    public int getGruopId() {
        return gruopId;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppointmentModel)) return false;

        AppointmentModel that = (AppointmentModel) o;

        if (getGruopId() != that.getGruopId()) return false;
        if (getStart() != null ? !getStart().equals(that.getStart()) : that.getStart() != null) return false;
        return !(getEnd() != null ? !getEnd().equals(that.getEnd()) : that.getEnd() != null);

    }

    @Override
    public int hashCode() {
        int result = getGruopId();
        result = 31 * result + (getStart() != null ? getStart().hashCode() : 0);
        result = 31 * result + (getEnd() != null ? getEnd().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AppointmentModel{" +
                "gruopId=" + gruopId +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
