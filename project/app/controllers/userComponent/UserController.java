package controllers.userComponent;

import assets.Global;
import assets.Utils;
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
        Student currentUser = Utils.getCurrentUser(session());
        Date sessionTimeout = Utils.getSessionTimeout(session());

        return ok(showUser.render(currentUser, sessionTimeout));
    }
}
