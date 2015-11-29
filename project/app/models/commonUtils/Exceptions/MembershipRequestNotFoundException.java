package models.commonUtils.Exceptions;

import models.commonUtils.ID.StudentNumber;
import models.commonUtils.ID.TeamID;

/**
 * This exception gets thrown when a membership request could not be found in the database.
 * It contains the TeamID of the associated team and the student number of the requesting student
 * @author Thomas Bednorz
 */
public class MembershipRequestNotFoundException extends Exception{
    private final StudentNumber student;
    private final TeamID teamID;

    public MembershipRequestNotFoundException(StudentNumber sNumber, TeamID tID){
        super("Membership request not found");
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