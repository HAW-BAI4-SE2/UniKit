package controllers.studentComponent;

/**
 * @author Thomas Bednorz
 */

import assets.SessionUtils;
import controllers.courseComponent.CourseController;
import controllers.courseComponent.CourseRegistrationController;
import models.commonUtils.Database.DatabaseUtils;
import models.commonUtils.Exceptions.*;
import models.commonUtils.ID.CourseID;
import models.commonUtils.ID.StudentNumber;
import models.commonUtils.ID.TeamID;
import models.courseComponent.CourseModel;
import models.studentComponent.StudentModel;
import models.studentComponent.TeamModel;
import net.unikit.database.exceptions.EntityNotFoundException;
import net.unikit.database.interfaces.entities.Course;
import net.unikit.database.interfaces.entities.Student;
import net.unikit.database.interfaces.entities.Team;
import net.unikit.database.interfaces.managers.CourseManager;
import play.mvc.Controller;
import play.mvc.Result;


public class StudentController extends Controller {
    /**
     *  Creates a team associated with the studentNumber and courseID from the CreateTeamForm-object
     *  @param courseID the course ID for which the team is to be created
     *  @return showEditTeam-page
     */
    public static Result createTeam(int courseID){
        Student currentUser = SessionUtils.getCurrentUser(session());
        CourseID cID = CourseID.get(courseID);
        StudentNumber sNumber = StudentNumber.get(currentUser.getStudentNumber());

        // ID of the new team
        TeamID teamID = null;
        try{
            teamID = StudentModel.createTeam(sNumber, cID);
            return TeamController.showTeamOverview(teamID.value());

        } catch (StudentNotFoundException e) {
            //TODO error message
            e.printStackTrace();
            return CourseRegistrationController.showCourseOverview();

        } catch (StudentInTeamException e) {
            //TODO error message
            e.printStackTrace();
            return CourseRegistrationController.showCourseOverview();

        } catch (CourseNotFoundException e) {
            //TODO error message
            e.printStackTrace();
            return CourseRegistrationController.showCourseOverview();

        } catch (FatalErrorException e) {
            // If team couldn't be created or registration status couldn't be updated
            //TODO error message
            e.printStackTrace();
            return CourseRegistrationController.showCourseOverview();

        } catch (TeamExistsException e) {
            //TODO error message
            e.printStackTrace();
            return CourseRegistrationController.showCourseOverview();
        }

    }

    /**
     *  Deletes the team associated with the teamID and updates the registration status of all involved students
     *  @param teamID the team ID that is to be deleted
     *  @return showCourseDetails-page for the course the team was associated with
     */
    public static Result deleteTeam(int teamID){
        Student currentUser = SessionUtils.getCurrentUser(session());
        TeamID tID = TeamID.get(teamID);
        StudentNumber deletedBy = StudentNumber.get(currentUser.getStudentNumber());

        try{
            TeamModel.deleteTeam(tID,deletedBy);
            return CourseRegistrationController.showCourseOverview();
        }
        catch(TeamNotFoundException e){
            //TODO error message
            e.printStackTrace();
            return CourseRegistrationController.showCourseOverview();

        } catch (StudentNotInTeamException e) {
            //TODO error message
            e.printStackTrace();
            return CourseRegistrationController.showCourseOverview();

        } catch (TeamDeletedException e) {
            //TODO error message
            e.printStackTrace();
            return CourseRegistrationController.showCourseOverview();

        } catch (FatalErrorException e) {
            // This code gets executed in case the team couldn't be created for some reason
            //TODO error message
            e.printStackTrace();
            return CourseRegistrationController.showCourseOverview();

        } catch (CourseNotFoundException e) {
            //TODO error message
            e.printStackTrace();
            return CourseRegistrationController.showCourseOverview();

        } catch (StudentNotFoundException e) {
            //TODO error message
            e.printStackTrace();
            return CourseRegistrationController.showCourseOverview();
        }
    }

    /**
     * Accepts a pending invitation by a team
     * @param teamID the ID of the team that issued the invite
     * @return showTeamOverview-page
     */
    public static Result acceptInvitation(int teamID) {
        Student currentUser = SessionUtils.getCurrentUser(session());
        TeamID tID = TeamID.get(teamID);
        StudentNumber sNumber = StudentNumber.get(currentUser.getStudentNumber());

        try{
            //Add the student to the team and updates registration status
            StudentModel.acceptInvitation(sNumber, tID);
            return TeamController.showTeamOverview(teamID);

        } catch (TeamNotFoundException e) {
            //TODO error message
            e.printStackTrace();
            return CourseRegistrationController.showCourseOverview();

        } catch (StudentNotFoundException e) {
            //TODO error message
            e.printStackTrace();
            return CourseRegistrationController.showCourseOverview();

        } catch (StudentInTeamException e) {
            //TODO error message
            e.printStackTrace();
            return CourseRegistrationController.showCourseOverview();

        }
    }

    /**
     *
     * @param teamID
     * @return
     */
    public static Result declineInvitation(int teamID){
        Student currentUser = SessionUtils.getCurrentUser(session());
        TeamID tID = TeamID.get(teamID);
        StudentNumber sNumber = StudentNumber.get(currentUser.getStudentNumber());

        try{
            StudentModel.declineInvitation(sNumber, tID);
            return CourseRegistrationController.showCourseOverview();

        } catch (InvitationNotFoundException e) {
            //TODO error message
            e.printStackTrace();
            return CourseRegistrationController.showCourseOverview();

        } catch (TeamNotFoundException e) {
            //TODO error message
            e.printStackTrace();
            return CourseRegistrationController.showCourseOverview();

        } catch (StudentNotFoundException e) {
            //TODO error message
            e.printStackTrace();
            return CourseRegistrationController.showCourseOverview();
        }
    }
    /**
     *  The student requests the membership with the team. The relevant data is retrived using a TeamStateChangeForm-object
     *  @param teamID the ID of the team the student requests membership with
     *  @return showCourseDetails-page
    **/
    public static Result requestMembership(int teamID){
        Student currentUser = SessionUtils.getCurrentUser(session());
        TeamID tID = TeamID.get(teamID);
        StudentNumber sNumber = StudentNumber.get(currentUser.getStudentNumber());

        try {
            StudentModel.requestMembership(sNumber, tID);
            return TeamController.showTeamOverview(teamID);

        } catch (CourseNotFoundException e) {
            //TODO error message
            e.printStackTrace();
            return CourseRegistrationController.showCourseOverview();

        } catch (TeamNotFoundException e) {
            //TODO error message
            e.printStackTrace();
            return CourseRegistrationController.showCourseOverview();

        } catch (StudentNotFoundException e) {
            //TODO error message
            e.printStackTrace();
            return CourseRegistrationController.showCourseOverview();

        } catch (StudentInTeamException e) {
            //TODO error message
            e.printStackTrace();
            return CourseRegistrationController.showCourseOverview();

        } catch (MembershipRequestExistsException e) {
            //TODO error message
            e.printStackTrace();
            return CourseRegistrationController.showCourseOverview();
        }
    }

    /**
     *  The student cancels his membership request with the team. The relevant data is retrived using a TeamStateChangeForm-object
     *  @return showCourseDetails-page
    **/
    public static Result cancelMembershipRequest(int teamID) {
        Student currentUser = SessionUtils.getCurrentUser(session());
        TeamID tID = TeamID.get(teamID);
        StudentNumber sNumber = StudentNumber.get(currentUser.getStudentNumber());

        Course courseToDisplay = null;
        Team team = null;
        try {
            StudentModel.cancelMembershipRequest(sNumber, tID);
            team = DatabaseUtils.getTeam(tID);

        } catch (MembershipRequestNotFoundException e) {
            //TODO error message
            e.printStackTrace();
            return CourseRegistrationController.showCourseOverview();

        } catch (TeamNotFoundException e) {
            //TODO error message
            e.printStackTrace();
            return CourseRegistrationController.showCourseOverview();

        } catch (StudentNotFoundException e) {
            //TODO error message
            e.printStackTrace();
            return CourseRegistrationController.showCourseOverview();
        }

        try {
            courseToDisplay = CourseModel.getCourse(CourseID.get(team.getCourse().getId()));

        } catch (EntityNotFoundException e) {
            //TODO error message
            e.printStackTrace();
            return CourseRegistrationController.showCourseOverview();

        } catch (CourseNotFoundException e) {
            //TODO error message
            e.printStackTrace();
            return CourseRegistrationController.showCourseOverview();
        }
        return CourseController.showCourseDetails(courseToDisplay.getId().getValue());
    }
}