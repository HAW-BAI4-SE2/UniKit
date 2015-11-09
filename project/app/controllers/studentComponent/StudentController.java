package controllers.studentComponent;

/**
 * @author Thomas Bednorz
 */

import assets.SessionUtils;
import models.UnikitDatabaseUtils;
import models.courseComponent.CourseDatabaseUtils;
import models.studentComponent.FormModels.CreateTeamFormModel;
import models.studentComponent.FormModels.TeamStateChangeFormModel;
import models.studentComponent.StudentDatabaseUtils;

import controllers.courseComponent.CourseController;

import models.studentComponent.TeamDatabaseUtils;
import net.unikit.database.external.interfaces.Student;
import net.unikit.database.unikit_.interfaces.Team;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;

public class StudentController extends Controller {

    /**
     *  Creates a team associated with the studentNumber and courseID from the CreateTeamForm-object
     *  @return showEditTeam-page
     */
    public static Result createTeam(int courseID){
//        Form<CreateTeamFormModel> createTeamForm =
//                Form.form(CreateTeamFormModel.class)
//                        .bindFromRequest();
//        CreateTeamFormModel createTeam = createTeamForm.get();

        Student currentUser = SessionUtils.getCurrentUser(session());
        Date sessionTimeout = SessionUtils.getSessionTimeout(session());

        //TODO: form validation
        checkNotNull(currentUser.getStudentNumber());
        checkNotNull(courseID);

        int newTeam = StudentDatabaseUtils.createTeam(currentUser.getStudentNumber(), courseID);

        //TODO send mail to student

        return redirect(controllers.studentComponent.routes.TeamController.showTeamOverview(newTeam));
    }

    /**
     *  Deletes the team associated with the teamID and updates the registration status of all involved students
     *  @param teamID the team that is to be deleted
     *  @return showCourseDetails-page for the course the team was associated with
     */
    public static Result deleteTeam(int teamID){
        //TODO: send mail to all teammembers

        int courseForTeam = StudentDatabaseUtils.deleteTeam(teamID);
        return CourseController.showCourseDetails(courseForTeam);
    }

    public static Result acceptInvitation(){
        Form<TeamStateChangeFormModel> acceptInvitation =
                Form.form(TeamStateChangeFormModel.class)
                        .bindFromRequest();
        TeamStateChangeFormModel teamStateChange = acceptInvitation.get();

        //TODO: actual form validation
        checkNotNull(teamStateChange.studentNumber);
        checkNotNull(teamStateChange.teamID);

        //Add the student to the team and updates registration status
        StudentDatabaseUtils.acceptInvitation(teamStateChange.studentNumber, teamStateChange.teamID);

        //TODO: send mail to team members

        return TeamController.addMember();
    }

    /**
     *   Declines (deletes) the invitation for the student by the team. The relevant data is retrived using a TeamStateChangeForm-object
     **/
    public static Result declineInvitation(){
        Form<TeamStateChangeFormModel> declinedInvitationForm =
                Form.form(TeamStateChangeFormModel.class)
                        .bindFromRequest();
        TeamStateChangeFormModel declinedInvitation = declinedInvitationForm.get();

        //TODO: actual form validation
        checkNotNull(declinedInvitation.studentNumber);
        checkNotNull(declinedInvitation.teamID);

        StudentDatabaseUtils.declineInvitation(declinedInvitation.studentNumber,declinedInvitation.teamID);

        //TODO: send mail to teammembers & student

        return TeamController.showEditTeam(declinedInvitation.teamID);
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
}