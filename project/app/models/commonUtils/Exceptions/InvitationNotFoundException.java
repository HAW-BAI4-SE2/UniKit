package models.commonUtils.Exceptions;

import models.commonUtils.ID.TeamID;

/**
 * This exception gets thrown when a invitation does not exist. It contains a generic errormessage and the TeamID for the team
 * @author Thomas Bednorz
 */
public class InvitationNotFoundException extends Exception{
    private final TeamID teamID;

    public InvitationNotFoundException(TeamID tID){
        super("Invitation not found");
        this.teamID = tID;
    }

    public TeamID getTeamID(){
        return teamID;
    }
}