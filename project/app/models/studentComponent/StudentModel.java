package models.studentComponent;

import assets.SessionUtils;
import models.commonUtils.Database.DatabaseUtils;
import models.commonUtils.Exceptions.*;
import models.commonUtils.ID.CourseID;
import models.commonUtils.ID.StudentNumber;
import models.commonUtils.ID.TeamID;
import models.commonUtils.NotificationModel;
import net.unikit.database.interfaces.entities.MembershipRequest;
import net.unikit.database.interfaces.entities.Student;
import net.unikit.database.interfaces.entities.Team;
import net.unikit.database.interfaces.entities.TeamInvitation;

import java.util.List;

import static models.commonUtils.NotificationModel.*;
import static play.mvc.Controller.session;

/**
 * @author Jana Wengenroth
 */
public class StudentModel {
    /**
     *
     * @param sNumber
     * @param courseID
     * @return
     * @throws CourseNotFoundException
     * @throws StudentNotFoundException
     * @throws StudentInTeamException
     * @throws FatalErrorException
     * @throws TeamExistsException
     */
    public static TeamID createTeam(StudentNumber sNumber, CourseID courseID) throws CourseNotFoundException, StudentNotFoundException, StudentInTeamException, FatalErrorException, TeamExistsException {

        // is the student in an other team in this course?
        try {
            DatabaseUtils.getTeam(sNumber,courseID);
            throw new StudentInTeamException(sNumber);
        } catch (TeamNotFoundException e) {
            return DatabaseUtils.createTeam(sNumber, courseID);
        }
    }

    /**
     *
     * @param tID
     * @throws FatalErrorException
     * @throws CourseNotFoundException
     * @throws TeamNotFoundException
     */
    public static void deleteTeam(TeamID tID) throws FatalErrorException, CourseNotFoundException, TeamNotFoundException {
        StudentNumber currentUser = StudentNumber.get(SessionUtils.getCurrentUser(session()).getStudentNumber());

        Team thisTeam = DatabaseUtils.getTeam(tID);
        try{
            DatabaseUtils.deleteTeam(tID);
            NotificationModel.informTeamTeamDeletedBy(thisTeam,currentUser);
        } catch (TeamDeletedException e){
            throw new TeamNotFoundException(TeamID.get(thisTeam.getId()));
        }
    }

    /**
     *
     * @param sNumber
     * @param tID
     * @throws StudentNotFoundException
     * @throws StudentInTeamException
     * @throws TeamNotFoundException
     */
    public static void acceptInvitation(StudentNumber sNumber, TeamID tID) throws StudentNotFoundException, StudentInTeamException, TeamNotFoundException {
        try {
            DatabaseUtils.addStudent(sNumber, tID);
            DatabaseUtils.deleteInvitation(sNumber,tID);
        } catch (FatalErrorException e) {
            e.printStackTrace();
        } catch (CourseNotFoundException e) {
            e.printStackTrace();
        } catch (InvitationNotFoundException e) {
            // If student could be added to team, it's of no consequence that the invitation couldn't be found
        }

        try {
            DatabaseUtils.deleteMembershipRequest(sNumber, tID);
        } catch (MembershipRequestNotFoundException e) {
            // It's possible the student had no membership request for the team, do nothing
        }
        informStudentTeamJoined(tID, sNumber);
    }

    /**
     *
     * @param sNumber
     * @param tID
     * @throws InvitationNotFoundException
     * @throws TeamNotFoundException
     * @throws StudentNotFoundException
     */
    public static void declineInvitation(StudentNumber sNumber, TeamID tID) throws InvitationNotFoundException, TeamNotFoundException, StudentNotFoundException {
        DatabaseUtils.deleteInvitation(sNumber, tID);
        NotificationModel.informTeamInviteCancelled(tID, sNumber);
        NotificationModel.informTeamInviteCancelled(tID, sNumber);
    }

    /**
     * Stores the membership request for a student and a team. If the team already invited the student, he gets added to the team and the invitation gets deleted
     * @param sNumber the student number of the requesting student
     * @param tID the ID of the team that receives the request
     * @throws TeamNotFoundException if the team couldn't be found
     * @throws StudentNotFoundException if the student couldn't be found
     * @throws CourseNotFoundException if the associated course couldn't be found
     * @throws FatalErrorException if an error occurs while changing the registration status of the student
     * @throws StudentInTeamException if the student already is in the team
     * throws MembershipRequestExistsException if membership request already exists
     */
    public static void requestMembership(StudentNumber sNumber, TeamID tID) throws TeamNotFoundException, StudentNotFoundException, CourseNotFoundException, StudentInTeamException, MembershipRequestExistsException {
            if(DatabaseUtils.isStudentInvited(sNumber,tID)){
                try {
                    DatabaseUtils.addStudent(sNumber, tID);
                } catch (FatalErrorException e) {
                    // Gets thrown when a error occurs while updating registration status
                    e.printStackTrace();
                }

                try {
                DatabaseUtils.deleteInvitation(sNumber, tID);
            } catch (InvitationNotFoundException e) {
                // Do nothing, if the invitation gets deleted after the membership gets requested, all is well
            }
            informStudentTeamJoined(tID, sNumber);
            informTeamStudentJoined(tID, sNumber);

        } else {
            DatabaseUtils.storeMembershipRequest(sNumber, tID);
            informStudentMembershipRequested(tID, sNumber);
            informTeamMembershipRequested(tID, sNumber);
        }
    }

    /**
     * Deletes a membership request by a student and informs the team that the student cancelled his request
     * @param sNumber the student number of the requesting student
     * @param tID the ID of the team that receives the request
     * @throws MembershipRequestNotFoundException if the membership request couldn't be found
     * @throws TeamNotFoundException if the team couldn't be found
     * @throws StudentNotFoundException if the student couldn't be found
     */
    public static void cancelMembershipRequest(StudentNumber sNumber, TeamID tID) throws MembershipRequestNotFoundException, TeamNotFoundException, StudentNotFoundException {
        // delete membership request
        DatabaseUtils.deleteMembershipRequest(sNumber, tID);

        // Inform team of membership request was deleted
        NotificationModel.informTeamStudentDeletedMembershipRequest(tID, sNumber);
    }

    public static List<Student> getAllStudents(TeamID tID) throws TeamNotFoundException {
        return DatabaseUtils.getAllStudents(tID);
    }

    public static List<Student> getAllStudents(CourseID cID) throws CourseNotFoundException {
        return DatabaseUtils.getAllStudents(cID);
    }

    public static List<MembershipRequest> getMembershipRequests(StudentNumber sNumber, CourseID cID) throws CourseNotFoundException, StudentNotFoundException {
        return DatabaseUtils.getAllMembershipRequests(sNumber, cID);
    }

    public static List<TeamInvitation> getInvitations(StudentNumber sNumber, CourseID cID) throws CourseNotFoundException, StudentNotFoundException {
        return DatabaseUtils.getAllInvitations(sNumber,cID);
    }
}
