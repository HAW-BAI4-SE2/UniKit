package assets;

import net.unikit.database.external.interfaces.Student;
import play.mvc.Http;

import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Andreas on 06.11.2015.
 */
public final class SessionUtils {
    public static Student getCurrentUser(Http.Session session) {
        String username = session.get("username");
        checkNotNull(username, "username is null!");

        Student currentUser = Global.getStudentManager().getStudent(username);
        checkNotNull(currentUser, "currentUser is null!");

        return currentUser;
    }

    public static Date getSessionTimeout(Http.Session session) {
        String uuid = session.get("uuid");

        Date sessionTimeout = new Date();
        checkNotNull(sessionTimeout, "sessionTimeout is null!");

        return sessionTimeout;
    }
}
