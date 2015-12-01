package models.commonUtils.Exceptions;

import models.commonUtils.ID.StudentNumber;
import models.commonUtils.ID.TeamID;

/**
 * @author Thomas Bednorz
 */
public class InvitationExistsException extends Throwable {
    private final StudentNumber studentNumber;
    private final TeamID teamID;

    public InvitationExistsException(StudentNumber sNumber, TeamID tID) {
        super("Invitation already exists");
        this.studentNumber = sNumber;
        this.teamID = tID;
    }

    public StudentNumber getStudentNumber() {
        return studentNumber;
    }

    public TeamID getTeamID(){
        return teamID;
    }
}
