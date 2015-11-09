package models;

/**
 * @author Thomas Bednorz
 */

import assets.Global;
import controllers.courseComponent.CourseRegistrationController;
import net.unikit.database.external.interfaces.Course;
import net.unikit.database.unikit_.interfaces.*;

import javax.swing.text.TabableView;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class UnikitDatabaseUtils {

    /**
     * Returns a Course-object associated with the courseID
     * @param courseId the course ID for which the course is queried
     * @return the Course-object for the given ID
     */
    public static Course getCourseByID(int courseId) {
        Course course = null;

        List<Course> allCourses = Global.getCourseManager().getAllCourses();

        for(Course currentCourse : allCourses){
            if(currentCourse.getId() == courseId){
                course = currentCourse;
            }
        }

        checkNotNull(course);
        return course;
    }

    /**
     * Returns the Team-object associated with the teamID
     * @param teamID the ID of the queried team
     * @return the Team-object for the given ID
     */
    public static Team getTeamByID(int teamID) {
        Team queriedTeam = null;
        List<Team> allTeams = Global.getTeamManager().getAllTeams();

        for(Team currentTeam : allTeams){
            if(currentTeam.getId() == teamID){
                queriedTeam = currentTeam;
            }
        }
        checkNotNull(queriedTeam);
        return queriedTeam;
    }

    /**
     * Returns the team for the student number and the course ID
     * @param studentNumber the student number for the team
     * @param courseID the course ID for the team
     * @return the Team-object for the student number and course ID
     */
    public static Team getTeamByStudentAndCourse(String studentNumber, int courseID){
        checkNotNull(studentNumber);
        Team teamByStudentNumberAndCourseID = null;

        List<TeamRegistration> allTeamRegistrations = Global.getTeamRegistrationManager().getAllTeamRegistrations();
        List<TeamRegistration> allTeamRegistrationsForStudent = null;

        for(TeamRegistration currentTeamRegistration : allTeamRegistrations){
            if(currentTeamRegistration.getStudentNumber().equals(studentNumber) && currentTeamRegistration.getTeam().getCourseId() == courseID){
                teamByStudentNumberAndCourseID = currentTeamRegistration.getTeam();
            }
        }

        checkNotNull(teamByStudentNumberAndCourseID);
        return teamByStudentNumberAndCourseID;
    }

    public static void deleteInvitation(String studentNumber, int teamID){
        TeamInvitationManager invitationManager = Global.getTeamInvitationManager();
        List<TeamInvitation> allTeamInvitations = invitationManager.getAllTeamInvitations();
        TeamInvitation invitationToBeDeleted = null;

        for(TeamInvitation currentInvitation : allTeamInvitations){
            if(currentInvitation.getId() == teamID && currentInvitation.getInviteeStudentNumber().equals(studentNumber)){
               invitationToBeDeleted = currentInvitation;
            }
        }

        checkNotNull(invitationToBeDeleted);

        invitationManager.deleteTeamInvitation(invitationToBeDeleted);
    }

    public static void addStudentToTeam(String studentNumber, int teamID) {
        TeamRegistrationManager registrationManager = Global.getTeamRegistrationManager();
        TeamRegistration newTeamRegistration = registrationManager.createTeamRegistration();
        Team teamByID = UnikitDatabaseUtils.getTeamByID(teamID);

        newTeamRegistration.setStudentNumber(studentNumber);
        newTeamRegistration.setTeam(teamByID);

        checkNotNull(newTeamRegistration);

        registrationManager.addTeamRegistration(newTeamRegistration);
    }

    public static void removeStudentFromTeam(String studentNumber, int teamID) {
        TeamRegistrationManager registrationManager = Global.getTeamRegistrationManager();
        TeamRegistration newTeamRegistration = registrationManager.createTeamRegistration();
        Team teamByID = UnikitDatabaseUtils.getTeamByID(teamID);

        newTeamRegistration.setStudentNumber(studentNumber);
        newTeamRegistration.setTeam(teamByID);

        checkNotNull(newTeamRegistration);

        registrationManager.deleteTeamRegistration(newTeamRegistration);
    }

    public static void changeTeamRegistrationStatus(String studentNumber, int courseID, boolean status){
        checkNotNull(studentNumber);

        CourseRegistrationManager courseRegistrationManager = Global.getCourseRegistrationManager();
        List<CourseRegistration> allCourseRegistrations = courseRegistrationManager.getAllCourseRegistrations();

        CourseRegistration courseRegistrationToBUpdated = null;

        //Finds the registration for the student and the course ID
        for(CourseRegistration currentCourseRegistration : allCourseRegistrations){
            if(currentCourseRegistration.getCourseId() == courseID && currentCourseRegistration.getStudentNumber().equals(studentNumber)){
                courseRegistrationToBUpdated = currentCourseRegistration;
                courseRegistrationToBUpdated.setCurrentlyAssignedToTeam(status);
            }
        }

        courseRegistrationManager.updateCourseRegistration(courseRegistrationToBUpdated);
    }
}
