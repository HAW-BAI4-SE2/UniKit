package models.studentComponent;

/**
 * @author Thomas Bednorz
 */

import assets.Global;
import net.unikit.database.external.interfaces.Student;
import net.unikit.database.unikit_.interfaces.Team;
import net.unikit.database.unikit_.interfaces.TeamRegistration;

import java.util.ArrayList;
import java.util.List;

public class TeamDatabaseConnector {

    public static Team getTeam(String studentNumber, int courseID) throws NullPointerException{
        //Get all registered teams
        List<Team> allTeams = Global.getTeamManager().getAllTeams();
        List<Team> allTeamsForCourse = new ArrayList<Team>();
        List<TeamRegistration> registrationsInTeam;
        Team teamForCourse = null;

        // Get all teams for this course
        for(Team currentTeam : allTeams){
            if(currentTeam.getCourseId() == courseID){
                allTeamsForCourse.add(currentTeam);
            }
        }

        // Get all registrations in the team
        for(Team currentTeam : allTeamsForCourse){
            registrationsInTeam = currentTeam.getTeamRegistrations();

            // Get the team for which the student is a member
            for(TeamRegistration teamRegistration : registrationsInTeam){
                if(teamRegistration.getStudentNumber().equals(studentNumber)){
                    teamForCourse = currentTeam;
                }
            }
        }
        if(teamForCourse == null) throw new NullPointerException();
        return teamForCourse;
    }
}
