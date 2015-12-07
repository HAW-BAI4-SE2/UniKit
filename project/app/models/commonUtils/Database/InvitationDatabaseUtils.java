package models.commonUtils.Database;

import assets.Global;
import models.commonUtils.Exceptions.InvitationExistsException;
import models.commonUtils.Exceptions.InvitationNotFoundException;
import models.commonUtils.Exceptions.StudentNotFoundException;
import models.commonUtils.Exceptions.TeamNotFoundException;
import models.commonUtils.ID.StudentNumber;
import models.commonUtils.ID.TeamID;
import net.unikit.database.exceptions.ConstraintViolationException;
import net.unikit.database.exceptions.EntityNotFoundException;
import net.unikit.database.interfaces.entities.Student;
import net.unikit.database.interfaces.entities.Team;
import net.unikit.database.interfaces.entities.TeamInvitation;
import net.unikit.database.interfaces.managers.TeamInvitationManager;

/**
 * Handles the database interaction related to invitations.
 * @author Thomas Bednorz
 */
public class InvitationDatabaseUtils {

    /**
     *
     * @param invitedStudentNumber
     * @param invitingTeamID
     * @param createdByStudentNumber
     * @throws TeamNotFoundException
     * @throws StudentNotFoundException
     * @throws InvitationExistsException
     */
    public static void storeInvitation(StudentNumber invitedStudentNumber, TeamID invitingTeamID, StudentNumber createdByStudentNumber) throws TeamNotFoundException, StudentNotFoundException, InvitationExistsException {
        TeamInvitationManager invitationManager = Global.getTeamInvitationManager();

        // Get object for invited student
        Student invitedStudent = StudentDatabaseUtils.getStudent(invitedStudentNumber);

        // Get object for inviting student
        Student createdByStudent = StudentDatabaseUtils.getStudent(createdByStudentNumber);

        // Get object for inviting team
        Team invitingTeam = TeamDatabaseUtils.getTeam(invitingTeamID);

        // If invitation already exists, inform the system

        TeamInvitation newInvitation = invitationManager.createEntity();
        try {
            invitationManager.getInvitation(invitedStudent,invitingTeam);
            throw new InvitationExistsException(invitedStudentNumber,invitingTeamID);

        } catch (EntityNotFoundException e) {
            newInvitation.setCreatedBy(createdByStudent);
            newInvitation.setInvitee(invitedStudent);
            newInvitation.setTeam(invitingTeam);
        }

        try {
            invitationManager.addEntity(newInvitation);

        } catch (ConstraintViolationException e) {
            throw new InvitationExistsException(invitedStudentNumber,invitingTeamID);
        }
    }

    /**
     *
     * @param sNumber
     * @param tID
     */
    public static void deleteInvitation(StudentNumber sNumber, TeamID tID) throws InvitationNotFoundException, StudentNotFoundException, TeamNotFoundException {
        TeamInvitationManager invitationManager = Global.getTeamInvitationManager();

        // Get student object
        Student invitedStudent = StudentDatabaseUtils.getStudent(sNumber);

        // Get team object
        Team invitingTeam = TeamDatabaseUtils.getTeam(tID);

        // Get the invitation to be deleted
        TeamInvitation invitationToBeDeleted = null;

        try {
            invitationToBeDeleted = invitationManager.getInvitation(invitedStudent,invitingTeam);
        } catch (EntityNotFoundException e) {
            throw new InvitationNotFoundException(tID);
        }

        try {
            invitationManager.deleteEntity(invitationToBeDeleted);
        } catch (EntityNotFoundException e) {
            throw new InvitationNotFoundException(tID);
        }

    }

}
