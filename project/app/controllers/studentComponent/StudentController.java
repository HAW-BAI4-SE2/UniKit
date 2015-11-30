package controllers.studentComponent;

/**
 * @author Thomas Bednorz
 */

import assets.SessionUtils;

import controllers.courseComponent.CourseController;

import models.commonUtils.CommonDatabaseUtils;
import models.commonUtils.Exceptions.*;

import models.commonUtils.ID.CourseID;
import models.commonUtils.ID.StudentNumber;
import models.commonUtils.ID.TeamID;

import models.studentComponent.StudentModel;

import net.unikit.database.interfaces.entities.*;

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

        // ID of the new team
        TeamID teamID = null;
        try{
            teamID = StudentModel.createTeam(sNumber, cID);
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        } catch (StudentAlreadyInTeamException e) {
            e.printStackTrace();
        } catch (CourseNotFoundException e) {
            e.printStackTrace();
        } catch (FatalErrorException e) {
            // This code gets executed in case the team couldn't be created for some reason
        }
        return redirect(controllers.studentComponent.routes.TeamController.showTeamOverview(teamID.value()));
    }

    /**
     *  Deletes the team associated with the teamID and updates the registration status of all involved students
     *  @param teamID the team ID that is to be deleted
     *  @return showCourseDetails-page for the course the team was associated with
     */
    public static Result deleteTeam(int teamID){
        TeamID tID = TeamID.get(teamID);

        try{
            StudentModel.deleteTeam(tID);
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());
        }
        catch(TeamNotFoundException e){
            //TODO error message
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());
        } catch (NotTeamMemberExcpetion e) {
            //TODO error message
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());
        }
    }

    /**
     * Accepts a pending invitation by a team
     * @param teamID the ID of the team that issued the invite
     * @return showTeamOverview-page
     */
    public static Result acceptInvitation(int teamID) {
            TeamID tID = TeamID.get(teamID);
            StudentNumber sNumber = StudentNumber.get(currentUser.getStudentNumber());

            try{
            //Add the student to the team and updates registration status
            StudentModel.acceptInvitation(sNumber, tID);
                return redirect(controllers.studentComponent.routes.TeamController.showTeamOverview(teamID));
            }catch(InvitationNotFoundException e){
                //TODO error message
                return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());
            } catch (TeamNotFoundException e) {
                //TODO error message
                return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());
            } catch (StudentNotFoundException e) {
                //TODO error message
                return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());
            }
    }

    /**
     *   Declines (deletes) the invitation for the student by the team. The relevant data is retrived using a TeamStateChangeForm-object
     **/
    public static Result declineInvitation(int teamID){
        TeamID tID = TeamID.get(teamID);
        StudentNumber sNumber = StudentNumber.get(currentUser.getStudentNumber());

        try{
            StudentModel.declineInvitation(sNumber, tID);
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());

        } catch (InvitationNotFoundException e) {
            //TODO error message
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());

        } catch (TeamNotFoundException e) {
            //TODO error message
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());

        } catch (StudentNotFoundException e) {
            //TODO error message
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());
        }
    }
    /**
     *  The student requests the membership with the team. The relevant data is retrived using a TeamStateChangeForm-object
     *  @param teamID the ID of the team the student requests membership with
     *  @return showCourseDetails-page
    **/
    public static Result requestMembership(int teamID){
        TeamID tID = TeamID.get(teamID);
        StudentNumber sNumber = StudentNumber.get(currentUser.getStudentNumber());

        try {
            StudentModel.requestMembership(sNumber, tID);
            return redirect(controllers.studentComponent.routes.TeamController.showTeamOverview(teamID));
        } catch (CourseNotFoundException e) {
            //TODO error message
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());
        } catch (TeamMaxSizeReachedException e) {
            //TODO error message
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());
        } catch (TeamNotFoundException e) {
            //TODO error message
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());
        } catch (StudentNotFoundException e) {
            //TODO error message
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());
        }
    }

    /**
     *  The student cancels his membership request with the team. The relevant data is retrived using a TeamStateChangeForm-object
     *  @return showCourseDetails-page
    **/
    public static Result cancelMembershipRequest(int teamID) {
        TeamID tID = TeamID.get(teamID);
        StudentNumber sNumber = StudentNumber.get(currentUser.getStudentNumber());

        CourseID courseToDisplay = null;
        try {
            StudentModel.cancelMembershipRequest(sNumber, tID);

        } catch (MembershipRequestNotFoundException e) {
            //TODO error message
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());

        } catch (TeamNotFoundException e) {
            //TODO error message
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());

        } catch (StudentNotFoundException e) {
            //TODO error message
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());
        }

        int courseToDispay = -1;
        try {
            courseToDispay = CommonDatabaseUtils.getTeamByID(tID).getCourse().getId().getValue();
        } catch (TeamNotFoundException e) {
            //TODO error message
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());
        }
        return CourseController.showCourseDetails(courseToDispay);
    }
}