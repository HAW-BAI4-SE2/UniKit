package models.studentComponent;

import models.commonUtils.CommonDatabaseUtils;
import models.commonUtils.Database.InvitationDatabaseUtils;
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
     * Adds a student to a team and informs all team members of the change.
     * @param sNumber the student number of the student that is to be added
     * @param tID the ID of the team the student is to be added to
     * @throws TeamNotFoundException
     */
    public static void addMember(StudentNumber sNumber, TeamID tID) throws TeamNotFoundException, StudentNotFoundException, CourseNotFoundException, StudentInTeamException {
        // Get team prior to addition of student
        Team originalTeam = CommonDatabaseUtils.getTeamByID(tID);

        // Add student to team
        CommonDatabaseUtils.addStudentToTeam(sNumber, tID);

        // Inform team of new member
        NotificationModel.informTeamStudentJoined(tID,sNumber);

        // Inform student of new membership
        NotificationModel.informStudentTeamJoined(tID,sNumber);
    }

    /**
     * Removes a student from a team and informs all remaining team members of the change.
     * If the student is the last member of the team, the team gets deleted.
     * @param sNumber the student number of the student that is to be removed.
     * @param tID the ID of the team from which the student is to be removed.
     * @returns the team from from which the student is to be removed.
     * @throws TeamNotFoundException
     * @throws StudentNotInTeamException
     */
    public static Team removeMember(StudentNumber sNumber, TeamID tID) throws TeamNotFoundException, StudentNotInTeamException, StudentNotFoundException {
        // Get team prior to removal of student
        Team thisTeam = CommonDatabaseUtils.getTeamByID(tID);

        // Delete team if student was the last member, else remove student from team
        if(thisTeam.getTeamRegistrations().size() <=1){
            // Delete team
            try {
                CommonDatabaseUtils.deleteTeam(tID);
            } catch (TeamDeletedException e) {
                //TODO error handling
                e.printStackTrace();
            }

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
     * @throws TeamMaxSizeReachedException
     * @throws CourseNotFoundException
     */
    public static Team inviteStudent(StudentNumber sNumber, TeamID tID, StudentNumber invitedBy) throws TeamMaxSizeReachedException, TeamNotFoundException, CourseNotFoundException, InvitationExistsException, StudentNotFoundException, FatalErrorException {

        // Get team prior to addition of student
        Team thisTeam = CommonDatabaseUtils.getTeamByID(tID);

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
            InvitationDatabaseUtils.storeInvitation(sNumber, tID, invitedBy);

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
    public static void cancelInvitation(StudentNumber sNumber, TeamID tID) throws InvitationNotFoundException, TeamNotFoundException, StudentNotFoundException {
        // Get team prior to cancellation of invite
        Team thisTeam = CommonDatabaseUtils.getTeamByID(tID);

        // Delete invitation
        InvitationDatabaseUtils.deleteInvitation(sNumber, tID);

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
    public static Team acceptMembershipRequest(StudentNumber sNumber, TeamID tID) throws MembershipRequestNotFoundException, TeamNotFoundException, StudentNotFoundException, CourseNotFoundException, StudentInTeamException {
        // Get team prior to accepting the membership request
        Team thisTeam = CommonDatabaseUtils.getTeamByID(tID);

        // Accept membership request
        CommonDatabaseUtils.deleteMembershipRequest(sNumber, tID);
        CommonDatabaseUtils.addStudentToTeam(sNumber, tID);

        // If invitation exists for the student, delete it.
        try{
            InvitationDatabaseUtils.deleteInvitation(sNumber, tID);
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
        Team thisTeam = CommonDatabaseUtils.getTeamByID(tID);

        // Decline membership request
        CommonDatabaseUtils.deleteMembershipRequest(sNumber, tID);

        // Inform student that membership request was declined
        NotificationModel.informStudentMembershipRequestDeclined(thisTeam,sNumber);

    }

    public static List<Student> getAllStudents(CourseID courseID) throws CourseNotFoundException {
        Course course = CommonDatabaseUtils.getCourseByID(courseID);
        return CommonDatabaseUtils.getAllStudents(course);
    }

    public static List<Student> getAllMembershipRequests(TeamID teamID) throws TeamNotFoundException {
        Team team = CommonDatabaseUtils.getTeamByID(teamID);
        return CommonDatabaseUtils.getAllMembershipRequests(team);
    }

    public static List<Student> getAllStudents(TeamID teamID) throws TeamNotFoundException {
        Team team = CommonDatabaseUtils.getTeamByID(teamID);
        return CommonDatabaseUtils.getAllStudents(team);
    }

    public static List<Student> getAllInvites(TeamID teamID) throws TeamNotFoundException {
        Team team = CommonDatabaseUtils.getTeamByID(teamID);
        return CommonDatabaseUtils.getAllInvites(team);
    }

    public static Course getCourseByID(CourseID courseID) throws CourseNotFoundException {
        return CommonDatabaseUtils.getCourseByID(courseID);
    }

    public static List<Team> getAllTeams(CourseID courseID) throws CourseNotFoundException {
        return CommonDatabaseUtils.getAllTeams(courseID);
    }

    public static List<Team> getAllAvailableTeams(CourseID courseID) throws CourseNotFoundException {
        return CommonDatabaseUtils.getAvailableTeams(courseID);
    }
}
