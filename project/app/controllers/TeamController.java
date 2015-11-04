package controllers;

/**
 * @author Thomas Bednorz
 */

import play.mvc.Controller;
import play.mvc.Result;

import views.html.*;

public class TeamController extends Controller {

    public static Result deleteMember(){
        /* TODO:
                delete studentNumber from TEAM_REGISTRATION
                send mail to student
                display EditTeam-page
         */
        return ok(showEditTeam.render());
    }

    private static Result addMember() {
        /* TODO:
                persist studentNumber to TEAM_REGISTRATION
                display EditTeam-page
         */
        return ok(showEditTeam.render());
    }

    public static Result inviteStudent(){
        /* TODO:
                persist TeamInvitation for studentNumber and teamID
                send mail to student
                send mail to teammembers
                display EditTeam-page
         */
        return ok(showEditTeam.render());
    }

    public static Result acceptMembershipRequest(){
        /* TODO:
                delete invite
                send mail to requesting student
                send mail to all members of the team
                add the student
         */
        return addMember();
    }

    public static Result requestMembership(){
        /* TODO:
                Persist request for studentNumber and teamID
                send mail to student and potential teammembers
                display CourseDetail-page
         */
        return ok(showCourseDetails.render());
    }

    public static Result showTeamOverview(){
        /* TODO:
                display all registered teams
                display all pending requests
                display all pending invites
         */
        return ok(showTeamOverview.render());
    }

    public static Result acceptInvitation(){
        /* TODO:
                delete invite
                send mail to team members
                send mail to student
                add student to team
         */
        return addMember();
    }

    public static Result declineInvitation(){
        /* TODO
                delete invitation
                send mail to team members
                send mail to student
                display team overview
         */
        return ok(showCourseDetails.render());
    }

    public static Result deleteMembershipRequest(){
    /* TODO:
            delete membership request
            send mail to team members
            send mail to student
            display course detail page
     */
        return ok(showCourseDetails.render());
    }

    /**
     * Displays the teams for this course for which the student can request membership
     * @return
     */
    public static Result showAvailableTeams(){

        return ok(showAvailableTeams.render());
    }
}
