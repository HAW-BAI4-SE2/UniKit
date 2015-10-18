package controllers;

import database.common.interfaces.DatabaseConfiguration;
import database.haw_hamburg.implementations.ImportDatabaseManager;
import database.haw_hamburg.interfaces.Student;
import database.unikit.implementations.UnikitDatabaseManager;
import play.Play;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import java.io.InputStream;
import java.util.List;

import static database.common.implementations.DatabaseConfigurationUtils.createDatabaseConfiguration;

public class Application extends Controller {
    static {
        InputStream inputStreamImport = Play.application().resourceAsStream("hibernate_import.properties");
        DatabaseConfiguration databaseConfigurationImport = createDatabaseConfiguration(inputStreamImport);
        ImportDatabaseManager.init(databaseConfigurationImport);
        ImportDatabaseManager.cacheData();

        InputStream inputStreamUnikit = Play.application().resourceAsStream("hibernate_unikit.properties");
        DatabaseConfiguration databaseConfigurationUnikit = createDatabaseConfiguration(inputStreamUnikit);
        UnikitDatabaseManager.init(databaseConfigurationUnikit);
        UnikitDatabaseManager.cacheData();
    }

    public static Result index() {
        //Student currentUser = DatabaseManager.getCurrentUser();
        //String message = currentUser.toString();

        List<Student> students = ImportDatabaseManager.getAllStudents();
        String message = students.toString();
        return ok(index.render(message));
    }
}
