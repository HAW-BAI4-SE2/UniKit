package controllers.studentComponent;

/**
 * @author Thomas Bednorz
 */

import assets.Global;

import models.studentComponent.FormModels.CreateTeamFormModel;
import models.studentComponent.StudentDatabaseConnector;

import controllers.courseComponent.CourseController;

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
            databaseConnector.createTeam(ctfm.studentNumber, ctfm.courseID);
            return TeamController.showEditTeam(ctfm.studentNumber,ctfm.courseID);
        }

        return internalServerError("There was an error creating the team, please contact the administrator");
    }

    /**
     *  Deletes the team associated with the teamID and updates the registration status of all involved students
     *  @param teamID the team that is to be deleted
     *  @return showCourseDetails-page for the course the team was associated with
     */
    public static Result deleteTeam(int teamID){
        int courseIDForTeam = StudentDatabaseConnector.deleteTeam(teamID);
        return CourseController.showCourseDetails(courseIDForTeam);
    }

    /**
     *  The student requests the membership with the team
     *  @param studentID the student who wants to join the team
     *  @param teamID the ID for which the students requests membership
     *  @return showCourseDetails-page
    **/
    public static Result requestMembership(){
        /* TODO:
         *      persist membershiprequest from a student to database
         *      send mail to student and potential teammembers
         */
        return CourseController.showCourseDetails();
    }

    /**
     *  The student cancels his membership request with the team
     *  @param studentNumber the student who wants to cancel his membership request
     *  @param teamID the ID of the team the students wants to cancel his request
     *  @return showCourseDetails-page
    **/
    public static Result cancelMembershipRequest(){
        /* TODO:
         *      delete membership request from database
         *      send mail to team members
         *      send mail to student
         */
        return CourseController.showCourseDetails();
    }

    public static Result acceptInvitation(){
        /* TODO:
         *      delete invitation from database
         */
        return TeamController.addMember();
    }

    /**
    *   Declines (deletes) the invitation for the student by the team
    *   @param teamID the ID of the team that issued the invite
    *   @param studentNumber the student number of the student who declined the invitation
    **/
    public static Result declineInvitation(){
        /* TODO
         *      delete invitation from database
         *      send mail to team members
         *      send mail to student
         */
        return TeamController.showEditTeam();
    }
}