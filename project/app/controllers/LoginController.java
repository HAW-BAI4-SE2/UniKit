package controllers;

import assets.Global;
import models.loginComponent.LoginFormModel;
import net.unikit.database.external.interfaces.Student;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
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
        // Load input form
        Form<LoginFormModel> loginForm = Form.form(LoginFormModel.class).bindFromRequest();
        LoginFormModel loginFormModel = loginForm.get();

        // Check if username exists
        String username = loginForm.get().username;
        Student currentUser = Global.getStudentManager().getStudent(username);
        checkNotNull(currentUser, "Unknown username '" + username + "'!");

        // Check if password is correct
        // TODO!

        // If errors occured, show errors
        if (loginForm.hasErrors()) {
            // return badRequest(login.render(loginForm));
            return badRequest(test.render(loginForm.toString()));
        }

        // Clear previous session data
        session().clear();

        // Store username in session
        session("username", username);

        // Go to user overview page
        return redirect(routes.UserController.showUser());
    }

    public static Result logout() {
        // Clear session data
        session().clear();

        // Show result message
        flash("Auf Wiedersehen!", "Sie wurden erfolgreich ausgeloggt!");

        // Go to login page
        return redirect(routes.LoginController.showLogin());
    }
}
