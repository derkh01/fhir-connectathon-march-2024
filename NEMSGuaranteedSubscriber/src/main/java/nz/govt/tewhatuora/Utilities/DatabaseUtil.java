package nz.govt.tewhatuora.Utilities;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import nz.govt.tewhatuora.Database.DeathDatabase;
import nz.govt.tewhatuora.Service.GlobalProperties;

public class DatabaseUtil {

    // final static Properties databaseProperties =
    // DatabaseUtil.SetDatabaseProperties();
    private static Properties properties = new Properties();

    public static void CallStoredProcedure(DeathDatabase dp) {
        // Create a Hibernate SessionFactory

        SetDatabaseProperties();
        Configuration configuration = new Configuration().configure();
        SessionFactory sessionFactory = configuration.configure().buildSessionFactory();

        // Open a session
        try (Session session = sessionFactory.openSession()) {
            // Begin a transaction
            session.beginTransaction();

            session.save(dp);

            // Commit the transaction
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Failed calling Stored procedure");
            System.out.println(e);
        } finally {
            // Close the SessionFactory
            sessionFactory.close();
        }

    }

    public static void SetDatabaseProperties() {

        properties.setProperty("hibernate.connection.url", GlobalProperties.getProperty("hibernate.connection.url"));
        properties.setProperty("hibernate.connection.username",
                GlobalProperties.getProperty("hibernate.connection.username"));
        properties.setProperty("hibernate.connection.password",
                GlobalProperties.getProperty("hibernate.connection.password"));
        properties.setProperty("hibernate.dialect", GlobalProperties.getProperty("hibernate.dialect"));

        // return properties;
    }

    public static Connection GetConnection(String env) throws SQLException {

        String url = GlobalProperties.getProperty(env + ".connection.url");
        String username = GlobalProperties.getProperty(env + ".connection.username");
        String password = GlobalProperties.getProperty(env + ".connection.password");

        return DriverManager.getConnection(url, username, password);

    }

    public static void CloseConnection(Connection connection) {

        if (connection != null) {
            try {

                connection.close();

            } catch (SQLException e) {
                System.out.println("Failed to close database connection");
                System.out.println(e);
            }
        }

    }

}