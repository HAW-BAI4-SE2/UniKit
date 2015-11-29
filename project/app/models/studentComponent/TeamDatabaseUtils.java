package models.studentComponent;

/**
 * @author Thomas Bednorz
 */

import assets.Global;
import models.commonUtils.CommonDatabaseUtils;
import models.commonUtils.Exceptions.InvitationNotFoundException;
import models.commonUtils.Exceptions.TeamNotFoundException;
import models.commonUtils.ID.StudentNumber;
import models.commonUtils.ID.TeamID;
import net.unikit.database.external.interfaces.Course;
import net.unikit.database.external.interfaces.Student;
import net.unikit.database.unikit_.interfaces.*;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class TeamDatabaseUtils {

    public static void storeInvitation(StudentNumber sNumber, TeamID tID, Student currentUser) throws TeamNotFoundException {
        // Init
        String studentNumber = sNumber.value();

        //Get team for the invitation
        Team invitationForTeam = CommonDatabaseUtils.getTeamByID(tID);

        //Create new invitation
        TeamInvitation newInvitation = Global.getTeamInvitationManager().createTeamInvitation();
        newInvitation.setInviteeStudentNumber(studentNumber);
        newInvitation.setTeam(invitationForTeam);
        newInvitation.setCreatedByStudentNumber(currentUser.getStudentNumber());

        //Store invitation in database
        Global.getTeamInvitationManager().addTeamInvitation(newInvitation);
    }

    public static void deleteInvitation(StudentNumber sNumber, TeamID tID) throws InvitationNotFoundException {
        // Init
        String studentNumber = sNumber.value();
        int teamID = tID.value();

        // Get invitation
        TeamInvitation teamInvitation = null;
        List<TeamInvitation> allTeamInvitations = Global.getTeamInvitationManager().getAllTeamInvitations();
        for (TeamInvitation currentTeamInvitation : allTeamInvitations) {
            if (currentTeamInvitation.getInviteeStudentNumber().equals(studentNumber) && currentTeamInvitation.getTeam().getId().equals(teamID)) {
                teamInvitation = currentTeamInvitation;
                break;
            }
        }

        //Delete invitation from database
        if(teamInvitation == null){
            throw new InvitationNotFoundException(tID);
        } else {
            Global.getTeamInvitationManager().deleteTeamInvitation(teamInvitation);
        }
    }

    public static List<Student> getAllStudents(Team team) {

        //TODO: what if team has no registrations?

        //Get all registrations for team
        List<TeamRegistration> allRegistrationsForTeam = team.getTeamRegistrations();

        //Get all registered students
        List<Student> allStudentsInTeam = new ArrayList<>();
        for(TeamRegistration currentRegistration : allRegistrationsForTeam){
            allStudentsInTeam.add(Global.getStudentManager().getStudent(currentRegistration.getStudentNumber()));
        }

        checkNotNull(allStudentsInTeam);
        return allStudentsInTeam;
    }

    public static Team getTeamByID(int teamID) {
        return CommonDatabaseUtils.getTeamByID(teamID);
    }

    public static void addStudentToTeam(String studentNumber, int teamID) {
        CommonDatabaseUtils.addStudentToTeam(studentNumber, teamID);
        Team team = TeamDatabaseUtils.getTeamByID(teamID);
        CommonDatabaseUtils.changeTeamRegistrationStatus(studentNumber, team.getCourseId(), true);
    }

    public static void removeStudentFromTeam(String studentNumber, int teamID) {
        Team team = TeamDatabaseUtils.getTeamByID(teamID);
        CommonDatabaseUtils.changeTeamRegistrationStatus(studentNumber, team.getCourseId(), false);
        CommonDatabaseUtils.removeStudentFromTeam(studentNumber, teamID);
    }

    public static boolean teamCanInvite(int teamID) {
        return (!isTeamFull(teamID) && hasInvitationSlots(teamID));
    }

    private static boolean isTeamFull(int teamID) {
        Team currentTeam = CommonDatabaseUtils.getTeamByID(teamID);
        Course associatedCourse = CommonDatabaseUtils.getCourseByID(currentTeam.getCourseId());

        if(currentTeam.getTeamRegistrations().size() >= associatedCourse.getMaxTeamSize()){
            return true;
        }else{
            return false;
        }
    }

    private static boolean hasInvitationSlots(int teamID) {
        Team currentTeam = CommonDatabaseUtils.getTeamByID(teamID);
        Course associatedCourse = CommonDatabaseUtils.getCourseByID(currentTeam.getCourseId());

        if(currentTeam.getTeamInvitations().size() <= associatedCourse.getMaxTeamSize()){
            return true;
        }else{
            return false;
        }
    }

    public static void deleteMembershipRequest(String studentNumber, int teamID) {
        CommonDatabaseUtils.deleteMembershipRequest(studentNumber,teamID);
    }

    public static void deleteTeam(TeamID teamID) {
        CommonDatabaseUtils.deleteTeam(teamID);
    }

    public static boolean isMembershipRequested(String studentNumber, int teamID) {
        boolean isMembershipRequested = false;
        TeamApplicationManager membershipRequestManager = Global.getTeamApplicationManager();
        List<TeamApplication> allMembershipRequests = membershipRequestManager.getAllTeamApplications();

        for(TeamApplication currentMembershipRequest : allMembershipRequests){
            if(currentMembershipRequest.getApplicantStudentNumber().equals(studentNumber) && currentMembershipRequest.getTeam().getId().equals(teamID)){
                isMembershipRequested = true;
            }
        }
        return isMembershipRequested;
    }
}