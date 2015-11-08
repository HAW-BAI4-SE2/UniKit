package models.loginComponent;

import assets.BCrypt;
import assets.Global;
import net.unikit.database.external.interfaces.Student;
import play.data.validation.Constraints;
import play.data.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

/**
 * Model for the login form
 * @author Andreas Berks
 */
public class LoginFormModel {
    // Username of the student who wants to login
    public String username;

    // Password of the student who wants to login
    public String password;

    // CREATION
    public LoginFormModel() {
    }

    public LoginFormModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // VALIDATION
    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<ValidationError>();

        // Check if username is missing
        if (username == null || username.isEmpty())
            errors.add(new ValidationError("", "Username wurde nicht angegeben!"));

        // Check if password is missing
        if (password == null || password.isEmpty())
            errors.add(new ValidationError("", "Password wurde nicht angegeben!"));

        // If errors occurred, return them
        if (!errors.isEmpty())
            return errors;



        // Check if username is unknown
        Student currentUser = Global.getStudentManager().getStudent(username);
        boolean usernameUnknown = (currentUser == null);

        // Check if password is wrong
        String hashed = BCrypt.hashpw("test", BCrypt.gensalt()); // TODO: Load password from DB!
        boolean passwordWrong = !BCrypt.checkpw(password, hashed);

        // Return error if username is unknown or password is wrong
        if (usernameUnknown || passwordWrong)
            errors.add(new ValidationError("", "Login fuer User '" + username + "' ist fehlgeschlagen!"));

        return errors.isEmpty() ? null : errors;
    }
}
