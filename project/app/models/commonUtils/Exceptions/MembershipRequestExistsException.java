package models.commonUtils.Exceptions;

import models.commonUtils.ID.StudentNumber;
import models.commonUtils.ID.TeamID;

/**
 * Created by tbu on 12/1/2015.
 */
public class MembershipRequestExistsException extends Exception {
    private final StudentNumber studentNumber;
    private final TeamID teamID;

    public MembershipRequestExistsException(StudentNumber sNumber, TeamID tID) {
        super("Membershiprequest already exists");
        this.studentNumber = sNumber;
        this.teamID = tID;
    }

    public StudentNumber getStudentNumber() {
        return studentNumber;
    }

    public TeamID getTeamID() {
        return teamID;
    }
}
