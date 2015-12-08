package models.commonUtils.Database;

import assets.Global;
import models.commonUtils.Exceptions.MembershipRequestNotFoundException;
import models.commonUtils.Exceptions.StudentNotFoundException;
import models.commonUtils.Exceptions.TeamNotFoundException;
import models.commonUtils.ID.StudentNumber;
import models.commonUtils.ID.TeamID;
import net.unikit.database.exceptions.EntityNotFoundException;
import net.unikit.database.interfaces.entities.Course;
import net.unikit.database.interfaces.entities.MembershipRequest;
import net.unikit.database.interfaces.entities.Student;
import net.unikit.database.interfaces.entities.Team;
import net.unikit.database.interfaces.managers.MembershipRequestManager;

/**
 * Handles the database interaction related to membership requests.
 * @author Thomas Bednorz
 */
class MembershipRequestDatabaseUtils {

    /**
     *
     * @param student
     * @param course
     */
    public static void deleteAllMembershipRequests(Student student, Course course) {
        MembershipRequestManager membershipRequestManager = Global.getMembershipRequestManager();

        for(MembershipRequest currentMembershipRequest : membershipRequestManager.getAllEntities()){
            try {
                if(currentMembershipRequest.getTeam().getCourse().getId().equals(course.getId()) &&
                        currentMembershipRequest.getApplicant().getStudentNumber().equals(student.getStudentNumber())){
                    try {
                        deleteMembershipRequest(student, currentMembershipRequest.getTeam());
                    } catch (Exception e) {
                        // Depending of the context of the call, it's expected that no membership requests exist
                    }
                }
            } catch (EntityNotFoundException e) {
                // TODO: Handle this internal database error (erroneous foreign keys)
                e.printStackTrace();
            }
        }
    }

    /**
     * Deletes a membership request for the student and the team
     * @param student the student who requested membership with the team
     * @param team the team that received the membership request
     * @throws MembershipRequestNotFoundException
     * @throws TeamNotFoundException
     */
    public static void deleteMembershipRequest(Student student, Team team) throws MembershipRequestNotFoundException {
        MembershipRequestManager membershipRequestManager = Global.getMembershipRequestManager();

        // Get membershiprequest to be deleted, throw error if not found
        MembershipRequest membershipRequestToBeDeleted = null;
        try {
            membershipRequestToBeDeleted = membershipRequestManager.getMembershipRequest(student,team);
            membershipRequestManager.deleteEntity(membershipRequestToBeDeleted);
        } catch (EntityNotFoundException e) {
            throw new MembershipRequestNotFoundException(StudentNumber.get(student.getStudentNumber()), TeamID.get(team.getId()));
        }
    }
}
