package models.commonUtils.ID;

import net.unikit.database.interfaces.entities.Team;

/**
 * @author Thomas Bednorz
 */
public class TeamID {
    private int teamID;

    public static TeamID get(int teamID) throws IllegalArgumentException{
        return new TeamID(teamID);
    }

    public static TeamID get(Team.ID teamID) {
        return new TeamID(teamID.getValue());
    }

    private TeamID() {}

    private TeamID(int teamID) throws IllegalArgumentException{
        if (teamID < 0) {
            throw new IllegalArgumentException("Invalid team ID");
        } else {
            this.teamID = teamID;
        }
    }

    public int value(){
        return teamID;
    }


}
