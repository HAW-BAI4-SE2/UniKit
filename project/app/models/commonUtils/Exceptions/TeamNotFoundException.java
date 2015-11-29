package models.commonUtils.Exceptions;

import models.commonUtils.ID.TeamID;

/**
 * This exception gets thrown when a team can't be found in the database.
 * It containt the TeamID of the respectice team.
 * @author Thomas Bednorz
 */
public class TeamNotFoundException extends Exception {
    private final TeamID teamId;

    public TeamNotFoundException(TeamID teamID){
        super("Team not found");
        this.teamId = teamID;
    }

    public TeamNotFoundException(){
        this(null);
    }

    public TeamID getTeamId() {
        return teamId;
    }
}