package controllers.studentComponent;

/**
 * @author Thomas Bednorz
 */

import assets.SessionUtils;
import models.commonUtils.CommonDatabaseUtils;
import models.commonUtils.Exceptions.*;
import models.commonUtils.ID.CourseID;
import models.commonUtils.ID.IDUtils;
import models.commonUtils.ID.StudentNumber;
import models.commonUtils.ID.TeamID;
import models.studentComponent.TeamModel;
import net.unikit.database.interfaces.entities.Course;
import net.unikit.database.interfaces.entities.Student;
import net.unikit.database.interfaces.entities.Team;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.showAvailableTeams;
import views.html.showTeamOverview;

import java.util.Date;
import java.util.List;


public class TeamController extends Controller {

    private static Student currentUser;

    static{
        currentUser = SessionUtils.getCurrentUser(session());
    }

    /**
     * Removes a student from the team and returns a resultpage. If student was the last member, the associated team gets deleted.
     * @param studentNumber the student number of the student who is to be removed from the team
     * @param teamID the ID of the team from which the student is to be removed
     * @return team overview page if student was removed successful or if student is not a member of the team,
     * course detail page if the team wasn't found or if team was deleted, course overview page if team couldn't be found
     */
    public static Result removeMember(String studentNumber, int teamID){
        // Init
        StudentNumber sNumber = StudentNumber.get(studentNumber);
        TeamID tID = TeamID.get(teamID);

        // Remove student from team or delete team if last member
        Team modifiedTeam = null;
        try {
            modifiedTeam = TeamModel.removeMember(sNumber, tID);
        } catch (TeamNotFoundException e) {
            // TODO error message
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());
        } catch (StudentNotInTeamException e) {
            // TODO error message
            return redirect(controllers.studentComponent.routes.TeamController.showTeamOverview(teamID));
        } catch (StudentNotFoundException e) {
            // TODO error message
            return redirect(controllers.studentComponent.routes.TeamController.showTeamOverview(teamID));
        }

        // If team is empty, display course details, else team overview
        if(modifiedTeam.getTeamRegistrations().isEmpty()){
            // TODO error message
            return redirect(controllers.courseComponent.routes.CourseController.showCourseDetails(IDUtils.getInt(modifiedTeam.getCourse())));
        }else{
            // TODO error message
            return redirect(controllers.studentComponent.routes.TeamController.showTeamOverview(teamID));
        }
    }

    /**
     * Invites a student to a team and returns a resultpage. If the student has a pending membership request, the student
     * is immediatly added to the team.
     * @param studentNumber the student number of the student that is to be invited
     * @param teamID the ID of the team for which the student is to be invited
     * @return
     */
    public static Result inviteStudent(String studentNumber, int teamID) {
        // Init
        StudentNumber sNumber = StudentNumber.get(studentNumber);
        StudentNumber createdBySNumber = StudentNumber.get(currentUser.getStudentNumber());
        TeamID tID = TeamID.get(teamID);

        try {
            TeamModel.inviteStudent(sNumber, tID, createdBySNumber);
            return redirect(controllers.studentComponent.routes.TeamController.showTeamOverview(teamID));

        } catch (TeamMaxSizeReachedException e) {
            // TODO error message
            return redirect(controllers.studentComponent.routes.TeamController.showTeamOverview(teamID));

        } catch (TeamNotFoundException e) {
            // TODO error message
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());

        } catch (CourseNotFoundException e) {
            // TODO error message
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());

        } catch (InvitationExistsException e) {
            // TODO error message
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());

        } catch (StudentNotFoundException e) {
            // TODO error message
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());
        }
    }

    public static Result cancelInvitation(String studentNumber, int teamID){
        // Init
        StudentNumber sNumber = StudentNumber.get(studentNumber);
        TeamID tID = TeamID.get(teamID);

        try {
            TeamModel.cancelInvitation(sNumber, tID);
            return redirect(controllers.studentComponent.routes.TeamController.showTeamOverview(teamID));
        } catch (InvitationNotFoundException e) {
            // TODO error message
            return redirect(controllers.studentComponent.routes.TeamController.showTeamOverview(teamID));
        } catch (TeamNotFoundException e) {
            // TODO error message
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());
        } catch (StudentNotFoundException e) {
            // TODO error message
            return redirect(controllers.studentComponent.routes.TeamController.showTeamOverview(teamID));
        }
    }

    public static Result acceptMembershipRequest(String studentNumber, int teamID){
        // Init
        StudentNumber sNumber = StudentNumber.get(studentNumber);
        TeamID tID = TeamID.get(teamID);

        try {
            TeamModel.acceptMembershipRequest(sNumber,tID);
            return redirect(controllers.studentComponent.routes.TeamController.showTeamOverview(teamID));
        } catch (MembershipRequestNotFoundException e) {
            // TODO error message
            return redirect(controllers.studentComponent.routes.TeamController.showTeamOverview(teamID));
        } catch (TeamNotFoundException e) {
            // TODO error message
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());
        } catch (StudentNotFoundException e) {
            // TODO error message
            return redirect(controllers.studentComponent.routes.TeamController.showTeamOverview(teamID));
        }
    }

    public static Result declineMembershipRequest(String studentNumber, int teamID){
        // Init
        StudentNumber sNumber = StudentNumber.get(studentNumber);
        TeamID tID = TeamID.get(teamID);

        try {
            TeamModel.declineMembershipRequest(sNumber,tID);
            return redirect(controllers.studentComponent.routes.TeamController.showTeamOverview(teamID));
        } catch (MembershipRequestNotFoundException e) {
            // TODO error message
            return redirect(controllers.studentComponent.routes.TeamController.showTeamOverview(teamID));
        } catch (TeamNotFoundException e) {
            // TODO error message
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());
        } catch (StudentNotFoundException e) {
            // TODO error message
            return redirect(controllers.studentComponent.routes.TeamController.showTeamOverview(teamID));
        }
    }

    /**
     *   Displays the details for a team
     **/
    public static Result showTeamOverview(int teamID){
        Date sessionTimeout = SessionUtils.getSessionTimeout(session());

        Team teamToDisplay = null;
        List<Student> allStudentsInTeam = null;
        List<Student> allMembershipRequests = null;
        List<Student> allInvites = null;
        try {
            teamToDisplay = CommonDatabaseUtils.getTeamByID(TeamID.get(teamID));
            allMembershipRequests = TeamModel.getAllMembershipRequests(TeamID.get(teamID));
            allStudentsInTeam = TeamModel.getAllStudents(TeamID.get(teamID));
            allInvites = TeamModel.getAllInvites(TeamID.get(teamID));
        } catch (TeamNotFoundException e) {
            // TODO error message
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());
        }

        // Get all students who are not in the team
        Course associatedCourse = teamToDisplay.getCourse();
        List<Student> availableStudents = null;
        try {
            availableStudents = TeamModel.getAllStudents(CourseID.get(associatedCourse.getId()));
        } catch (CourseNotFoundException e) {
            // No Students found
        }

        availableStudents.removeAll(allStudentsInTeam);


        return ok(showTeamOverview.render(teamToDisplay, allStudentsInTeam, allMembershipRequests, allInvites, associatedCourse, availableStudents, currentUser, sessionTimeout));
}


    /**
     *  Displays the teams for this course for which the student can request membership
     *  @return
     **/ 
    public static Result showAvailableTeams(int courseID){
        Student currentUser = SessionUtils.getCurrentUser(session());
        Date sessionTimeout = SessionUtils.getSessionTimeout(session());

        Course course = null;
        try {
            course = TeamModel.getCourseByID(CourseID.get(courseID));
        } catch (CourseNotFoundException e) {
            e.printStackTrace();
        }

        List<Team> availableTeams = null;
        try {
            TeamModel.getAllAvailableTeams(CourseID.get(courseID));
        } catch (CourseNotFoundException e) {
            // Course doesn't exist
        }


        return ok(showAvailableTeams.render(availableTeams, course, currentUser, sessionTimeout));
    }
}