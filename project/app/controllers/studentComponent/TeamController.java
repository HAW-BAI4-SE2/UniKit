package controllers.studentComponent;

/**
 * @author Thomas Bednorz
 */

import assets.SessionUtils;
import models.commonUtils.Database.DatabaseUtils;
import models.commonUtils.Exceptions.*;
import models.commonUtils.ID.CourseID;
import models.commonUtils.ID.IDUtils;
import models.commonUtils.ID.StudentNumber;
import models.commonUtils.ID.TeamID;
import models.courseComponent.CourseModel;
import models.studentComponent.StudentModel;
import models.studentComponent.TeamModel;
import net.unikit.database.exceptions.EntityNotFoundException;
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

        } catch (FatalErrorException e) {
            // Gets thrown when an error occurs while updating the registration status of the student
            // TODO error message
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());

        } catch (CourseNotFoundException e) {
            // TODO error message
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());
        }

        // If team is empty, display course details, else team overview
        if(modifiedTeam.getTeamRegistrations().isEmpty()){
            // TODO error message
            try {
                return redirect(controllers.courseComponent.routes.CourseController.showCourseDetails(CourseID.get(modifiedTeam.getCourse().getId()).value());

            } catch (EntityNotFoundException e) {
                e.printStackTrace();
            }
        }else{
            // TODO error message
            return redirect(controllers.studentComponent.routes.TeamController.showTeamOverview(teamID));
        }
        return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());
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

        } catch (FatalErrorException e) {
            // Gets thrown when an error occurs while updating the registration status of the student
            //TODO error message
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());
        }
    }

    /**
     * Cancels the invitation from a team to a student
     * @param studentNumber the student number of the student who's invitation gets cancelled
     * @param teamID the ID of the team for which the invitation gets cancelled
     * @return a result page either displaying the team overview page (success) or a page with the error message
     */
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

    /**
     * Accepts the membership request a student issued to a team
     * @param studentNumber the student number of the student who issued the membership request
     * @param teamID the ID of the team that accepts the request
     * @return a result page either displaying the team overview page (success) or a page with the error message
     */
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

        } catch (StudentInTeamException e) {
            // TODO error message
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());

        } catch (CourseNotFoundException e) {
            // TODO error message
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());

        } catch (FatalErrorException e) {
            // TODO error message
            // Gets thrown when an error occurs while updating the registration status of the student
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());
        }
    }

    /**
     * Declines the membership request issued by a student to a team
     * @param studentNumber the student number of the student who issued the membership request
     * @param teamID the ID of the team that declines the membership request
     * @return a result page either displaying the team overview page (success) or a page with the error message
     */
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
     * Displays the details of a team. Includes current members, pending invitations and pending membership requests
     * @param teamID the ID of the team for which the details should be displayed
     * @return a result page containing all relevant team details
     */
    public static Result showTeamOverview(int teamID){
        Date sessionTimeout = SessionUtils.getSessionTimeout(session());

        Team teamToDisplay = null;
        List<Student> allStudentsInTeam = null;
        List<Student> allMembershipRequests = null;
        List<Student> allInvites = null;
        try {
            teamToDisplay = TeamModel.getTeam(TeamID.get(teamID));
            allMembershipRequests = TeamModel.getAllMembershipRequests(TeamID.get(teamID));
            allStudentsInTeam = StudentModel.getAllStudents(TeamID.get(teamID));
            allInvites = TeamModel.getAllInvites(TeamID.get(teamID));
        } catch (TeamNotFoundException e) {
            // TODO error message
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());
        }

        // Get all students who are not in the team
        Course associatedCourse = null;
        try {
            associatedCourse = teamToDisplay.getCourse();
        } catch (EntityNotFoundException e) {
            controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview();
        }
        List<Student> availableStudents = null;
        try {
            availableStudents = StudentModel.getAllStudents(CourseID.get(associatedCourse.getId()));
            availableStudents.removeAll(allStudentsInTeam);
        } catch (CourseNotFoundException e) {
            // TODO error message
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());
        }

        return ok(showTeamOverview.render(teamToDisplay, allStudentsInTeam, allMembershipRequests, allInvites, associatedCourse, availableStudents, currentUser, sessionTimeout));
}


    /**
     * Displays all teams for which the student can request membership with
     * @param courseID the ID of the course for which all available teams should be displayed
     * @return a result page containing all teams for which the student can request membership,
     * or redirects to the course overview page with an appropirate error code
     */
    public static Result showAvailableTeams(int courseID){
        Student currentUser = SessionUtils.getCurrentUser(session());
        Date sessionTimeout = SessionUtils.getSessionTimeout(session());

        Course course = null;
        List<Team> availableTeams = null;
        try {
            course = CourseModel.getCourse(CourseID.get(courseID));
            TeamModel.getAllAvailableTeams(CourseID.get(courseID));
        } catch (CourseNotFoundException e) {
            // TODO error message
            return redirect(controllers.courseComponent.routes.CourseRegistrationController.showCourseOverview());
        }

        return ok(showAvailableTeams.render(availableTeams, course, currentUser, sessionTimeout));
    }
}