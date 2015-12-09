package models.commonUtils.Database;

import assets.Global;
import models.commonUtils.Exceptions.CourseNotFoundException;
import models.commonUtils.Exceptions.CourseRegistrationNotFoundException;
import models.commonUtils.Exceptions.StudentNotFoundException;
import models.commonUtils.ID.CourseID;
import models.commonUtils.ID.StudentNumber;
import net.unikit.database.exceptions.ConstraintViolationException;
import net.unikit.database.exceptions.EntityNotFoundException;
import net.unikit.database.interfaces.entities.Course;
import net.unikit.database.interfaces.entities.CourseRegistration;
import net.unikit.database.interfaces.entities.Student;
import net.unikit.database.interfaces.managers.CourseManager;
import net.unikit.database.interfaces.managers.CourseRegistrationManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the database interaction related to courses.
 * @author Thomas Bednorz
 */
class CourseDatabaseUtils {
    private static CourseManager courseManager;
    private static CourseRegistrationManager courseRegistrationManager;
    static{
        courseManager = Global.getCourseManager();
        courseRegistrationManager = Global.getCourseRegistrationManager();
    }

    /**
     *
     * @param cID
     * @return
     * @throws CourseNotFoundException
     */
    public static Course getCourse(CourseID cID) throws CourseNotFoundException {
        Course course = null;
        try {
            course = courseManager.getEntity(courseManager.createID(cID.value()));
        } catch (EntityNotFoundException e) {
            throw new CourseNotFoundException(cID);
        }

        return course;
    }

    /**
     *
     * @param student
     * @return
     */
    public static List<Course> getRegisteredCourses(Student student){
        List<Course> allRegisteredCourses = new ArrayList<>();

        try {
            for(CourseRegistration currentCourseRegistration : student.getCourseRegistrations()){
                allRegisteredCourses.add(currentCourseRegistration.getCourse());
            }
        } catch (EntityNotFoundException e) {
            // TODO: Handle this internal database error (erroneous foreign keys)
            e.printStackTrace();
        }

        return allRegisteredCourses;
    }

    /**
     *
     * @param student
     * @return
     */
    public static List<Course> getAvailableCourses(Student student){
        List<Course> allAvailableCourses = new ArrayList<>(courseManager.getAllEntities());
        allAvailableCourses.removeAll(student.getCompletedCourses());
        allAvailableCourses.removeAll(getRegisteredCourses(student));

        return allAvailableCourses;
    }

    public static void storeCourseRegistration(Student student, Course course){
        CourseRegistration newCourseRegistration = courseRegistrationManager.createEntity();
        newCourseRegistration.setCourse(course);
        newCourseRegistration.setStudent(student);
        newCourseRegistration.setCurrentlyAssignedToTeam(false);

        try {
            courseRegistrationManager.addEntity(newCourseRegistration);
        } catch (ConstraintViolationException e) {
            // NOTE: Registration is already in database!
            // TODO: Show error message in view?
            e.printStackTrace();
        }
    }

    public static void deleteCourseRegistration(Student student, Course course) throws StudentNotFoundException, CourseNotFoundException, CourseRegistrationNotFoundException {
        CourseRegistration courseRegistration = null;
        for(CourseRegistration currentCourseRegsitration : courseRegistrationManager.getAllEntities()){
            try {
                if(currentCourseRegsitration.getCourse().equals(course) &&
                        currentCourseRegsitration.getStudent().equals(student)){
                    courseRegistration = currentCourseRegsitration;
                }
            } catch (EntityNotFoundException e) {
                // TODO: Handle this internal database error (erroneous foreign keys)
                e.printStackTrace();
            }
        }

        if (courseRegistration == null)
            throw new CourseRegistrationNotFoundException(StudentNumber.get(student.getStudentNumber()),CourseID.get(course.getId()));

        try {
            courseRegistrationManager.deleteEntity(courseRegistration);
        } catch (EntityNotFoundException e) {
            // TODO (NOTE): Should never been thrown, because entry is directly taken out of database
            e.printStackTrace();
        }
    }
}
