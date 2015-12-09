package models.commonUtils.Database;

import assets.Global;
import models.commonUtils.Exceptions.FatalErrorException;
import models.commonUtils.Exceptions.StudentNotFoundException;
import models.commonUtils.ID.CourseID;
import models.commonUtils.ID.StudentNumber;
import net.unikit.database.exceptions.EntityNotFoundException;
import net.unikit.database.interfaces.entities.*;
import net.unikit.database.interfaces.managers.CourseRegistrationManager;
import net.unikit.database.interfaces.managers.StudentManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the database interaction related to students.
 * @author Thomas Bednorz
 */
class StudentDatabaseUtils {
    /**
     *
     * @param sNumber
     * @return
     * @throws StudentNotFoundException
     */
    public static Student getStudent(StudentNumber sNumber) throws StudentNotFoundException {
        StudentManager studentManager = Global.getStudentManager();
        Student student = null;
        try {
            student = studentManager.getEntity(studentManager.createID(sNumber.value()));
        } catch (EntityNotFoundException e) {
            throw new StudentNotFoundException(sNumber);
        }

        return student;
    }

    /**
     *
     * @param team
     * @return
     */
    public static List<Student> getAllStudents(Team team) {

        //Get all registered students
        List<Student> allStudentsInTeam = new ArrayList<>();
        for(TeamRegistration currentRegistration : team.getTeamRegistrations()){
            try {
                allStudentsInTeam.add(currentRegistration.getStudent());
            } catch (EntityNotFoundException e) {
                // Student for registration not found
                e.printStackTrace();
            }
        }

        return allStudentsInTeam;
    }

    /**
     *
     * @param associatedCourse
     * @return
     */
    public static List<Student> getAllStudents(Course associatedCourse) {
        CourseRegistrationManager courseRegistrationManager = Global.getCourseRegistrationManager();

        List<Student> allStudentsInCourse = new ArrayList<>();
        for(CourseRegistration currentCourseRegistration : courseRegistrationManager.getAllEntities()){
            try {
                if(currentCourseRegistration.getCourse().equals(associatedCourse)){
                    allStudentsInCourse.add(currentCourseRegistration.getStudent());
                }
            } catch (EntityNotFoundException e) {
                //TODO Error handling
                // Course for registration not found
                e.printStackTrace();
            }
        }

        return allStudentsInCourse;

    }

    /**
     * Changes the registration status of a student for a specified course
     * @param student the student for which the registration status is to be changed
     * @param course the course for which the registration status is to be changed
     * @param status true if the student is in a team for the course, else false
     */
    public static void changeTeamRegistrationStatus(Student student, Course course, boolean status) throws FatalErrorException {
        CourseRegistrationManager courseRegistrationManager = Global.getCourseRegistrationManager();

        try{
            for(CourseRegistration courseRegistration : courseRegistrationManager.getAllEntities()){
                if(courseRegistration.getCourse().getId().equals(course.getId()) &&
                        courseRegistration.getStudent().getStudentNumber().equals(student.getStudentNumber())){
                    courseRegistration.setCurrentlyAssignedToTeam(status);
                    break;
                }
            }

    } catch (EntityNotFoundException e) {
            throw new FatalErrorException("Student or course not found");
        }
    }
}
