package controllers.loginComponent;

import models.login.loginComponent.LoginFormModel;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

/**
 * Created by abq308 on 05.11.2015.
 */
public class LoginController extends Controller {
    public static Result showLogin() {
        LoginFormModel loginFormModel = new LoginFormModel("", "");
        Form<LoginFormModel> loginForm = Form.form(LoginFormModel.class).fill(loginFormModel);

        return ok(showLogin.render(loginForm));
    }

    public static Result login() {
        Form<LoginFormModel> loginForm = Form.form(LoginFormModel.class).bindFromRequest();
        LoginFormModel loginFormModel = loginForm.get();

        String studentNumber = loginFormModel.username;
        studentNumber = studentNumber + loginFormModel.password;
        return ok(test.render(studentNumber));
    }
}
