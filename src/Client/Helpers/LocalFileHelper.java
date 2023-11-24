package Client.Helpers;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class LocalFileHelper {
    private static String sharedFilesDirectory;
    public static final String SHARED_FILES_DIRECTORY_PART = "shared_files";
    public static final String RECEIVED_FILES_DIRECTORY = "received_files";

    public static void createSharedFileDirectory(String socketServerAddress, int port) {
        //Get the shared file directory;
        sharedFilesDirectory = SHARED_FILES_DIRECTORY_PART + File.separator
                + socketServerAddress.replace(".", "_")
                + "_" + port;

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

    public static File createNewFileForSending(String fileName) {
        return new File(sharedFilesDirectory + File.separator + fileName);

    }

    public static File createNewFileForReceiving(String fileName) {
        File file = new File(RECEIVED_FILES_DIRECTORY + File.separator + fileName);

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

}