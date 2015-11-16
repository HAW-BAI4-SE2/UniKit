package models.commonUtils.ID;

/**
 * @author Thomas Bednorz
 */
public class TeamID {
    private int teamID;

    public static TeamID get(int teamID) throws IllegalArgumentException{
        return new TeamID(teamID);
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
