package models.imports.implementations;

import models.imports.implementations.hibernate.CourseModel;
import models.imports.interfaces.Course;

/**
 * An Implementation of university Courses.
 * Author: Jonas Johannsen, 10.10.2015
 */
class CourseImpl implements Course {
    private CourseModel course;

    /**
     *
     * @param courseModel The underlying model
     */
    public CourseImpl(CourseModel courseModel){
        course = courseModel;
    }

    @Override
    /**
     * Returns the course-ID.
     */
    public int getId() {
        return course.getId();
    }

    @Override
    /**
     * Returns the courses shortcut.
     */
    public String getShortcut() {
        return course.getShortcut();
    }

    @Override
    /**
     * Returns the courses name.
     */
    public String getName() {
        return course.getName();
    }

    @Override
    /**
     * Returns the minimal teamsize that is allowed for teams.
     */
    public int getMinTeamSize() {
        return course.getMinTeamSize();
    }

    @Override
    /**
     * Returns the maximal teamsize that is allowed for teams.
     */
    public int getMaxTeamSize() {
        return course.getMaxTeamSize();
    }
}
