package models.commonUtils.ID;

import assets.Global;
import net.unikit.database.interfaces.entities.Course;
import net.unikit.database.interfaces.entities.Student;
import net.unikit.database.interfaces.entities.Team;

/**
 * This utility converts between the ID types used in Unikit and the ID types used in the database library
 * @author Thomas Bednorz
 */
public class IDUtils {

    public static Student.StudentNumber getStudentNumber(StudentNumber unikitStudentNumber){
        return Global.getStudentManager().createID(unikitStudentNumber.value());
    }

    public static Student.StudentNumber getStudentNumber(String studentNumber) {
        return Global.getStudentManager().createID(studentNumber);
    }

    public static Team.ID getTeamID(TeamID unikitTeanID){
        return Global.getTeamManager().createID(unikitTeanID.value());
    }

    public static StudentNumber getUnikitStudentNumber(Student.StudentNumber dbStudentNumber){
        return StudentNumber.get(dbStudentNumber.getValue());
    }

    public static TeamID getUnikitTeamID(Team.ID dbTeamID){
        return TeamID.get(dbTeamID.getValue());
    }

    public static CourseID getUnikitCourseID(Course course){
        return CourseID.get(course.getId().getValue());
    }

    public static int getInt(Course course) {
        return course.getId().getValue();
    }
}
