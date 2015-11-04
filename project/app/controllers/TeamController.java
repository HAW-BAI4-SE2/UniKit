package controllers;

/**
 * @author Thomas Bednorz
 */

import play.mvc.Controller;
import play.mvc.Result;

import views.html.*;

public class TeamController extends Controller {


    private static Result addMember() {
        /* TODO:
         *      persist studentNumber to TEAM_REGISTRATION
         *      send mail to new member
         *      send mail to other team members
         */
        return showEditTeam();
    }

    public static Result deleteMember(){
        /* TODO:
         *      delete studentNumber from TEAM_REGISTRATION
         *      send mail to former member
         *      send mail to other members
         */
        return showEditTeam();
    }

    public static Result inviteStudent(){
        /* TODO:
                persist invitation to database
                send mail to student
                send mail to teammembers
         */
         return showEditTeam();
    }

    public static Result revokeInvitaion(){
        /*
        *   TODO:
        *       delete invitation from database
        *       send mail to invited student
        *       send mail to team members
        */
        return showEditTeam();
    }

    public static Result acceptInvitation(){
        /* TODO:
         *      delete invitation from database
         */
        return addMember();
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
        return showEditTeam();
    }


    public static Result acceptMembershipRequest(){
        /* TODO:
                delete membershiprequest from database
         */
        return addMember();
    }

    public static Result requestMembership(){
        /* TODO:
                persist membershiprequest in database
                send mail to student and potential teammembers
         */
        return ok(showCourseDetails.render());
    }

    /**
     *  Revokes (deletes) the MembershipRequest associated with the studentNumber and teamID and displays the results
     *  @param teamID the teamID of the team for which the membership request was issued
     *  @param studentNumber the studentNumber of the student who issued the membership request
    **/
    public static Result cancelMembershipRequest(){
        /* TODO:
         *      delete membership request from database
         *      send mail to team members
         *      send mail to student
         *      display course detail page
         */
        return ok(showCourseDetails.render());
    }

    public static Result showTeamOverview(){
        /* TODO:
         *      display all registered teams
         *      display all pending requests
         *      display all pending invites
         */
        return ok(showTeamOverview.render());
    }

    public static Result showEditTeam(){
        /**
        *   TODO:
        *       display all members of the team
        *       display all pending requests
        *       display all pending invitations
        **/
        return ok(showEditTeam.render());
    }

    /**
     * Displays the teams for this course for which the student can request membership
     * @return
     */
    public static Result showAvailableTeams(){

        return ok(showAvailableTeams.render());
    }
}
