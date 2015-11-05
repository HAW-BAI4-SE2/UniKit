package controllers;

import assets.Global;
import controllers.userComponent.UserController;
import models.loginComponent.LoginFormModel;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

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

        // TODO: Implement username check
        // ...

        // TODO: Implement password check
        // ...

        // If errors occured, show errors
        if (loginForm.hasErrors()) {
            // return badRequest(login.render(loginForm));
            return badRequest(test.render(loginForm.toString()));
        }

        // Clear previous session data
        session().clear();

        // Store username in session
        String username = loginForm.get().username;
        session("username", username);

        // Go to user overview page
        return redirect("user");
    }

    public static Result logout() {
        // Clear session data
        session().clear();

        // Show message box
        flash("Auf Wiedersehen!", "Sie wurden erfolgreich ausgeloggt!");

        // Go to login page
        return redirect("login");
    }
}
