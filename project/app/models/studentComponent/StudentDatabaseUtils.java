package models.studentComponent;

/**
 * @author Thomas Bednorz
 */

import assets.Global;

import controllers.courseComponent.CourseRegistrationController;

import models.UnikitDatabaseUtils;

import models.courseComponent.CourseDatabaseUtils;

import net.unikit.database.unikit_.interfaces.Team;
import net.unikit.database.unikit_.interfaces.TeamManager;
import net.unikit.database.unikit_.interfaces.TeamRegistration;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;


public class StudentDatabaseUtils {

    /**
     *  Creates a team associated with the studentNumber and courseID,updates the registration status and returns the teamID
     *  @param studentNumber the first member of the team
     *  @param courseID the course for which the team will be created
     *  @return returns the ID for the new team
     */
    public static int createTeam(String studentNumber, int courseID){
        TeamManager teamManager = Global.getTeamManager();

        //Creates a new team and adds it to the database
        Team newTeam = teamManager.createTeam();
        newTeam.setCourseId(courseID);
        newTeam.setCreatedByStudentNumber(studentNumber);
        teamManager.addTeam(newTeam);

        // Register current user in team
        TeamRegistration teamRegistration = Global.getTeamRegistrationManager().createTeamRegistration();
        teamRegistration.setStudentNumber(studentNumber);
        teamRegistration.setTeam(newTeam);
        Global.getTeamRegistrationManager().addTeamRegistration(teamRegistration);

        //Gets the team that was just created
        Team createdTeam = null;
        createdTeam = UnikitDatabaseUtils.getTeamByStudentAndCourse(studentNumber,courseID);
        checkNotNull(createdTeam);

        //Updates registration status for student
        CourseDatabaseUtils.changeTeamRegistrationStatus(studentNumber, courseID, true);

        return createdTeam.getId();
    }

    /**
    *  Deletes the team associated with the teamID and updates the flags for the students in the course registration
    *  @param teamID the team that is to be deleted
    *  @return courseForTeam the course for which the team was associated
    */
    public static int deleteTeam(int teamID){
        TeamManager teamManager = Global.getTeamManager();

        Team teamToBeDeleted = null;

        List<Team> allTeams = teamManager.getAllTeams();

        for(Team currentTeam : allTeams){
            if(currentTeam.getId() == teamID){
                teamToBeDeleted = currentTeam;
            }
        }

        //TODO: better logic if team doestn exist
        checkNotNull(teamToBeDeleted);
        int courseForTeamID = courseForTeamID = teamToBeDeleted.getCourseId();

        //Update status of students to single registration
        for(TeamRegistration currentRegistration : teamToBeDeleted.getTeamRegistrations()){
            CourseRegistrationController.changeTeamRegistrationStatus(
                    currentRegistration.getStudentNumber(), currentRegistration.getTeam().getCourseId(),false);
        }

        //Delete team
        teamManager.deleteTeam(teamToBeDeleted);

        return courseForTeamID;
    }

    /**
     * Accepts the invitation by adding the student to the team, deleting the invitation and updating the registration status
     * @param studentNumber the student number of the student who accepted the invitation and will be added to the team
     * @param teamID the ID of the team that the student will be added to
     */
    public static void acceptInvitation(String studentNumber, int teamID){
        UnikitDatabaseUtils.addStudentToTeam(studentNumber, teamID);
        UnikitDatabaseUtils.deleteInvitation(studentNumber, teamID);

        //Get course associated with the team
        int associatedCourse = UnikitDatabaseUtils.getTeamByID(teamID).getCourseId();

        UnikitDatabaseUtils.changeTeamRegistrationStatus(studentNumber, associatedCourse, true);
    }

    /**
     * Declines the invitation by deleting the invitation
     * @param studentNumber the student number of the student who accepted the invitation and will be added to the team
     * @param teamID the ID of the team that the student will be added to
     */
    public static void declineInvitation(String studentNumber, int teamID){
        UnikitDatabaseUtils.deleteInvitation(studentNumber,teamID);
    }

    /**
     *  The student requests the membership with the team
     *  @param studentNumber the student who wants to join the team
     *  @param teamID the ID for which the students requests membership
     *  @return showCourseDetails-page
     **/
     public static void requestMembership(String studentNumber, int teamID){
        // TODO: store membershiprequest from a student to database
     }

     /**
      *  The student cancels his membership request with the team
      *  @param studentNumber the student who wants to cancel his membership request
      *  @param teamID the ID of the team the students wants to cancel his request
      *  @return showCourseDetails-page
      **/
    public static void cancelMembershipRequest(String studentNumber, int teamID){
        // TODO: delete membership request from database
    }
}