package assets;

import net.unikit.database.external.interfaces.Student;
import play.mvc.Http;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Andreas on 06.11.2015.
 */
public final class Utils {

    public static Student getCurrentUser(Http.Session session) {
        String username = session.get("username");
        checkNotNull(username, "username is null!");

        Student currentUser = Global.getStudentManager().getStudent(username);
        checkNotNull(currentUser, "currentUser is null!");

        return currentUser;
    }
}
