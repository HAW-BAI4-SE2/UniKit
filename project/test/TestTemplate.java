/**
 * WIP test template for structuring Unikit testing
 * @author Thomas Bednorz
 */

import static org.junit.Assert.*;
import static play.mvc.Http.Context.Implicit.session;

import assets.SessionUtils;
import controllers.studentComponent.TeamController;
import net.unikit.database.external.interfaces.Student;
import net.unikit.database.unikit_.interfaces.Team;
import net.unikit.database.unikit_.interfaces.TeamInvitation;

import org.junit.*;

public class TestTemplate {

    @Before
    public void resetDabase(){
        //TODO: reset database to original state
    }

    public void setActingStudent(String username){
        SessionUtils.initSession(session(),username);
    }

    @Test
    /**
     * Tests if a student was successfully invited into a team.
     * Context:
     *  Student1 is registered for course RN
     *  Student2 is registered for course RN
     *  Student1 is member of Team1
     *  Student2 not associated with any team
     *
     */
    public void testInviteStudent(){
        // Set Team1 to a team associated with course RN
        Team team1 = null;

        // Set Student1 to a student associated with Team1
        Student student1 = null;

        // Set Student2 to a student with no association to a team for 	course RN, student must be registered for course RN
        Student student2 = null;

        // Student1 is acting Student
        setActingStudent(student1.getStudentNumber());

        // Assert that no invitation exists for Student2 and Team1
        TeamInvitation invitation = null;
        //TODO: Get invitation for Student2 and Team1 (might throw NullPointerException)
        assertNull(invitation);

        // Student1 invites Student2 to Team1
        TeamController.inviteStudent(student2.getStudentNumber(),team1.getId());

        // Assert invitation exists in Database
        //TODO: Get invitation for Student2 and Team1
        assertNotNull(invitation);
    }
}
