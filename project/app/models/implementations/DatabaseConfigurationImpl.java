package models.implementations;

import haw_hamburg.database.interfaces.DatabaseConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static com.google.common.base.Preconditions.*;

final class DatabaseConfigurationImpl implements DatabaseConfiguration {
    private final String hibernateDialect;
    private final String hibernateConnectionDriverClass;
    private final String hibernateConnectionUrl;
    private final String hibernateConnectionUsername;
    private final String hibernateConnectionPassword;

    private Properties loadProperties(InputStream configuration) throws IllegalStateException, IllegalArgumentException, NullPointerException {
        Properties properties = new Properties();
        try {
            properties.load(configuration);
        } catch (IOException e) {
            e.printStackTrace();
            checkState(false);
        }

        checkArgument(properties.containsKey("hibernate.dialect"));
        checkArgument(properties.containsKey("hibernate.connection.driver_class"));
        checkArgument(properties.containsKey("hibernate.connection.url"));
        checkArgument(properties.containsKey("hibernate.connection.username"));
        checkArgument(properties.containsKey("hibernate.connection.password"));

        checkNotNull(properties.getProperty("hibernate.dialect"));
        checkNotNull(properties.getProperty("hibernate.connection.driver_class"));
        checkNotNull(properties.getProperty("hibernate.connection.url"));
        checkNotNull(properties.getProperty("hibernate.connection.username"));
        checkNotNull(properties.getProperty("hibernate.connection.password"));

        return properties;
    }

    private DatabaseConfigurationImpl(InputStream configuration) throws NullPointerException, IllegalStateException, IllegalArgumentException {
        checkNotNull(configuration);
        Properties properties = loadProperties(configuration);

        this.hibernateDialect = properties.getProperty("hibernate.dialect");
        this.hibernateConnectionDriverClass = properties.getProperty("hibernate.connection.driver_class");
        this.hibernateConnectionUrl = properties.getProperty("hibernate.connection.url");
        this.hibernateConnectionUsername = properties.getProperty("hibernate.connection.username");
        this.hibernateConnectionPassword = properties.getProperty("hibernate.connection.password");
    }

    public static DatabaseConfiguration create(InputStream configuration) throws NullPointerException, IllegalStateException, IllegalArgumentException {
        return new DatabaseConfigurationImpl(configuration);
    }

    @Override
    public String getHibernateDialect() {
        return hibernateDialect;
    }

    @Override
    public String getHibernateConnectionDriverClass() {
        return hibernateConnectionDriverClass;
    }

    @Override
    public String getHibernateConnectionUrl() {
        return hibernateConnectionUrl;
    }

    @Override
    public String getHibernateConnectionUsername() {
        return hibernateConnectionUsername;
    }

    @Override
    public String getHibernateConnectionPassword() {
        return hibernateConnectionPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DatabaseConfigurationImpl)) return false;

        DatabaseConfigurationImpl that = (DatabaseConfigurationImpl) o;

        if (!getHibernateDialect().equals(that.getHibernateDialect())) return false;
        if (!getHibernateConnectionDriverClass().equals(that.getHibernateConnectionDriverClass())) return false;
        if (!getHibernateConnectionUrl().equals(that.getHibernateConnectionUrl())) return false;
        if (!getHibernateConnectionUsername().equals(that.getHibernateConnectionUsername())) return false;
        return getHibernateConnectionPassword().equals(that.getHibernateConnectionPassword());

    }

    @Override
    public int hashCode() {
        int result = getHibernateDialect().hashCode();
        result = 31 * result + getHibernateConnectionDriverClass().hashCode();
        result = 31 * result + getHibernateConnectionUrl().hashCode();
        result = 31 * result + getHibernateConnectionUsername().hashCode();
        result = 31 * result + getHibernateConnectionPassword().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "DatabaseConfiguration{" +
                "hibernateDialect='" + hibernateDialect + '\'' +
                ", hibernateConnectionDriverClass='" + hibernateConnectionDriverClass + '\'' +
                ", hibernateConnectionUrl='" + hibernateConnectionUrl + '\'' +
                ", hibernateConnectionUsername='" + hibernateConnectionUsername + '\'' +
                ", hibernateConnectionPassword='" + hibernateConnectionPassword + '\'' +
                '}';
    }
}
