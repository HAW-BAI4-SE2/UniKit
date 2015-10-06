import java.util.Date;

public class SpaceOfTimeModel {
    private int gruopId;
    private Date start;
    private Date end;

    public SpaceOfTimeModel() {}

    public SpaceOfTimeModel(int gruopId, Date start, Date end) {
        this.gruopId = gruopId;
        this.start = start;
        this.end = end;
    }

    public int getGruopId() {
        return gruopId;
    }

    public void setGruopId(int gruopId) {
        this.gruopId = gruopId;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpaceOfTimeModel)) return false;

        SpaceOfTimeModel that = (SpaceOfTimeModel) o;

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
        return "SpaceOfTimeModel{" +
                "gruopId=" + gruopId +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
