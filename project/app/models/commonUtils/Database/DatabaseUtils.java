package models.commonUtils.Database;

import models.commonUtils.Exceptions.*;
import models.commonUtils.ID.CourseID;
import models.commonUtils.ID.StudentNumber;
import models.commonUtils.ID.TeamID;
import net.unikit.database.interfaces.entities.Course;
import net.unikit.database.interfaces.entities.Student;
import net.unikit.database.interfaces.entities.Team;

import java.util.List;

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
    public static Team getTeam(TeamID tID) throws TeamNotFoundException {
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
    public static Team getTeam(StudentNumber sNumber, CourseID cID) throws StudentNotFoundException, TeamNotFoundException, CourseNotFoundException {
        Student student = StudentDatabaseUtils.getStudent(sNumber);
        Course course = CourseDatabaseUtils.getCourse(cID);
        return TeamDatabaseUtils.getTeam(student,course);
    }

    public static Course getCourse(CourseID cID) throws CourseNotFoundException {
        return CourseDatabaseUtils.getCourse(cID);
    }

    public static List<Course> getRegisteredCourses(StudentNumber sNumber) throws StudentNotFoundException {
        Student student = StudentDatabaseUtils.getStudent(sNumber);
        return CourseDatabaseUtils.getRegisteredCourses(student);
    }

    public static List<Course> getAvailableCourses(StudentNumber sNumber) throws StudentNotFoundException {
        Student student = StudentDatabaseUtils.getStudent(sNumber);
        return CourseDatabaseUtils.getAvailableCourses(student);
    }

    public static void storeCourseRegistration(StudentNumber sNumber, CourseID cID) throws StudentNotFoundException, CourseNotFoundException {
        Student student = StudentDatabaseUtils.getStudent(sNumber);
        Course course = CourseDatabaseUtils.getCourse(cID);
        CourseDatabaseUtils.storeCourseRegistration(student,course);
    }

    public static void deleteCourseRegistration(StudentNumber sNumber, CourseID cID) throws CourseNotFoundException, CourseRegistrationNotFoundException, StudentNotFoundException {
        Student student = StudentDatabaseUtils.getStudent(sNumber);
        Course course = CourseDatabaseUtils.getCourse(cID);
        CourseDatabaseUtils.deleteCourseRegistration(student,course);
    }

    public static TeamID createTeam(StudentNumber sNumber, CourseID cID) throws FatalErrorException, TeamExistsException, StudentInTeamException, CourseNotFoundException, StudentNotFoundException {
        Student student = StudentDatabaseUtils.getStudent(sNumber);
        Course course = CourseDatabaseUtils.getCourse(cID);
        return TeamDatabaseUtils.createTeam(student,course);
    }

    public static void deleteTeam(TeamID tID) throws CourseNotFoundException, FatalErrorException, TeamDeletedException, TeamNotFoundException {
        Team team = TeamDatabaseUtils.getTeam(tID);
        TeamDatabaseUtils.deleteTeam(team);
    }

    public static void addStudent(StudentNumber sNumber, TeamID tID) throws FatalErrorException, StudentInTeamException, CourseNotFoundException, StudentNotFoundException, TeamNotFoundException {
        Student student = StudentDatabaseUtils.getStudent(sNumber);
        Team team = TeamDatabaseUtils.getTeam(tID);
        TeamDatabaseUtils.addStudentToTeam(student,team);
    }

    public static void deleteInvitation(StudentNumber sNumber, TeamID tID) throws InvitationNotFoundException, TeamNotFoundException, StudentNotFoundException {
        Student student = StudentDatabaseUtils.getStudent(sNumber);
        Team team = TeamDatabaseUtils.getTeam(tID);
        InvitationDatabaseUtils.deleteInvitation(student,team);
    }

    public static void deleteMembershipRequest(StudentNumber sNumber, TeamID tID) throws MembershipRequestNotFoundException, TeamNotFoundException, StudentNotFoundException {
        Student student = StudentDatabaseUtils.getStudent(sNumber);
        Team team = TeamDatabaseUtils.getTeam(tID);
        MembershipRequestDatabaseUtils.deleteMembershipRequest(student,team);
    }

    public static boolean isStudentInvited(StudentNumber sNumber, TeamID tID) throws StudentNotFoundException, TeamNotFoundException {
        Student student = StudentDatabaseUtils.getStudent(sNumber);
        Team team = TeamDatabaseUtils.getTeam(tID);
        return InvitationDatabaseUtils.isStudentInvited(student,team);
    }

    public static void storeMembershipRequest(StudentNumber sNumber, TeamID tID) throws StudentNotFoundException, TeamNotFoundException, MembershipRequestExistsException {
        Student student = StudentDatabaseUtils.getStudent(sNumber);
        Team team = TeamDatabaseUtils.getTeam(tID);
        MembershipRequestDatabaseUtils.storeMembershipRequest(student,team);
    }

    public static void removeStudent(StudentNumber sNumber, TeamID tID) throws TeamNotFoundException, StudentNotFoundException {
        Student student = StudentDatabaseUtils.getStudent(sNumber);
        Team team = TeamDatabaseUtils.getTeam(tID);
        TeamDatabaseUtils.removeStudent(student,team);

    }

    public static void storeInvitation(StudentNumber sNumber, TeamID tID, StudentNumber invitedBy) throws InvitationExistsException, TeamNotFoundException, StudentNotFoundException {
        Student student = StudentDatabaseUtils.getStudent(sNumber);
        Student createdBy = StudentDatabaseUtils.getStudent(sNumber);
        Team team = TeamDatabaseUtils.getTeam(tID);
        InvitationDatabaseUtils.storeInvitation(student,team,createdBy);
    }

    public static List<Team> getAvailableTeams(CourseID cID) throws CourseNotFoundException {
        Course course = CourseDatabaseUtils.getCourse(cID);
        return TeamDatabaseUtils.getAvailableTeams(course);
    }

    public static List<Student> getAllMembershipRequests(TeamID tID) throws TeamNotFoundException {
        Team team = TeamDatabaseUtils.getTeam(tID);
        return MembershipRequestDatabaseUtils.getAllMembershipRequests(team);
    }

    public static List<Student> getAllStudents(TeamID tID) throws TeamNotFoundException {
        Team team = TeamDatabaseUtils.getTeam(tID);
        return StudentDatabaseUtils.getAllStudents(team);
    }

    public static List<Student> getAllInvites(TeamID tID) throws TeamNotFoundException {
        Team team = TeamDatabaseUtils.getTeam(tID);
        return InvitationDatabaseUtils.getAllInvites(team);
    }

    public static List<Student> getAllStudents(CourseID cID) throws CourseNotFoundException {
        Course course = CourseDatabaseUtils.getCourse(cID);
        return StudentDatabaseUtils.getAllStudents(course);
    }

    public static boolean isStudentMember(StudentNumber deletedBy, TeamID tID) throws StudentNotFoundException, TeamNotFoundException {
        Student student = StudentDatabaseUtils.getStudent(deletedBy);
        Team team = TeamDatabaseUtils.getTeam(tID);
        if(TeamDatabaseUtils.isStudentMember(team,student)){
            return true;
        }

        return false;
    }
}
