package controllers;

import models.implementations.DatabaseManager;
import models.interfaces.Student;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class Application extends Controller {

    static {
        DatabaseManager.initImportData();
        DatabaseManager.cacheImportData();

        DatabaseManager.initUnikitData();
        DatabaseManager.cacheUnikitData();
    }

    public static Result index() {
        Student currentUser = DatabaseManager.getCurrentUser();
        String message = currentUser.toString();
        return ok(index.render(message));
    }

}
