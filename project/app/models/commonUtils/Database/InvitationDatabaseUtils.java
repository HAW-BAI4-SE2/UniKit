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
import net.unikit.database.interfaces.entities.Course;
import net.unikit.database.interfaces.entities.Student;
import net.unikit.database.interfaces.entities.Team;
import net.unikit.database.interfaces.entities.TeamInvitation;
import net.unikit.database.interfaces.managers.TeamInvitationManager;

/**
 * Handles the database interaction related to invitations.
 * @author Thomas Bednorz
 */
class InvitationDatabaseUtils {

    /**
     *
     * @param student
     * @param team
     * @param createdBy
     * @throws TeamNotFoundException
     * @throws StudentNotFoundException
     * @throws InvitationExistsException
     */
    public static void storeInvitation(Student student, Team team, Student createdBy) throws InvitationExistsException {
        TeamInvitationManager invitationManager = Global.getTeamInvitationManager();

        TeamInvitation newInvitation = invitationManager.createEntity();
        newInvitation.setCreatedBy(createdBy);
        newInvitation.setInvitee(student);
        newInvitation.setTeam(team);

        try {
            invitationManager.addEntity(newInvitation);

        } catch (ConstraintViolationException e) {
            throw new InvitationExistsException(StudentNumber.get(student.getStudentNumber()), TeamID.get(team.getId()));
        }
    }

    /**
     *
     * @param student
     * @param team
     * @throws InvitationNotFoundException
     */
    public static void deleteInvitation(Student student, Team team) throws InvitationNotFoundException {
        TeamInvitationManager invitationManager = Global.getTeamInvitationManager();

        // Get the invitation to be deleted
        TeamInvitation invitationToBeDeleted = null;

        try {
            invitationToBeDeleted = invitationManager.getInvitation(student,team);
            invitationManager.deleteEntity(invitationToBeDeleted);
        } catch (EntityNotFoundException e) {
            throw new InvitationNotFoundException(TeamID.get(team.getId()));
        }
    }

    /**
     *
     * @param student
     * @param course
     */
    public static void deleteAllInvitations(Student student, Course course) {
        TeamInvitationManager invitationManager = Global.getTeamInvitationManager();
        try {
            for (TeamInvitation currentInvitation : invitationManager.getAllEntities()) {
                if (currentInvitation.getTeam().getCourse().getId().equals(course.getId()) &&
                        currentInvitation.getInvitee().getStudentNumber().equals(student.getStudentNumber())) {
                    try {
                        deleteInvitation(student, currentInvitation.getTeam());
                    } catch (InvitationNotFoundException e) {
                        // Depending of the context of the call, it's expected that no invitations exist
                    }
                }
            }

        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
    }
}

