package controllers.userComponent;

import assets.Global;
import net.unikit.database.external.interfaces.Student;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

import java.util.Date;

/**
 * Created by Andreas on 05.11.2015.
 */
public class UserController extends Controller {
    public static Result showUser() {
        Student currentUser = Global.getStudentManager().getStudent("2055120");
        
        Date sessionTimeoutDate = new Date();
        java.util.Calendar sessionTimeout = java.util.Calendar.getInstance();
        sessionTimeout.setTime(sessionTimeoutDate);

        return ok(showUser.render(currentUser, sessionTimeout));
    }
}
