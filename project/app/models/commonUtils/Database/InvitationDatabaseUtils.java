package models.commonUtils.Database;

import assets.Global;
import models.commonUtils.Exceptions.*;
import models.commonUtils.ID.StudentNumber;
import models.commonUtils.ID.TeamID;
import net.unikit.database.exceptions.ConstraintViolationException;
import net.unikit.database.exceptions.EntityNotFoundException;
import net.unikit.database.interfaces.entities.Course;
import net.unikit.database.interfaces.entities.Student;
import net.unikit.database.interfaces.entities.Team;
import net.unikit.database.interfaces.entities.TeamInvitation;
import net.unikit.database.interfaces.managers.TeamInvitationManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the database interaction related to invitations.
 * @author Thomas Bednorz
 */
class InvitationDatabaseUtils {
    private static TeamInvitationManager invitationManager = Global.getTeamInvitationManager();

    static{
        invitationManager = Global.getTeamInvitationManager();
    }

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
        // Get the invitation to be deleted
        TeamInvitation invitationToBeDeleted = null;

        try {
            invitationToBeDeleted = invitationManager.getInvitation(student,team);
        } catch (EntityNotFoundException e) {
            throw new InvitationNotFoundException(TeamID.get(team.getId()));
        }

        if(invitationToBeDeleted == null){
            throw new InvitationNotFoundException(TeamID.get(team.getId()));
        } else {
            try {
                invitationManager.deleteEntity(invitationToBeDeleted);
            } catch (EntityNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param student
     * @param course
     */
    public static void deleteAllInvitations(Student student, Course course) {
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

    /**
     *
     * @param team
     */
    public static void deleteAllInvitations(Team team) {
        for(TeamInvitation currentInvitation : invitationManager.getAllEntities()){
            if(currentInvitation.getTeam().equals(team)){
                try {
                    deleteInvitation(currentInvitation.getInvitee(),team);
                } catch (InvitationNotFoundException e) {
                    // Invitation may have already been deleted, do nothin
                } catch (EntityNotFoundException e) {
                    // Student for invitation not found
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     *
     * @param team
     * @return
     */
    public static List<Student> getAllInvites(Team team) {
        List<Student> allInvitedStudents = new ArrayList<>();
        for(TeamInvitation currentInvitation : invitationManager.getAllEntities()){
            if(currentInvitation.getTeam().equals(team)){
                try {
                    allInvitedStudents.add(currentInvitation.getInvitee());
                } catch (EntityNotFoundException e) {
                    // TODO: Handle this internal database error (erroneous foreign keys)
                    e.printStackTrace();
                }
            }
        }
        return allInvitedStudents;
    }

    /**
     *
     * @param student
     * @param course
     * @return
     * @throws StudentNotFoundException
     * @throws CourseNotFoundException
     */
    public static List<TeamInvitation> getAllInvitations(Student student, Course course) throws StudentNotFoundException, CourseNotFoundException {
        List<TeamInvitation> allInvitations = new ArrayList<>();
        for(TeamInvitation currentInvitation : invitationManager.getAllEntities()){
            try {
                if(currentInvitation.getInvitee().equals(student) &&
                        currentInvitation.getTeam().getCourse().equals(course)){
                    allInvitations.add(currentInvitation);
                }
            } catch (EntityNotFoundException e) {
                // TODO: Handle this internal database error (erroneous foreign keys)
                e.printStackTrace();
            }

        }
        return allInvitations;
    }

    /**
     *
     * @param student
     * @param team
     * @return
     */
    public static boolean isStudentInvited(Student student, Team team){
        for(TeamInvitation currentInvitation : invitationManager.getAllEntities()){
            try {
                if(currentInvitation.getInvitee().equals(student) && currentInvitation.getTeam().equals(team)){
                    return true;
                }
            } catch (EntityNotFoundException e) {
                // Any student couldn't be found, don't handle (sideeffects)
                // Any team couldn't be found, don't handle
                e.printStackTrace();
            }
        }
        return false;
    }
}

