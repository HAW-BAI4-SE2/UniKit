package controllers.studentComponent;

/**
 * @author Thomas Bednorz
 */

import assets.Global;

import net.unikit.database.external.interfaces.Course;
import net.unikit.database.external.interfaces.Student;
import net.unikit.database.unikit_.interfaces.CourseRegistration;

import play.mvc.Controller;
import play.mvc.Result;

import views.html.*;

public class StudentController extends Controller {

    /**
     *  Creates a team associated with the studentNumber and courseID
     *  @param studentNumber the first member of the team
     *  @param courseID the course for which the team will be created    
     *  @return showEditTeam-page
     */
    public static Result createTeam(){
        /* 
         *  TODO: 
         *      create team in database
         *      send mail to student
        */
        return ok(showEditTeam.render());
    }

    /**
     *  Deletes the team associated with the studentNumber and courseID
     *  @param team the team that is to be deleted
     *  @return showCourseDetails-page
     */
    public static Result deleteTeam(){
        /*
         *  TODO:
         *      delete team from database
         *      change flag in COURSE_REGISTRATION for all students
        */
        return TeamController.showCourseDetails();
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
        return TeamController.showCourseDetails();
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
         *      display course detail page
         */
        return TeamController.showCourseDetails();
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