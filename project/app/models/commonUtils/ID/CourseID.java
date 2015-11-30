package models.commonUtils.ID;

import net.unikit.database.interfaces.entities.Course;

/**
 * @author Thomas Bednorz
 */
public class CourseID {
    int courseID;

    public static CourseID get(int courseID) throws IllegalArgumentException{
        return new CourseID(courseID);
    }

    public static CourseID get(Course.ID courseID){
        return new CourseID(courseID.getValue());
    }

    private CourseID() {}

    private CourseID(int courseID) throws IllegalArgumentException{
        if (courseID < 0) {
            throw new IllegalArgumentException("Invalid course ID");
        } else {
            this.courseID = courseID;
        }
    }

    public int value(){
        return courseID;
    }
}
