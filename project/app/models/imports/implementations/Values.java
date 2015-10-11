package models.imports.implementations;

import models.imports.implementations.hibernate.CourseModel;
import models.imports.implementations.hibernate.StudentModel;
import models.imports.interfaces.Course;
import models.imports.interfaces.Student;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * An utility class for basic database functions.
 * Author: Jonas Johannsen, 10.10.2015
 */
public class Values {

    /**
     * Returns a factory for sessions.
     * @return a factory for sessions.
     */
    public static SessionFactory createSessionFactory() {
        SessionFactory sessionFactory;
        ServiceRegistry serviceRegistry;
        Configuration configuration = new Configuration();
        configuration.configure();
        serviceRegistry = new ServiceRegistryBuilder().applySettings(
                configuration.getProperties()). buildServiceRegistry();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        return sessionFactory;
    }

    /**
     * Returns the currently logged in user.
     * @return The currently logged in user.
     * WARNING! This Method is just a prototype right now and doesn't return the real logged in user.
     */
    public static Student getCurrentUser() {
        SessionFactory factory = createSessionFactory();
        Session session = null;
        int matrikelnummer = 1;
        Student student = null;

        try{
            session = factory.openSession();
            StudentModel studentModel = ((StudentModel)session.load(StudentModel.class,matrikelnummer));
            student = new StudentImpl(studentModel);
        }
        finally {
            if (session != null && session.isConnected()) {
                session.close();
            }
        }
        return student;
    }

    /**
     * A Collection of every matriculated student in the system.
     * @return A Collection of every matriculated student in the system.
     */
    public static Collection<Student> getStudents() {
        SessionFactory factory = createSessionFactory();
        Session session = null;
        List<Student> students = new ArrayList<>();

        session = factory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            List<StudentModel> studentList = session.createQuery("FROM Student").list();
            for (Iterator iterator=studentList.iterator(); iterator.hasNext();){
                StudentModel studentModel = (StudentModel) iterator.next();
                students.add(new StudentImpl(studentModel));
            }
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
        return students;
    }

    /**
     * A Collection of every course in the system.
     * @return A Collection of every course in the system.
     */
    public static Collection<Course> getCourses() {
        SessionFactory factory = createSessionFactory();
        Session session = null;
        List<Course> courses = new ArrayList<>();

        session = factory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            List<Course> courseList = session.createQuery("FROM Course").list();
            for (Iterator iterator=courseList.iterator(); iterator.hasNext();){
                CourseModel courseModel = (CourseModel) iterator.next();
                courses.add(new CourseImpl(courseModel));
            }
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
        return courses;
    }
}
