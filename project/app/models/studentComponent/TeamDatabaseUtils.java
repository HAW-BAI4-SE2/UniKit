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

    public static void deleteInvitation(String studentNumber, int teamID) {
        TeamInvitation teamInvitation = null;

        // Get invitation
        List<TeamInvitation> allTeamInvitations = Global.getTeamInvitationManager().getAllTeamInvitations();
        for (TeamInvitation currentTeamInvitation : allTeamInvitations) {
            if (currentTeamInvitation.getInviteeStudentNumber().equals(studentNumber) && currentTeamInvitation.getTeam().getId().equals(teamID)) {
                teamInvitation = currentTeamInvitation;
                break;
            }
        }

        //Delete invitation from database
        checkNotNull(teamInvitation);
        Global.getTeamInvitationManager().deleteTeamInvitation(teamInvitation);
    }

    public static List<Student> getAllStudents(Team team) {

        //TODO: what if team has no registrations?

        List<Student> allStudentsInTeam = new ArrayList<>();

        List<Team> allTeams =  Global.getTeamManager().getAllTeams();
        List<TeamRegistration> allRegistrationsForTeam = null;
        StudentManager studentManager = Global.getStudentManager();

        //Get all registrations for team
        for(Team currentTeam : allTeams){
            if(currentTeam.getId().equals(team.getId())){
                allRegistrationsForTeam = currentTeam.getTeamRegistrations();
                break;
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
        UnikitDatabaseUtils.changeTeamRegistrationStatus(studentNumber,teamID,true);
    }

    public static void removeStudentFromTeam(String studentNumber, int teamID) {
        UnikitDatabaseUtils.removeStudentFromTeam(studentNumber,teamID);
        UnikitDatabaseUtils.changeTeamRegistrationStatus(studentNumber,teamID,false);
    }

    public static boolean teamCanInvite(int teamID) {
        return (!isTeamFull(teamID) && hasInvitationSlots(teamID));
    }

    private static boolean isTeamFull(int teamID) {
        Team currentTeam = UnikitDatabaseUtils.getTeamByID(teamID);
        Course associatedCourse = UnikitDatabaseUtils.getCourseByID(currentTeam.getCourseId());

        if(currentTeam.getTeamRegistrations().size() >= associatedCourse.getMaxTeamSize()){
            return true;
        }else{
            return false;
        }
    }

    private static boolean hasInvitationSlots(int teamID) {
        Team currentTeam = UnikitDatabaseUtils.getTeamByID(teamID);
        Course associatedCourse = UnikitDatabaseUtils.getCourseByID(currentTeam.getCourseId());

        if(currentTeam.getTeamInvitations().size() <= associatedCourse.getMaxTeamSize()){
            return true;
        }else{
            return false;
        }
    }

    public static void deleteMembershipRequest(String studentNumber, int teamID) {
        checkNotNull(studentNumber);
        TeamApplicationManager membershipRequestManager = Global.getTeamApplicationManager();

        List<TeamApplication> allMembershipRequests = membershipRequestManager.getAllTeamApplications();

        //Get membership request to be deleted
        TeamApplication membershipRequestToBeDeleted = null;
        for(TeamApplication currentMembershipRequest : allMembershipRequests){
            if(currentMembershipRequest.getTeam().getId().equals(teamID) &&
                    currentMembershipRequest.getApplicantStudentNumber().equals(studentNumber)){
                        membershipRequestToBeDeleted = currentMembershipRequest;
                        break;
            }
        }

        //Delete membership request from database
        checkNotNull(membershipRequestToBeDeleted);
        membershipRequestManager.deleteTeamApplication(membershipRequestToBeDeleted);
    }

    public static void deleteTeam(int teamID) {
        UnikitDatabaseUtils.deleteTeam(teamID);
    }
}