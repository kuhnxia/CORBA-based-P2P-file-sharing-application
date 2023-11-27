package Server.Connector;

import java.sql.*;

/**
 * The DatabaseConnector class is responsible for establishing and managing connections to the MySQL database.
 */
public class DatabaseConnector {
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "MyNewPass";
    private static final String DATABASE_NAME = "p2p_file_sharing_db";

    /**
     * Gets a connection to the MySQL database.
     *
     * @return A Connection object representing the database connection.
     * @throws SQLException If a database access error occurs.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL+DATABASE_NAME, USERNAME, PASSWORD);
    }

    /**
     * Initializes the database by creating necessary tables.
     */
    public static void initialize() {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {

            // Create the database if it does not exist
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME);

            // Switch to the database
            statement.executeUpdate("USE " + DATABASE_NAME);

            // Create SharedFiles table
            String createSharedFilesTable = "CREATE TABLE IF NOT EXISTS SharedFiles (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "file_name VARCHAR(255)," +
                    "ip_address VARCHAR(15)," +
                    "port INT" +
                    ")";
            statement.executeUpdate(createSharedFilesTable);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
