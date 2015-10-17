package controllers;

import haw_hamburg.database.implementations.DatabaseManager;
import haw_hamburg.database.interfaces.Student;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import java.util.Collection;

public class Application extends Controller {
    static {
        haw_hamburg.database.implementations.DatabaseManager.init();
        haw_hamburg.database.implementations.DatabaseManager.cacheData();

        unikit.database.implementations.DatabaseManager.init();
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
