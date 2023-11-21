package Server.Dao;
import Server.Entity.SharedFile;
import Server.Connecter.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SharedFileDao {

    public String createSharedFile(SharedFile sharedFile) {
        String message = null;
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO SharedFiles (file_name, ip_address, port) VALUES (?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, sharedFile.getFilename());
            preparedStatement.setString(2, sharedFile.getIpAddress());
            preparedStatement.setInt(3, sharedFile.getPort());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                message = "Creating shared file failed, no rows affected.";
                throw new SQLException(message);
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    sharedFile.setId(generatedKeys.getInt(1));
                    message = "File registered: " + sharedFile;
                } else {
                    message = "Creating shared file failed, no ID obtained.";
                    throw new SQLException(message);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            message = "Error: " + e;
        }
        return message;
    }

    public SharedFile getSharedFileById(int fileId) {
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM SharedFiles WHERE id = ?")) {

            preparedStatement.setInt(1, fileId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToSharedFile(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<SharedFile> getAllSharedFiles() {
        List<SharedFile> sharedFiles = new ArrayList<>();

        try (Connection connection = DatabaseConnector.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM SharedFiles")) {

            while (resultSet.next()) {
                SharedFile sharedFile = mapResultSetToSharedFile(resultSet);
                sharedFiles.add(sharedFile);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sharedFiles;
    }

    public void updateSharedFile(SharedFile sharedFile) {
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE SharedFiles SET file_name = ?, ip_address = ?, port = ? WHERE id = ?")) {

            preparedStatement.setString(1, sharedFile.getFilename());
            preparedStatement.setString(2, sharedFile.getIpAddress());
            preparedStatement.setInt(3, sharedFile.getPort());
            preparedStatement.setInt(4, sharedFile.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String deleteSharedFile(int fileId) {
        String message = null;
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM SharedFiles WHERE id = ?")) {

            preparedStatement.setInt(1, fileId);
            preparedStatement.executeUpdate();
            message = "The shared file has no longer shareable.";

        } catch (SQLException e) {
            e.printStackTrace();
            message = "Error: " + e;
        }
        return message;
    }

    private SharedFile mapResultSetToSharedFile(ResultSet resultSet) throws SQLException {
        SharedFile sharedFile = new SharedFile(
                resultSet.getString("file_name"),
                resultSet.getString("ip_address"),
                resultSet.getInt("port")
        );
        sharedFile.setId(resultSet.getInt("id"));
        return sharedFile;
    }
}