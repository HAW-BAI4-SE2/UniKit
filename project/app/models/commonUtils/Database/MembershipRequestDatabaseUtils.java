package models.commonUtils.Database;

import assets.Global;
import models.commonUtils.Exceptions.*;
import models.commonUtils.ID.CourseID;
import models.commonUtils.ID.StudentNumber;
import models.commonUtils.ID.TeamID;
import net.unikit.database.exceptions.ConstraintViolationException;
import net.unikit.database.exceptions.EntityNotFoundException;
import net.unikit.database.interfaces.entities.Course;
import net.unikit.database.interfaces.entities.MembershipRequest;
import net.unikit.database.interfaces.entities.Student;
import net.unikit.database.interfaces.entities.Team;
import net.unikit.database.interfaces.managers.MembershipRequestManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the database interaction related to membership requests.
 * @author Thomas Bednorz
 */
class MembershipRequestDatabaseUtils {
    private static MembershipRequestManager membershipRequestManager;
    static {
        membershipRequestManager = Global.getMembershipRequestManager();
    }

    /**
     *
     * @param student
     * @param course
     */
    public static void deleteAllMembershipRequests(Student student, Course course) {

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
        // Get membershiprequest to be deleted, throw error if not found
        MembershipRequest membershipRequestToBeDeleted = null;
        try {
            membershipRequestToBeDeleted = membershipRequestManager.getMembershipRequest(student,team);
            membershipRequestManager.deleteEntity(membershipRequestToBeDeleted);
        } catch (EntityNotFoundException e) {
            throw new MembershipRequestNotFoundException(StudentNumber.get(student.getStudentNumber()), TeamID.get(team.getId()));
        }
    }

    public static void deleteAllMembershipRequests(Team team) {
        for(MembershipRequest currentMembershipRequest : membershipRequestManager.getAllEntities()){
            if(currentMembershipRequest.getTeam().equals(team)){
                try {
                    deleteAllMembershipRequests(currentMembershipRequest.getApplicant(),team.getCourse());
                } catch (EntityNotFoundException e) {
                    // Student for membership request not found
                    // Course for membership request not found
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Stores the new Membershiprequest n the Database
     * @param student the student number of the student who requested membership with the team
     * @param team the ID of the team that received the membership request
     * @throws TeamNotFoundException
     * */
    public static void storeMembershipRequest(Student student, Team team) throws MembershipRequestExistsException {
        // Create membership request
        MembershipRequest newMembershipRequest = membershipRequestManager.createEntity();
        newMembershipRequest.setApplicant(student);
        newMembershipRequest.setTeam(team);
        try {
            membershipRequestManager.addEntity(newMembershipRequest);
        } catch (ConstraintViolationException e) {
            throw new MembershipRequestExistsException(StudentNumber.get(student.getStudentNumber()),TeamID.get(team.getId()));
        }
    }

    /**
     *
     * @param team
     * @return
     */
    public static List<Student> getAllMembershipRequests(Team team) {
        List<Student> allRequestingStudents = new ArrayList<>();

        for(MembershipRequest currentMembershipRequest : membershipRequestManager.getAllEntities()){
            if(currentMembershipRequest.getTeam().equals(team)){
                try {
                    allRequestingStudents.add(currentMembershipRequest.getApplicant());
                } catch (EntityNotFoundException e) {
                    // TODO: Handle this internal database error (erroneous foreign keys)
                    e.printStackTrace();
                }
            }
        }
        return allRequestingStudents;
    }

    /**
     *
     * @param student
     * @return
     */
    public static List<MembershipRequest> getAllMembershipRequests(Student student){
        List<MembershipRequest> allMembershipRequests = new ArrayList<>();
        for(MembershipRequest currentMembershipRequest : membershipRequestManager.getAllEntities()){
            try {
                if(currentMembershipRequest.getApplicant().equals(student)){
                    allMembershipRequests.add(currentMembershipRequest);
                }
            } catch (EntityNotFoundException e) {
                // Any student couldn't be found in membership requests
                // TODO: Handle this internal database error (erroneous foreign keys)
                e.printStackTrace();
            }
        }

        return allMembershipRequests;
    }

    /**
     *
     * @param student
     * @param course
     * @return
     * @throws StudentNotFoundException
     * @throws CourseNotFoundException
     */
    public static List<MembershipRequest> getAllMembershipRequests(Student student, Course course) throws StudentNotFoundException, CourseNotFoundException {
        List<MembershipRequest> allMembershipRequests = new ArrayList<>();

        for(MembershipRequest currentMembershipRequest : membershipRequestManager.getAllEntities()){
            try {
                if(currentMembershipRequest.getApplicant().equals(student) &&
                        currentMembershipRequest.getTeam().getCourse().equals(course)){
                    allMembershipRequests.add(currentMembershipRequest);
                }
            } catch (EntityNotFoundException e) {
                // Any student couldn't be found in membership requests
                // Any course couldn't be found in membership requests
                // TODO: Handle this internal database error (erroneous foreign keys)
                e.printStackTrace();
            }
        }

        return allMembershipRequests;
    }

    public static boolean isMembershipRequested(Student student, Team team) {
        for(MembershipRequest currentMembershipRequest : membershipRequestManager.getAllEntities()){
            try {
                if(currentMembershipRequest.getApplicant().equals(student) && currentMembershipRequest.getTeam().equals(team)){
                    return true;
                }
            } catch (EntityNotFoundException e) {
                // Some student couldn't be found
                e.printStackTrace();
            }
        }
        return false;
    }
}
