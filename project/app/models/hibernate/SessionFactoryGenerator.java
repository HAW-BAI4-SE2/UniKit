package models.hibernate;

/**
 * @author Andreas Berks
 */

import models.imports.implementations.hibernate.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SessionFactoryGenerator
{
    public static SessionFactory buildSessionFactory() {
        try {
            InputStream inputStream = new FileInputStream("hibernate_unikit.properties");
            Properties properties = new Properties();
            properties.load(inputStream);

            Configuration configuration = new Configuration();
            configuration.setProperties(properties);

            configuration = configuration.addAnnotatedClass(StudentModel.class);
            configuration = configuration.addAnnotatedClass(CourseModel.class);
            configuration = configuration.addAnnotatedClass(CompletedCourseModel.class);
            configuration = configuration.addAnnotatedClass(GroupModel.class);
            configuration = configuration.addAnnotatedClass(AppointmentModel.class);

            return configuration.buildSessionFactory();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
