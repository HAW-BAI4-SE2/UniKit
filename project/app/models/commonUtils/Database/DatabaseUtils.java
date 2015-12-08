package models.commonUtils.Database;

import models.commonUtils.Exceptions.CourseNotFoundException;
import models.commonUtils.Exceptions.StudentNotFoundException;
import models.commonUtils.Exceptions.TeamNotFoundException;
import models.commonUtils.ID.CourseID;
import models.commonUtils.ID.StudentNumber;
import models.commonUtils.ID.TeamID;
import net.unikit.database.interfaces.entities.Course;
import net.unikit.database.interfaces.entities.Student;
import net.unikit.database.interfaces.entities.Team;

/**
 * Functions as the public interface for the database utilities. Delegates the received calls to the respective utility
 * @author Thomas Bednorz
 */
public class DatabaseUtils {

    /**
     *
     * @param tID
     * @return
     * @throws TeamNotFoundException
     */
    public Team getTeam(TeamID tID) throws TeamNotFoundException {
        return TeamDatabaseUtils.getTeam(tID);
    }

    /**
     *
     * @param sNumber
     * @param cID
     * @return
     * @throws StudentNotFoundException
     * @throws TeamNotFoundException
     * @throws CourseNotFoundException
     */
    public Team getTeam(StudentNumber sNumber, CourseID cID) throws StudentNotFoundException, TeamNotFoundException, CourseNotFoundException {
        Student student = StudentDatabaseUtils.getStudent(sNumber);
        Course course = CourseDatabaseUtils.getCourse(cID);
        return TeamDatabaseUtils.getTeam(student,course);
    }
}
