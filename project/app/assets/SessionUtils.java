package assets;


import net.unikit.database.exceptions.EntityNotFoundException;
import net.unikit.database.interfaces.entities.Student;
import play.mvc.Http;

import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Andreas on 06.11.2015.
 */
public final class SessionUtils {
    private static final long TIMEOUT = (1000 * 60 * 10); // 10 minutes

    public static void initSession(Http.Session session, String username) {
        // Clear previous session data
        session.clear();

        // Store unique id in session
        String uuid = java.util.UUID.randomUUID().toString();
        session.put("uuid", uuid);

        // Store username in session
        session.put("username", username);

        // Store timeout in session
        session.put("timeout", String.valueOf(System.currentTimeMillis() + TIMEOUT));
    }

    public static void destroySession(Http.Session session) {
        // Clear session data
        session.clear();
    }

    public static Student getCurrentUser(Http.Session session) {
        String username = session.get("username");
        checkNotNull(username, "username is null!");

        Student currentUser = null;
        try {
            currentUser = Global.getStudentManager().getEntity(
                    Global.getStudentManager().createID(username));
        } catch (EntityNotFoundException e) {
            //TODO: Error handling
            e.printStackTrace();
        }
        checkNotNull(currentUser, "currentUser is null!");

        return currentUser;
    }

    public static Date getSessionTimeout(Http.Session session) {
        String timeout = session.get("timeout");
        checkNotNull(timeout, "timeout is null!");

        long timeoutLong = Long.parseLong(timeout);
        long currentTimeMillis = System.currentTimeMillis();

        if (timeoutLong < currentTimeMillis) {
            // TODO: Destroy session!
        } else {
            timeoutLong = currentTimeMillis + TIMEOUT;
            session.put("timeout", String.valueOf(timeoutLong));
        }

        return new Date(timeoutLong);
    }
}
