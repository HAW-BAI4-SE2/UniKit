package models.login.loginComponent;

/**
 * Model for the login form
 * @author Andreas Berks
 */
public class LoginFormModel {
    // Username of the student who wants to login
    public String username;
    // Password of the student who wants to login
    public String password;

    public LoginFormModel() {
    }

    public LoginFormModel(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
