package models.commonUtils.Exceptions;

import models.commonUtils.ID.StudentNumber;
import models.commonUtils.ID.TeamID;

/**
 * This Exception gets thrown when the student is not a member of the team.
 * It contains the StudentNumber of the respective student and the TeamID of the team.
 * @author Thomas Bednorz
 */
public class NotTeamMemberExcpetion extends Exception {
    private final StudentNumber student;
    private final TeamID teamID;

    public NotTeamMemberExcpetion(StudentNumber sNumber, TeamID tID) {
        super("Student not member of the team");
        this.student = sNumber;
        this.teamID = tID;
    }

    public StudentNumber getStudent() {
        return student;
    }

    public TeamID getTeamID() {
        return teamID;
    }
}
