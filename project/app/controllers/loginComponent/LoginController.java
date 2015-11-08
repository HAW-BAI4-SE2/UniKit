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

import static assets.SessionUtils.destroySession;
import static assets.SessionUtils.initSession;
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

        // Init session data
        initSession(session(), loginFormModel.username);

        // Go to user overview page
        return redirect(controllers.userComponent.routes.UserController.showUser());
    }

    public static Result logout() {
        // Destroy session data
        destroySession(session());

        // Go to login page
        return redirect(controllers.loginComponent.routes.LoginController.showLogin());
    }
}
