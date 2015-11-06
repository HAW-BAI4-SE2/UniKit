package controllers.studentComponent;

/**
 * @author Thomas Bednorz
 */

import models.studentComponent.FormModels.CreateTeamFormModel;
import models.studentComponent.StudentDatabaseConnector;

import controllers.courseComponent.CourseController;

import net.unikit.database.unikit_.interfaces.Team;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import views.html.*;

public class StudentController extends Controller {

    private static StudentDatabaseConnector databaseConnector = new StudentDatabaseConnector();
    /**
     *  Creates a team associated with the studentNumber and courseID
     *  @return showEditTeam-page
     */
    public static Result createTeam(){
        /* TODO: send mail to student
        */

        Form<CreateTeamFormModel> createTeamForm =
                Form.form(CreateTeamFormModel.class)
                        .bindFromRequest();
        CreateTeamFormModel ctfm = createTeamForm.get();

        if(ctfm.studentNumber != null && ctfm.courseID != 0){
            StudentDatabaseConnector.createTeam(ctfm.studentNumber, ctfm.courseID);
            Team newTeam = databaseConnector.getTeam(ctfm.studentNumber, ctfm.courseID);
            return TeamController.showEditTeam(newTeam);
        }else{
            return internalServerError("There was an error creating the team, please contact the administrator");
        }
    }

    /**
     *  Deletes the team associated with the teamID and updates the registration status of all involved students
     *  @param teamID the team that is to be deleted
     *  @return showCourseDetails-page for the course the team was associated with
     */
    public static Result deleteTeam(int teamID){
        int courseForTeam = StudentDatabaseConnector.deleteTeam(teamID);
        return CourseController.showCourseDetails(courseForTeam);
    }

    /**
     *  The student requests the membership with the team. The relevant data is retrived using a TeamStateChangeForm-object
     *  @return showCourseDetails-page
    **/
    public static Result requestMembership(){
        /* TODO:
         *      persist membershiprequest from a student to database
         *      send mail to student and potential teammembers
         */
        int courseToDispay = -1;

        return CourseController.showCourseDetails(courseToDispay);
    }

    /**
     *  The student cancels his membership request with the team. The relevant data is retrived using a TeamStateChangeForm-object
     *  @return showCourseDetails-page
    **/
    public static Result cancelMembershipRequest(){
        /* TODO:
         *      delete membership request from database
         *      send mail to team members
         *      send mail to student
         */
        int courseToDispay = -1;
        return CourseController.showCourseDetails(courseToDispay);
    }

    public static Result acceptInvitation(){
        /* TODO:
         *      delete invitation from database
         */
        return TeamController.addMember();
    }

    /**
    *   Declines (deletes) the invitation for the student by the team. The relevant data is retrived using a TeamStateChangeForm-object
    **/
    public static Result declineInvitation(){
        /* TODO
         *      delete invitation from database
         *      send mail to team members
         *      send mail to student
         */

        Team teamToDisplay = null;

        return TeamController.showEditTeam(teamToDisplay);
    }
}