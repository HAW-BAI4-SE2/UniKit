package controllers;

import assets.Global;
import net.unikit.database.external.interfaces.Student;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.testPage;

import java.util.List;

public class TestPage extends Controller {
    public static Result index() {
        //Student currentUser = UnikitDatabaseManager.getCurrentUser();
        //String message = currentUser.toString();

        List<Student> students = Global.getStudentManager().getAllStudents();
        String message = students.toString();
        return ok(testPage.render(message));
    }
}
