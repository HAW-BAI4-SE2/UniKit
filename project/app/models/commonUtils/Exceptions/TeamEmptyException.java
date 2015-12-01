package models.commonUtils.Exceptions;

import models.commonUtils.ID.TeamID;

/**
 * @author Thomas Bednorz
 */
public class TeamEmptyException extends Exception {
    private final TeamID teamID;

    public TeamEmptyException(TeamID teamID) {
        super("Team is empty");
        this.teamID = teamID;
    }

    public TeamID getTeamID(){
        return teamID;
    }
}
