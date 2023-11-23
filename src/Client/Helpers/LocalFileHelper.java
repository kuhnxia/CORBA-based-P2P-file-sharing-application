package Client.Helpers;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class LocalFileHelper {
    public static final String SHARED_FILES_DIRECTORY = "shared_files/";
    public static final String RECEIVED_FILES_DIRECTORY = "received_files/";

    public static File createNewFileForSending(String fileName) {
        return new File(LocalFileHelper.SHARED_FILES_DIRECTORY + fileName);

    }

    public static File createNewFileForReceiving(String fileName) {
        File file = new File(RECEIVED_FILES_DIRECTORY + fileName);

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

    public static void copyFileToSharedFolder(String sourceFilePath) {
        // Create Path object for the source file
        Path sourcePath = Paths.get(sourceFilePath);

        // Extract file name from the source path
        String fileName = sourcePath.getFileName().toString();

        // Create Path object for the destination folder
        Path destinationFolderPath = Paths.get(SHARED_FILES_DIRECTORY);

        // Check if the destination folder exists, and create it if not
        if (!Files.exists(destinationFolderPath)) {
            try {
                Files.createDirectories(destinationFolderPath);
                System.out.println("Destination folder created: " + destinationFolderPath);
            } catch (IOException e) {
                System.err.println("Error creating destination folder: " + e.getMessage());
                return; // Stop execution if folder creation fails
            }
        }

        try {
            // Create Path object for the destination file
            Path destinationFilePath = destinationFolderPath.resolve(fileName);

            // Copy the file to the destination folder
            Files.copy(sourcePath, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("File copied successfully to: " + destinationFilePath);
        } catch (IOException e) {
            System.err.println("Error copying the file: " + e.getMessage());
        }
    }

    public static void deleteFileFromSharedFolder(String fileName) {
        // Construct the file path
        String filePath = SHARED_FILES_DIRECTORY + fileName;

        // Create a File object for the file and delete the file.
        File fileToDelete = new File(filePath);
        if (fileToDelete.exists()) fileToDelete.delete();

    }
}