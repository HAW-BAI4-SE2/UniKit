package controllers.userComponent;

import models.loginComponent.LoginFormModel;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

/**
 * Created by Andreas on 05.11.2015.
 */
public class UserController extends Controller {
    public static Result showUser() {
        LoginFormModel loginFormModel = new LoginFormModel("", "");
        Form<LoginFormModel> loginForm = Form.form(LoginFormModel.class).fill(loginFormModel);

        return ok(showUser.render(loginForm));
    }
}
