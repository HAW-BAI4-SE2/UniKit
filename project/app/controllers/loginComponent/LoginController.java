package controllers.loginComponent;

import assets.Global;
import controllers.routes;
import models.loginComponent.LoginFormModel;
import net.unikit.database.external.interfaces.Student;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import views.html.showLogin;
import views.html.test;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by abq308 on 05.11.2015.
 */
public class LoginController extends Controller {
    public static Result showLogin() {
        // Generate input form
        LoginFormModel loginFormModel = new LoginFormModel("2055120", "");
        Form<LoginFormModel> loginForm = Form.form(LoginFormModel.class).fill(loginFormModel);

        return ok(showLogin.render(loginForm));
    }

    public static Result login() {
        // Get form with data from request
        Form<LoginFormModel> loginForm = Form.form(LoginFormModel.class).bindFromRequest();

        // If errors occurred, redirect to view and show the errors
        if (loginForm.hasErrors()) {
            return badRequest(showLogin.render(loginForm));
        }

        // Get form model
        LoginFormModel loginFormModel = loginForm.get();

        // Clear previous session data
        session().clear();

        // Store unique id in session
        String uuid = java.util.UUID.randomUUID().toString();
        session("uuid", uuid);

        // Store username in session
        session("username", loginFormModel.username);

        // Go to user overview page
        return redirect(controllers.userComponent.routes.UserController.showUser());
    }

    public static Result logout() {
        // Clear session data
        session().clear();

        // Show result message
        flash("Auf Wiedersehen!", "Sie wurden erfolgreich ausgeloggt!");

        // Go to login page
        return redirect(controllers.loginComponent.routes.LoginController.showLogin());
    }
}
