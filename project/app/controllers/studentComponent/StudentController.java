package controllers.studentComponent;

/**
 * @author Thomas Bednorz
 */

import assets.SessionUtils;

import controllers.courseComponent.CourseController;

import models.commonUtils.CommonDatabaseUtils;
import models.commonUtils.Exceptions.InvitationNotFoundException;
import models.commonUtils.Exceptions.MembershipRequestNotFoundException;
import models.commonUtils.Exceptions.TeamNotFoundException;

import models.commonUtils.ID.CourseID;
import models.commonUtils.ID.StudentNumber;
import models.commonUtils.ID.TeamID;

import models.studentComponent.StudentModel;
import net.unikit.database.external.interfaces.Student;

import play.mvc.Controller;
import play.mvc.Result;


public class StudentController extends Controller {
    private static Student currentUser;

    static{
        currentUser = SessionUtils.getCurrentUser(session());
    }

    /**
     *  Creates a team associated with the studentNumber and courseID from the CreateTeamForm-object
     *  @param courseID the course ID for which the team is to be created
     *  @return showEditTeam-page
     */
    public static Result createTeam(int courseID){
        CourseID cID = CourseID.get(courseID);
        StudentNumber sNumber = StudentNumber.get(currentUser.getStudentNumber());

        try{
            StudentModel.createTeam(sNumber, cID);
        }
        catch(TeamNotFoundException e){

        }
        CourseID createdTeamID = CommonDatabaseUtils.createTeam(sNumber, cID);

        return redirect(controllers.studentComponent.routes.TeamController.showTeamOverview(createdTeamID.value()));
    }

    /**
     *  Deletes the team associated with the teamID and updates the registration status of all involved students
     *  @param teamID the team ID that is to be deleted
     *  @return showCourseDetails-page for the course the team was associated with
     */
    public static Result deleteTeam(int teamID){
        TeamID tID = TeamID.get(teamID);

        try{
            CommonDatabaseUtils.deleteTeam(tID);
        }
        catch(TeamNotFoundException e){

        }
        int courseForTeam = CommonDatabaseUtils.deleteTeam(tID);

        return CourseController.showCourseDetails(courseForTeam);
    }

    /**
     * Accepts a pending invitation by a team
     * @param teamID the ID of the team that issued the invite
     * @return showTeamOverview-page
     */
    public static Result acceptInvitation(int teamID){

            TeamID tID = TeamID.get(teamID);
            StudentNumber sNumber = StudentNumber.get(currentUser.getStudentNumber());
            try{
            //Add the student to the team and updates registration status
            StudentModel.acceptInvitation(sNumber, tID);
            }catch(InvitationNotFoundException e){
                System.err.println("Invitation not found");
             }

            return redirect(controllers.studentComponent.routes.TeamController.showTeamOverview(tID.value()));

    }

    /**
     *   Declines (deletes) the invitation for the student by the team. The relevant data is retrived using a TeamStateChangeForm-object
     **/
    public static Result declineInvitation(int teamID){
        TeamID tID = TeamID.get(teamID);
        StudentNumber sNumber = StudentNumber.get(currentUser.getStudentNumber());

        try{
            StudentModel.declineInvitation(sNumber, tID);
        }
        catch(Exception e){

        }
        CommonDatabaseUtils.deleteInvitation(sNumber, tID);

        return TeamController.showTeamOverview(teamID);
    }
    /**
     *  The student requests the membership with the team. The relevant data is retrived using a TeamStateChangeForm-object
     *  @param teamID the ID of the team the student requests membership with
     *  @return showCourseDetails-page
    **/
    public static Result requestMembership(int teamID){
        TeamID tID = TeamID.get(teamID);
        StudentNumber sNumber = StudentNumber.get(currentUser.getStudentNumber());

        try{
            StudentModel.requestMembership(sNumber,tID);
        } catch (MembershipRequestNotFoundException e) {

        }

        int courseToDispay = CommonDatabaseUtils.getTeamByID(tID).getCourseId();

        return CourseController.showCourseDetails(courseToDispay);
    }

    /**
     *  The student cancels his membership request with the team. The relevant data is retrived using a TeamStateChangeForm-object
     *  @return showCourseDetails-page
    **/
    public static Result cancelMembershipRequest(int teamID){
        TeamID tID = TeamID.get(teamID);
        StudentNumber sNumber = StudentNumber.get(currentUser.getStudentNumber());
        try{
            StudentModel.cancelMembershipRequest(sNumber, tID);
        } catch (MembershipRequestNotFoundException e) {

        }

        int courseToDispay = CommonDatabaseUtils.getTeamByID(tID).getCourseId();
        return CourseController.showCourseDetails(courseToDispay);
    }
}