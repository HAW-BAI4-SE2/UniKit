package models.utils.ID;

/**
 * @author Thomas Bednorz
 */
public  class IDFactory {
    private IDFactory(){};

    public static TeamID getTeamID(int teamID) throws IllegalArgumentException{
        if (teamID < 0) {
            throw new IllegalArgumentException();
        } else {
            return new TeamID(teamID);
        }
    }

    public static CourseID getCourseID(int courseID){
        if (courseID < 0) {
            throw new IllegalArgumentException();
        } else {
            return new CourseID(courseID);
        }
    }

    public static StudentNumber getStudentNumber(String studentNumber){
        if (studentNumber == null) {
            throw new IllegalArgumentException();
        } else {
            return new StudentNumber(studentNumber);
        }
    }
}
