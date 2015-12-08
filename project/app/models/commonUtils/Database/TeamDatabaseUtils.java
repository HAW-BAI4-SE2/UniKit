package models.commonUtils.Database;

import assets.Global;
import models.commonUtils.Exceptions.*;
import models.commonUtils.ID.CourseID;
import models.commonUtils.ID.StudentNumber;
import models.commonUtils.ID.TeamID;
import net.unikit.database.exceptions.ConstraintViolationException;
import net.unikit.database.exceptions.EntityNotFoundException;
import net.unikit.database.interfaces.entities.Course;
import net.unikit.database.interfaces.entities.Student;
import net.unikit.database.interfaces.entities.Team;
import net.unikit.database.interfaces.entities.TeamRegistration;
import net.unikit.database.interfaces.managers.TeamManager;
import net.unikit.database.interfaces.managers.TeamRegistrationManager;

/**
 * Handles the database interaction related to teams.
 * @author Thomas Bednorz
 */
class TeamDatabaseUtils {

    /**
     *
     * @param student the student that is to be added to the team
     * @param team the team to which the student is to be added
     * @throws CourseNotFoundException if the course associated with the team couldn't be found
     * @throws StudentInTeamException if the student is already a member of the team
     * @throws FatalErrorException if the student or the course couldn't be found during registration change
     */
    public static void addStudentToTeam(Student student, Team team) throws StudentNotFoundException, TeamNotFoundException, CourseNotFoundException, StudentInTeamException, FatalErrorException {
        // Init
        TeamRegistrationManager teamRegistrationManager = Global.getTeamRegistrationManager();

        // Get course of the Team
        Course courseForTeam = null;
        try {
            courseForTeam = team.getCourse();
        } catch (EntityNotFoundException e) {
            throw new CourseNotFoundException();
        }

        // Add student to team
        TeamRegistration newTeamRegistration = teamRegistrationManager.createEntity();
        newTeamRegistration.setStudent(student);
        newTeamRegistration.setTeam(team);

        try {
            teamRegistrationManager.addEntity(newTeamRegistration);
        } catch (ConstraintViolationException e) {
            throw new StudentInTeamException(StudentNumber.get(student.getStudentNumber()));
        }

        // Delete all pending invitations
        InvitationDatabaseUtils.deleteAllInvitations(student, courseForTeam);

        // Delete all pending membership requests
        MembershipRequestDatabaseUtils.deleteAllMembershipRequests(student, courseForTeam);

        //change registration Status of the student
        StudentDatabaseUtils.changeTeamRegistrationStatus(student,courseForTeam , true);
    }

    /**
     *
     * @param tID
     * @return
     * @throws TeamNotFoundException
     */
    public static Team getTeam(TeamID tID) throws TeamNotFoundException {
        TeamManager teamManager = Global.getTeamManager();
        Team team = null;
        try {
            team = teamManager.getEntity(teamManager.createID(tID.value()));
        } catch (EntityNotFoundException e) {
            throw new TeamNotFoundException(tID);
        }

        return team;
    }

    /**
     * Returns the team for the student number and the course ID
     * @param student the student number of the student
     * @param course the course ID for the team
     * @return the Team-object for the student number and course ID
     */
    public static Team getTeam(Student student, Course course) throws TeamNotFoundException {
        TeamRegistrationManager teamRegistrationManager = Global.getTeamRegistrationManager();
        // Get the team
        Team queriedTeam = null;
        try{
            for(TeamRegistration currentTeamRegistration : teamRegistrationManager.getAllEntities()) {
                if (currentTeamRegistration.getTeam().getCourse().getId().equals(course.getId()) &&
                        currentTeamRegistration.getStudent().getStudentNumber().equals(student.getStudentNumber())) {
                    return currentTeamRegistration.getTeam();
                }
            }
        } catch (Exception e){
            // TODO: Error handling?
            e.printStackTrace();
        }

        throw new TeamNotFoundException();
    }
}
