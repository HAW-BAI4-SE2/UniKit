package controllers.studentComponent;

/**
 * @author Thomas Bednorz
 */

import assets.Global;
import assets.SessionUtils;
import controllers.courseComponent.CourseController;
import models.commonUtils.ID.CourseID;
import models.commonUtils.ID.StudentNumber;
import models.commonUtils.ID.TeamID;
import models.studentComponent.StudentDatabaseUtils;
import net.unikit.database.external.interfaces.Student;
import net.unikit.database.unikit_.interfaces.TeamInvitation;
import net.unikit.database.unikit_.interfaces.TeamInvitationManager;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class StudentController extends Controller {
    static Student currentUser;

    static{
        currentUser = SessionUtils.getCurrentUser(session());
    }

    /**
     *  Creates a team associated with the studentNumber and courseID from the CreateTeamForm-object
     *  @param id the course ID for which the team is to be created
     *  @return showEditTeam-page
     */
    public static Result createTeam(int id){
        CourseID courseID = CourseID.get(id);

        int createdTeamID = StudentDatabaseUtils.createTeam(currentUser.getStudentNumber(), courseID);

        //TODO send mail to student

        return redirect(controllers.studentComponent.routes.TeamController.showTeamOverview(createdTeamID));
    }

    /**
     *  Deletes the team associated with the teamID and updates the registration status of all involved students
     *  @param id the team ID that is to be deleted
     *  @return showCourseDetails-page for the course the team was associated with
     */
    public static Result deleteTeam(int id){
        TeamID teamID = TeamID.get(id);

        int courseForTeam = StudentDatabaseUtils.deleteTeam(teamID);

        return CourseController.showCourseDetails(courseForTeam);
    }

    /**
     * Accepts a pending invitation by a team
     * @param id the ID of the team that issued the invite
     * @return showTeamOverview-page
     */
    public static Result acceptInvitation(int id){
        TeamID teamID = TeamID.get(id);

        //Add the student to the team and updates registration status
        StudentDatabaseUtils.acceptInvitation(currentUser.getStudentNumber(), teamID);

        //TODO: send mail to team members

        return redirect(controllers.studentComponent.routes.TeamController.showTeamOverview(teamID.value()));
    }

    /**
     *   Declines (deletes) the invitation for the student by the team. The relevant data is retrived using a TeamStateChangeForm-object
     **/
    public static Result declineInvitation(int id){
        TeamID teamID = TeamID.get(id);

        StudentDatabaseUtils.deleteInvitation(currentUser.getStudentNumber(), teamID);

        //TODO: send mail to teammembers & student

        return TeamController.showEditTeam(id);
    }
    /**
     *  The student requests the membership with the team. The relevant data is retrived using a TeamStateChangeForm-object
     *  @param id the ID of the team the student requests membership with
     *  @return showCourseDetails-page
    **/
    public static Result requestMembership(int id){
        TeamID teamID = TeamID.get(id);
        StudentNumber studentNumber = StudentNumber.get(currentUser.getStudentNumber());

        if (StudentDatabaseUtils.isStudentInvited(studentNumber, teamID)) {
            acceptInvitation(teamID.value());
        } else {
            StudentDatabaseUtils.storeMembershipRequest(currentUser.getStudentNumber(), teamID);
        }
        //TODO: send mail to team members & student

        int courseToDispay = StudentDatabaseUtils.getTeamByID(teamID).getCourseId();

        return CourseController.showCourseDetails(courseToDispay);
    }

    /**
     *  The student cancels his membership request with the team. The relevant data is retrived using a TeamStateChangeForm-object
     *  @return showCourseDetails-page
    **/
    public static Result cancelMembershipRequest(int id){
        TeamID teamID = TeamID.get(id);
        StudentNumber studentNumber = StudentNumber.get(currentUser.getStudentNumber());

        StudentDatabaseUtils.deleteMembershipRequest(studentNumber,teamID);
        
        //TODO: send mail to student

        int courseToDispay = StudentDatabaseUtils.getTeamByID(teamID).getCourseId();
        return CourseController.showCourseDetails(courseToDispay);
    }
}