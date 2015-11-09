package models.studentComponent;

/**
 * @author Thomas Bednorz
 */

import assets.Global;
import net.unikit.database.external.interfaces.Student;
import net.unikit.database.external.interfaces.StudentManager;
import net.unikit.database.unikit_.interfaces.Team;
import net.unikit.database.unikit_.interfaces.TeamApplication;
import net.unikit.database.unikit_.interfaces.TeamRegistration;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

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

        checkNotNull(teamForCourse);
        return teamForCourse;
    }

    public static List<Student> getAllStudents(Team teamToDispay) {
        List<Student> allStudentsInTeam = null;

        List<Team> allTeams =  Global.getTeamManager().getAllTeams();
        List<TeamRegistration> allRegistrationsForTeam = null;
        StudentManager studentManager = Global.getStudentManager();

        //Get all registrations for team
        for(Team currentTeam : allTeams){
            if(currentTeam.getId() == teamToDispay.getId()){
                allRegistrationsForTeam = currentTeam.getTeamRegistrations();
            }
        }

        //Get all registered students
         for(TeamRegistration currentRegistration : allRegistrationsForTeam){
            allStudentsInTeam.add(studentManager.getStudent(currentRegistration.getStudentNumber()));
         }

        checkNotNull(allStudentsInTeam);

        return allStudentsInTeam;
    }

}
