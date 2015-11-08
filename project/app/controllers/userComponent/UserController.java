package controllers.userComponent;

import assets.SessionUtils;
import net.unikit.database.external.interfaces.Student;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Andreas on 05.11.2015.
 */
public class UserController extends Controller {
    public static Result showUser() {
        Student currentUser = SessionUtils.getCurrentUser(session());
        Date sessionTimeout = SessionUtils.getSessionTimeout(session());

        return ok(showUser.render(currentUser, sessionTimeout));
    }

    public static Result showResults() {
        Student currentUser = SessionUtils.getCurrentUser(session());
        Date sessionTimeout = SessionUtils.getSessionTimeout(session());

        return ok(showResults.render(currentUser, sessionTimeout));
    }
}
