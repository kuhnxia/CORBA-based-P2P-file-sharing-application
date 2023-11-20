package Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnector {
    private static final String URL = "jdbc:mysql://localhost:3306/P2P-file-sharing-db";
    private static final String USERNAME = "your_username";
    private static final String PASSWORD = "your_password";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public static void initialize() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            // Create Owners table
            String createOwnersTable = "CREATE TABLE IF NOT EXISTS Owners (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "username VARCHAR(255)," +
                    "ip_address VARCHAR(15)," +
                    "port INT," +
                    ")";
            statement.executeUpdate(createOwnersTable);

            // Create SharedFiles table
            String createSharedFilesTable = "CREATE TABLE IF NOT EXISTS SharedFiles (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "owner_id INT," +
                    "file_name VARCHAR(255)," +
                    "FOREIGN KEY (owner_id) REFERENCES Owners(id)" +
                    ")";
            statement.executeUpdate(createSharedFilesTable);

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle initialization failure
        }
    }
}
