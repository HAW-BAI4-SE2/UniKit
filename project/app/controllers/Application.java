package controllers;

import export.interfaces.DatabaseConfiguration;
import haw_hamburg.database.implementations.DatabaseManager;
import haw_hamburg.database.interfaces.Student;
import models.implementations.DatabaseConfigurationImpl;
import play.Play;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import java.io.InputStream;
import java.util.Collection;

public class Application extends Controller {
    static {
        InputStream inputStreamImport = Play.application().resourceAsStream("hibernate_import.properties");
        DatabaseConfiguration databaseConfigurationImport = DatabaseConfigurationImpl.create(inputStreamImport);
        haw_hamburg.database.implementations.DatabaseManager.init(databaseConfigurationImport);
        haw_hamburg.database.implementations.DatabaseManager.cacheData();

        InputStream inputStreamUnikit = Play.application().resourceAsStream("hibernate_unikit.properties");
        DatabaseConfiguration databaseConfigurationUnikit = DatabaseConfigurationImpl.create(inputStreamUnikit);
        unikit.database.implementations.DatabaseManager.init(databaseConfigurationUnikit);
        unikit.database.implementations.DatabaseManager.cacheData();
    }

    public static Result index() {
        //Student currentUser = DatabaseManager.getCurrentUser();
        //String message = currentUser.toString();

        Collection<Student> students = DatabaseManager.getStudents();
        String message = students.toString();
        return ok(index.render(message));
    }
}
