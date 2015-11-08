package controllers.userComponent;

import assets.SessionUtils;
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
        Student currentUser = SessionUtils.getCurrentUser(session());
        Date sessionTimeout = SessionUtils.getSessionTimeout(session());

        return ok(showUser.render(currentUser, sessionTimeout));
    }

    public static Result showResults() {
        Student currentUser = SessionUtils.getCurrentUser(session());
        Date sessionTimeout = SessionUtils.getSessionTimeout(session());

        return ok(showResults.render(currentUser, sessionTimeout));
    }

    public static Result showMailingLists() {
        Student currentUser = SessionUtils.getCurrentUser(session());
        Date sessionTimeout = SessionUtils.getSessionTimeout(session());

        return ok(showMailingLists.render(currentUser, sessionTimeout));
    }

    public static Result showPrintHistory() {
        Student currentUser = SessionUtils.getCurrentUser(session());
        Date sessionTimeout = SessionUtils.getSessionTimeout(session());

        return ok(showPrintHistory.render(currentUser, sessionTimeout));
    }

    public static Result showPrintCreditsHistory() {
        Student currentUser = SessionUtils.getCurrentUser(session());
        Date sessionTimeout = SessionUtils.getSessionTimeout(session());

        return ok(showPrintCreditsHistory.render(currentUser, sessionTimeout));
    }

    public static Result showPrintCredits() {
        Student currentUser = SessionUtils.getCurrentUser(session());
        Date sessionTimeout = SessionUtils.getSessionTimeout(session());

        return ok(showPrintCredits.render(currentUser, sessionTimeout));
    }
}
