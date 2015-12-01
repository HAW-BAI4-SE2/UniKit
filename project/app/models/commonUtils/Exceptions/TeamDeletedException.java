package models.commonUtils.Exceptions;

import models.commonUtils.ID.TeamID;

/**
 * Created by tbu on 12/1/2015.
 */
public class TeamDeletedException extends Exception {
    private final TeamID teamID;
    public TeamDeletedException(TeamID tID) {
        super("Team deleted");
        this.teamID = tID;
    }

    public TeamID getTeamID() {
        return teamID;
    }
}
