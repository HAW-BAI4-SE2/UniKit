package models.studentComponent;

/**
 * @author Thomas Bednorz
 */

import assets.Global;
import models.UnikitDatabaseUtils;
import net.unikit.database.external.interfaces.Course;
import net.unikit.database.external.interfaces.Student;
import net.unikit.database.external.interfaces.StudentManager;
import net.unikit.database.unikit_.interfaces.*;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class TeamDatabaseUtils {

//    public static Team getTeam(String studentNumber, int courseID) throws NullPointerException{
//        //Get all registered teams
//        List<Team> allTeams = Global.getTeamManager().getAllTeams();
//        List<Team> allTeamsForCourse = new ArrayList<Team>();
//        List<TeamRegistration> registrationsInTeam;
//        Team teamForCourse = null;
//
//        // Get all teams for this course
//        for(Team currentTeam : allTeams){
//            if(currentTeam.getCourseId() == courseID){
//                allTeamsForCourse.add(currentTeam);
//            }
//        }
//
//        // Get all registrations in the team
//        for(Team currentTeam : allTeamsForCourse){
//            registrationsInTeam = currentTeam.getTeamRegistrations();
//
//            // Get the team for which the student is a member
//            for(TeamRegistration teamRegistration : registrationsInTeam){
//                if(teamRegistration.getStudentNumber().equals(studentNumber)){
//                    teamForCourse = currentTeam;
//                }
//            }
//        }
//
//        checkNotNull(teamForCourse);
//        return teamForCourse;
//    }





    public static void storeInvitation(String studentNumber, int teamID, Student currentUser) {
        TeamInvitationManager invitationManager = Global.getTeamInvitationManager();
        TeamInvitation newInvitation = invitationManager.createTeamInvitation();

        //Gets the team for the invitation
        Team invitationForTeam = null;
        invitationForTeam = UnikitDatabaseUtils.getTeamByID(teamID);
        checkNotNull(invitationForTeam);

        //Fill new invitation
        newInvitation.setInviteeStudentNumber(studentNumber);
        newInvitation.setTeam(invitationForTeam);
        newInvitation.setCreatedByStudentNumber(currentUser.getStudentNumber());

        //Store invitation in database
        invitationManager.addTeamInvitation(newInvitation);
    }

    public static void deleteInvitation(String studentNumber, int teamID, Student currentUser) {
        TeamInvitationManager invitationManager = Global.getTeamInvitationManager();
        TeamInvitation deletedInvitation = invitationManager.createTeamInvitation();

        //Gets the team for the invitation
        Team invitationForTeam = null;
        invitationForTeam = UnikitDatabaseUtils.getTeamByID(teamID);
        checkNotNull(invitationForTeam);

        //Fill new invitation
        deletedInvitation.setInviteeStudentNumber(studentNumber);
        deletedInvitation.setTeam(invitationForTeam);
        deletedInvitation.setCreatedByStudentNumber(currentUser.getStudentNumber());

        //Delete invitation from database
        invitationManager.deleteTeamInvitation(deletedInvitation);
    }

    public static List<Student> getAllStudents(Team team) {

        //TODO: what if team has no registrations?

        List<Student> allStudentsInTeam = new ArrayList<>();

        List<Team> allTeams =  Global.getTeamManager().getAllTeams();
        List<TeamRegistration> allRegistrationsForTeam = null;
        StudentManager studentManager = Global.getStudentManager();

        //Get all registrations for team
        for(Team currentTeam : allTeams){
            if(currentTeam.getId() == team.getId()){
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

    public static Team getTeamByID(int teamID) {
        return UnikitDatabaseUtils.getTeamByID(teamID);
    }

    public static void addStudentToTeam(String studentNumber, int teamID) {
        UnikitDatabaseUtils.addStudentToTeam(studentNumber,teamID);
    }

    public static void removeStudentFromTeam(String studentNumber, int teamID) {
        UnikitDatabaseUtils.removeStudentFromTeam(studentNumber,teamID);
    }

    public static boolean isTeamFull(int teamID) {
        Team currentTeam = UnikitDatabaseUtils.getTeamByID(teamID);
        Course associatedCourse = UnikitDatabaseUtils.getCourseByID(currentTeam.getCourseId());

        if(currentTeam.getTeamRegistrations().size() >= associatedCourse.getMaxTeamSize()){
            return true;
        }else{
            return false;
        }
    }

    public static boolean hasInvitationSlots(int teamID) {
        Team currentTeam = UnikitDatabaseUtils.getTeamByID(teamID);
        Course associatedCourse = UnikitDatabaseUtils.getCourseByID(currentTeam.getCourseId());

        if(currentTeam.getTeamInvitations().size() <= associatedCourse.getMaxTeamSize()){
            return true;
        }else{
            return false;
        }
    }
}
