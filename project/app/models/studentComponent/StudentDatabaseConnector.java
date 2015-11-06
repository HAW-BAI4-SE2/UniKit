package models.studentComponent;

/**
 * @author Thomas Bednorz
 */

import assets.Global;

import controllers.courseComponent.CourseRegistrationController;

import models.UnikitDatabaseHelper;

import net.unikit.database.unikit_.interfaces.Team;
import net.unikit.database.unikit_.interfaces.TeamRegistration;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;


public class StudentDatabaseConnector{

    /**
     *  Creates a team associated with the studentNumber and courseID
     *  @param studentNumber the first member of the team
     *  @param courseID the course for which the team will be created
     */
    public static void createTeam(String studentNumber, int courseID){
        Team newTeam = Global.getTeamManager().createTeam();
        newTeam.setCourseId(courseID);
        newTeam.setCreatedByStudentNumber(studentNumber);
        Global.getTeamManager().addTeam(newTeam);
    }

    /**
    *  Deletes the team associated with the teamID and updates the flags for the students in the course registration
    *  @param teamID the team that is to be deleted
    *  @return courseForTeam the course for which the team was associated
    */
    public static int deleteTeam(int teamID){

        // TODO: Better mechanic if team wasn't found

        // If team doesn't exist -1 is returned
        int courseForTeam = -1;
        Team teamToBeDeleted = null;

        List<Team> allTeams = Global.getTeamManager().getAllTeams();

        for(Team currentTeam : allTeams){
            if(currentTeam.getId() == teamID){
                teamToBeDeleted = currentTeam;
            }
        }

        checkNotNull(teamToBeDeleted);
        courseForTeam = teamToBeDeleted.getCourseId();

        //Update status of students to single registration
        for(TeamRegistration currentRegistration : teamToBeDeleted.getTeamRegistrations()){
            CourseRegistrationController.changeTeamRegistrationStatus(
                    currentRegistration.getStudentNumber(), currentRegistration.getTeam().getCourseId(),false);
        }

        //Delete team
        Global.getTeamManager().deleteTeam(teamToBeDeleted);


        return courseForTeam;
    }

    /**
     *  The student requests the membership with the team
     *  @param studentNumber the student who wants to join the team
     *  @param teamID the ID for which the students requests membership
     *  @return showCourseDetails-page
     **/
     public static void requestMembership(String studentNumber, int teamID){
        /* TODO:
         *      persist membershiprequest from a student to database
         *      send mail to student and potential teammembers
         */
     }

     /**
      *  The student cancels his membership request with the team
      *  @param studentNumber the student who wants to cancel his membership request
      *  @param teamID the ID of the team the students wants to cancel his request
      *  @return showCourseDetails-page
      **/
    public static void cancelMembershipRequest(String studentNumber, int teamID){
        /* TODO:
         *      delete membership request from database
         *      send mail to team members
         *      send mail to student
         *      display course detail page
         */
    }

    public static void acceptInvitation(){
        /* TODO:
         *      delete invitation from database
         */
    }

    /**
     *   Declines (deletes) the invitation for the student by the team
     *   @param studentNumber the student number of the student who declined the invitation
     *   @param teamID the ID of the team that issued the invite
     **/
    public static void declineInvitation(String studentNumber, int teamID){
         /* TODO
         *      delete invitation from database
         *      send mail to team members
         *      send mail to student
         */
    }

    public Team getTeam(String studentNumber, int courseID) {
        return UnikitDatabaseHelper.getTeam(studentNumber,courseID);
    }
}