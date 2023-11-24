package Client.Helpers;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class LocalFileHelper {
    private static String sharedFilesDirectory;
    private static String receivedFilesDirectory;
    private static final String APPLICATION_CACHE_DIRECTORY = "CORBA-based-P2P-file-sharing-application";
    public static final String SHARED_FILES_DIRECTORY_PART = "shared_files";
    public static final String RECEIVED_FILES_DIRECTORY_PART = "received_files";

    public static void createSharedFileDirectory(String socketServerAddress, int port) {
        //Get the shared file directory;
        sharedFilesDirectory = getSharedFilesDirectory(socketServerAddress, port);

        // Create Path object for the destination folder
        Path sharedFolderPath = Paths.get(sharedFilesDirectory);

        // Check if the destination folder exists, and create it if not
        if (!Files.exists(sharedFolderPath)) {
            try {
                Files.createDirectories(sharedFolderPath);
                System.out.println("Shared folder created: " + sharedFolderPath);
            } catch (IOException e) {
                System.err.println("Error creating destination folder: " + e.getMessage());
            }
        }

    }

    public static File createNewFileForSending(String fileName, String serverAddress, int port) {
        /*
        One socket server can map to more than one server address!

        In case, there is more than one available network interface,
        and the socket server is created with the same port more than one time,
        and each time, this client may choose a different IP (interface)
        to interact with other clients in its identical local network.

        This method can handle the file share request from a local network
        other than the local network where this client is registering, cancelling, searching and sharing files
        if the port is the same.
        */

        String alterSharedFilesDirectory = getSharedFilesDirectory(serverAddress, port);
        return new File(alterSharedFilesDirectory + File.separator + fileName);

    }

    public static File createNewFileForReceiving(String fileName) {

        receivedFilesDirectory = getReceivedFilesDirectory();
        File file = new File(receivedFilesDirectory + File.separator + fileName);

        // Ensure that the directories leading to the file exist
        File parentDirectory = file.getParentFile();
        if (!parentDirectory.exists()) {
            parentDirectory.mkdirs();
        }

        return file;
    }

    public static String getFilenameFromPath(String sourceFilePath) {
        // Create Path object for the source file
        Path sourcePath = Paths.get(sourceFilePath);

        // Extract file name from the source path
        String fileName = sourcePath.getFileName().toString();

        return fileName;
    }

    public static boolean copyFileToSharedFolder(String sourceFilePath) {
        // Create Path object for the source file
        Path sourcePath = Paths.get(sourceFilePath);

        // Extract file name from the source path
        String fileName = sourcePath.getFileName().toString();

        // Create Path object for the destination folder
        Path sharedFolderPath = Paths.get(sharedFilesDirectory);

        // Create Path object for the destination file
        Path destinationFilePath = sharedFolderPath.resolve(fileName);

        // Check if the destination file already exists
        if (Files.exists(destinationFilePath)) return true;

        try {
            // Copy the file to the destination folder
            Files.copy(sourcePath, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("File copied successfully to: " + destinationFilePath);
            return true;
        } catch (IOException e) {
            System.err.println("Error copying the file: " + e.getMessage());
        }
        return false;
    }

    public static void deleteFileFromSharedFolder(String fileName) {
        // Construct the file path
        String filePath = sharedFilesDirectory + File.separator + fileName;

        // Create a File object for the file and delete the file.
        File fileToDelete = new File(filePath);
        if (fileToDelete.exists()) {
            fileToDelete.delete();
            System.out.println("File deleted from the shared folder");
        }

    }

    private static String getSharedFilesDirectory(String socketServerAddress, int port) {
        // Get the user's home directory
        String userHome = System.getProperty("user.home");
        
        return userHome + File.separator
                + APPLICATION_CACHE_DIRECTORY + File.separator
                + SHARED_FILES_DIRECTORY_PART + File.separator
                + socketServerAddress.replace(".", "_")
                + "_" + port;
    }

    private static String getReceivedFilesDirectory() {
        // Get the user's home directory
        String userHome = System.getProperty("user.home");

        return userHome + File.separator
                + APPLICATION_CACHE_DIRECTORY + File.separator
                + RECEIVED_FILES_DIRECTORY_PART;
    }

}