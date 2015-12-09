package models.studentComponent;

import models.commonUtils.Database.DatabaseUtils;
import models.commonUtils.Exceptions.*;
import models.commonUtils.ID.CourseID;
import models.commonUtils.ID.StudentNumber;
import models.commonUtils.ID.TeamID;
import models.commonUtils.NotificationModel;
import net.unikit.database.exceptions.EntityNotFoundException;
import net.unikit.database.interfaces.entities.Course;
import net.unikit.database.interfaces.entities.Student;
import net.unikit.database.interfaces.entities.Team;

import java.util.List;

/**
 * This class receives the calls from the TeamController.
 * It initiates the updates in the database and informs the affected students of the change.
 * In case of errors it delegates back to the TeamController
 * @author Thomas Bednorz
 */
public class TeamModel {

     /**
     * Removes a student from a team and informs all remaining team members of the change.
     * If the student is the last member of the team, the team gets deleted.
     * @param sNumber the student number of the student that is to be removed.
     * @param tID the ID of the team from which the student is to be removed.
     * @returns the team from from which the student is to be removed.
     * @throws TeamNotFoundException
     * @throws StudentNotInTeamException
     */
    public static Team removeMember(StudentNumber sNumber, TeamID tID) throws TeamNotFoundException, StudentNotInTeamException, StudentNotFoundException, CourseNotFoundException, FatalErrorException {
        // Get team prior to removal of student
        Team thisTeam = DatabaseUtils.getTeam(tID);

        // Delete team if student was the last member, else remove student from team
        if(thisTeam.getTeamRegistrations().size() <= 1){

            try {
                DatabaseUtils.deleteTeam(tID);
            } catch (TeamDeletedException e) {
                // Team already deleted, mission accomplished
            }

            // Inform last member that team was deleted
            NotificationModel.informStudentTeamDeleted(thisTeam, sNumber);

        } else {
            // Remove student from team
            DatabaseUtils.removeStudent(sNumber, tID);

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
     * @throws TeamMaxSizeReachedException
     * @throws CourseNotFoundException
     */
    public static Team inviteStudent(StudentNumber sNumber, TeamID tID, StudentNumber invitedBy) throws FatalErrorException, TeamNotFoundException, InvitationExistsException, StudentNotFoundException, TeamMaxSizeReachedException {

        // Get team prior to addition of student
        Team thisTeam = DatabaseUtils.getTeam(tID);

        //Get course for the team
        Course associatedCourse = null;
        try {
            associatedCourse = thisTeam.getCourse();
        } catch (EntityNotFoundException e) {
            throw new FatalErrorException("Course does not exist");
        }

        // Invite student to team, error if (registrations + invitations) exceeds team size limit
        if((thisTeam.getTeamInvitations().size()
                + thisTeam.getTeamRegistrations().size())
                <= associatedCourse.getMaxTeamSize()){
            DatabaseUtils.storeInvitation(sNumber, tID, invitedBy);

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
     *
     * @param sNumber
     * @param tID
     * @throws InvitationNotFoundException
     * @throws TeamNotFoundException
     * @throws StudentNotFoundException
     */
    public static void cancelInvitation(StudentNumber sNumber, TeamID tID) throws InvitationNotFoundException, TeamNotFoundException, StudentNotFoundException {
        // Get team prior to cancellation of invite
        Team thisTeam = DatabaseUtils.getTeam(tID);

        // Delete invitation
        DatabaseUtils.deleteInvitation(sNumber, tID);

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
    public static Team acceptMembershipRequest(StudentNumber sNumber, TeamID tID) throws MembershipRequestNotFoundException, TeamNotFoundException, StudentNotFoundException, CourseNotFoundException, StudentInTeamException, FatalErrorException {
        // Get team prior to accepting the membership request
        Team thisTeam = DatabaseUtils.getTeam(tID);

        // Accept membership request
        DatabaseUtils.addStudent(sNumber, tID);
        DatabaseUtils.deleteMembershipRequest(sNumber, tID);

        // If invitation exists for the student, delete it.
        try{
            DatabaseUtils.deleteInvitation(sNumber, tID);
        } catch (InvitationNotFoundException e){
            // Do nothing if no invitations exist
        }

        // Inform student of new membership
        NotificationModel.informStudentTeamJoined(tID,sNumber);

        //Inform team of new member
        NotificationModel.informTeamStudentJoined(tID,sNumber);

        return thisTeam;
    }

    /**
     * Declines the membership request from the student to the team
     * @param sNumber the student number of the student who issued the membership request
     * @param tID the ID of the team for which the membership request was issued
     * @throws MembershipRequestNotFoundException
     * @throws TeamNotFoundException
     */
    public static void declineMembershipRequest(StudentNumber sNumber, TeamID tID) throws MembershipRequestNotFoundException, TeamNotFoundException, StudentNotFoundException {
        // Get team prior to declining the membership request
        Team thisTeam = DatabaseUtils.getTeam(tID);

        // Decline membership request
        DatabaseUtils.deleteMembershipRequest(sNumber, tID);

        // Inform student that membership request was declined
        NotificationModel.informStudentMembershipRequestDeclined(thisTeam,sNumber);

    }

    public static List<Team> getAllAvailableTeams(CourseID courseID) throws CourseNotFoundException {
        return DatabaseUtils.getAvailableTeams(courseID);
    }

    public static Team getTeam(TeamID tID) throws TeamNotFoundException {
        return DatabaseUtils.getTeam(tID);
    }

    public static List<Student> getAllMembershipRequests(TeamID tID) throws TeamNotFoundException {
        return DatabaseUtils.getAllMembershipRequests(tID);
    }

    public static List<Student> getAllInvites(TeamID tID) throws TeamNotFoundException {
        return DatabaseUtils.getAllInvites(tID);
    }

    public static void deleteTeam(TeamID tID, StudentNumber deletedBy) throws FatalErrorException, CourseNotFoundException, TeamDeletedException, TeamNotFoundException, StudentNotFoundException, StudentNotInTeamException {
        if (DatabaseUtils.isStudentMember(deletedBy, tID)) {
            DatabaseUtils.deleteTeam(tID);
        } else {
            throw new StudentNotInTeamException(deletedBy, tID);
        }
    }
}
