package models.studentComponent;

import models.commonUtils.CommonDatabaseUtils;
import models.commonUtils.Exceptions.*;
import models.commonUtils.ID.CourseID;
import models.commonUtils.ID.StudentNumber;
import models.commonUtils.ID.TeamID;
import models.commonUtils.NotificationModel;
import net.unikit.database.external.interfaces.Course;
import net.unikit.database.external.interfaces.Student;
import net.unikit.database.unikit_.interfaces.Team;

/**
 * This class receives the calls from the TeamController.
 * It initiates the updates in the database and informs the affected students of the change.
 * In case of errors it delegates back to the TeamController
 * @author Thomas Bednorz
 */
public class TeamModel {

    /**
     * Adds a student to a team and informs all team members of the change.
     * @param sNumber the student number of the student that is to be added
     * @param tID the ID of the team the student is to be added to
     * @throws TeamNotFoundException
     */
    public static void addMember(StudentNumber sNumber, TeamID tID) throws TeamNotFoundException {
        // Get team prior to addition of student
        Team originalTeam = CommonDatabaseUtils.getTeamByID(tID);

        // Add student to team
        CommonDatabaseUtils.addStudentToTeam(sNumber, tID);

        // Inform team of new member
        NotificationModel.informTeamStudentJoined(originalTeam,sNumber);

        // Inform student of new membership
        NotificationModel.informStudentTeamJoined(originalTeam,sNumber);
    }

    /**
     * Removes a student from a team and informs all remaining team members of the change.
     * If the student is the last member of the team, the team gets deleted.
     * @param sNumber the student number of the student that is to be removed.
     * @param tID the ID of the team from which the student is to be removed.
     * @returns the team from from which the student is to be removed.
     * @throws TeamNotFoundException
     * @throws StudentNotTeamMemberException
     */
    public static Team removeMember(StudentNumber sNumber, TeamID tID) throws TeamNotFoundException, StudentNotTeamMemberException{
        // Get team prior to removal of student
        Team thisTeam = CommonDatabaseUtils.getTeamByID(tID);

        // Delete team if student was the last member, else remove student from team
        if(thisTeam.getTeamRegistrations().size() <=1){
            // Delete team
            CommonDatabaseUtils.deleteTeam(tID);

            // Inform last member that team was deleted
            NotificationModel.informStudentTeamDeleted(thisTeam, sNumber);

        } else {
            // Remove student from team
            CommonDatabaseUtils.removeStudentFromTeam(sNumber, tID);

            // Inform student of new status
            NotificationModel.informStudentRemovedFromTeam(thisTeam,sNumber);

            // Inform team of removed member
            NotificationModel.informTeamStudentRemoved(thisTeam, sNumber);

        }
        return thisTeam;
    }

    /**
     * Invites a student to join the team if the team has enough free slots.
     * Informs the team and the student of the change.
     * @param sNumber the student number of the student that is to be invited.
     * @param tID the ID of the team to which the student is to be invited.
     * @param invitedBy the Student who issued the invite.
     * @returns the team to which the student was invited
     * @throws TeamNotFoundException
     * @throws FatalErrorException
     * @throws TeamMaxSizeReachedException
     * @throws CourseNotFoundException
     */
    public static Team inviteStudent(StudentNumber sNumber, TeamID tID, Student invitedBy) throws TeamMaxSizeReachedException, TeamNotFoundException, FatalErrorException, CourseNotFoundException {
        // Check student
        if(invitedBy == null){
            throw new FatalErrorException("No Student issued the invite");
        }

        // Get team prior to addition of student
        Team thisTeam = CommonDatabaseUtils.getTeamByID(tID);

        //Get course for the team
        Course associatedCourse = CommonDatabaseUtils.getCourseByID(CourseID.get(thisTeam.getCourseId()));

        // Invite student to team, error if (registrations + invitations) exceeds team size limit
        if((thisTeam.getTeamInvitations().size()
                + thisTeam.getTeamRegistrations().size())
                <= associatedCourse.getMaxTeamSize()){
            CommonDatabaseUtils.storeInvitation(sNumber, tID, invitedBy);

            // Inform student of invite
            NotificationModel.informStudentInvited(thisTeam, sNumber);

            // Inform team of invite
            NotificationModel.informTeamStudentInvited(thisTeam,sNumber);
        } else {
            throw new TeamMaxSizeReachedException(tID);
        }
        return thisTeam;
    }

    /**
     * Cancels the invitation for the student to the team
     * @param sNumber the student number of the student who was invited
     * @param tID the ID of the team to which the student was invited
     * @throws InvitationNotFoundException
     * @throws TeamNotFoundException
     */
    public static void cancelInvitation(StudentNumber sNumber, TeamID tID) throws InvitationNotFoundException, TeamNotFoundException{
        // Get team prior to cancellation of invite
        Team thisTeam = CommonDatabaseUtils.getTeamByID(tID);

        // Delete invitation
        CommonDatabaseUtils.deleteInvitation(sNumber, tID);

        // Inform student that his invitation was canceled
        NotificationModel.informStudentInviteCancelled(thisTeam, sNumber);
    }

    /**
     * Accepts the membership request from the student to the team.
     * If the team already invited the student, the invitation is deleted.
     * Informs the student and the team of the changes.
     * @param sNumber the student number of the student who requested membership
     * @param tID the ID of the team for which the membership request was requested
     * @throws MembershipRequestNotFoundException
     * @throws TeamNotFoundException
     */
    public static Team acceptMembershipRequest(StudentNumber sNumber, TeamID tID) throws MembershipRequestNotFoundException, TeamNotFoundException, InvitationNotFoundException {
        // Get team prior to accepting the membership request
        Team thisTeam = CommonDatabaseUtils.getTeamByID(tID);

        // Accept membership request
        CommonDatabaseUtils.deleteMembershipRequest(sNumber, tID);
        CommonDatabaseUtils.addStudentToTeam(sNumber, tID);

        // If invitation exists for the student, delete it.
        try{
            CommonDatabaseUtils.deleteInvitation(sNumber, tID);
        } catch (InvitationNotFoundException e){
            // Do nothing if no invitations exist
        }

        // Inform student of new membership
        NotificationModel.informStudentTeamJoined(thisTeam,sNumber);

        //Inform team of new member
        NotificationModel.informTeamStudentJoined(thisTeam,sNumber);

        return thisTeam;
    }

    /**
     * Declines the membership request from the student to the team
     * @param sNumber the student number of the student who issued the membership request
     * @param tID the ID of the team for which the membership request was issued
     * @throws MembershipRequestNotFoundException
     * @throws TeamNotFoundException
     */
    public static void declineMembershipRequest(StudentNumber sNumber, TeamID tID) throws MembershipRequestNotFoundException, TeamNotFoundException {
        // Get team prior to declining the membership request
        Team thisTeam = CommonDatabaseUtils.getTeamByID(tID);

        // Decline membership request
        CommonDatabaseUtils.deleteMembershipRequest(sNumber, tID);

        // Inform student that membership request was declined
        NotificationModel.informStudentMembershipRequestDeclined(thisTeam,sNumber);

    }
}
