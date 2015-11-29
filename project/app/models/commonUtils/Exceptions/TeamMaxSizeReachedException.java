package models.commonUtils.Exceptions;

import models.commonUtils.ID.TeamID;

/**
 * This exception gets thrown when a team attempts to invite more students than there are free slots in the team.
 * It contains a generic error message and the TeamID of the team.
 * @author Thomas Bednorz
 */
public class TeamMaxSizeReachedException extends Exception {
    private final TeamID teamID;

    public TeamMaxSizeReachedException(TeamID tID) {
        super("Maximum number of invites reached");
        this.teamID = tID;
    }

    public TeamID getTeamID() {
        return teamID;
    }
}
