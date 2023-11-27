package Server.FileShare;

import Server.CORBA.FileSharePOA;
import Server.Dao.SharedFileDao;
import Server.Entity.SharedFile;

import java.util.List;

/**
 * The FileShareImpl class implements the FileShare interface and provides the server-side
 * implementation for file sharing operations using CORBA.
 */
public class FileShareImpl extends FileSharePOA {
    SharedFileDao sharedFileDao = new SharedFileDao();

    /**
     * Registers a file for sharing in the P2P file sharing system.
     *
     * @param fileName  The name of the file to be registered.
     * @param ipAddress The IP address of the client sharing the file.
     * @param port      The port on which the client is sharing the file.
     * @return A message indicating the success or failure of the registration process.
     */
    public String registerFile(String fileName, String ipAddress, int port) {
        SharedFile newSharedFile = new SharedFile(fileName, ipAddress, port);

        // Get all the share files from the database.
        List<SharedFile> sharedFiles = sharedFileDao.getAllSharedFiles();

        // Check if the share file already exists.
        for (SharedFile sharedFile : sharedFiles){
            if (newSharedFile.equals(sharedFile))
                return "The shared file already exists";
        }

        // Register the file.
        return sharedFileDao.createSharedFile(newSharedFile);

    }

    /**
     * Cancels the sharing of a file in the P2P file sharing system.
     *
     * @param fileName  The name of the file to be canceled for sharing.
     * @param ipAddress The IP address of the client canceling the sharing.
     * @param port      The port on which the client is canceling the sharing.
     * @return A message indicating the success or failure of the canceling process.
     */
    public String cancelSharing(String fileName, String ipAddress, int port){
        SharedFile newSharedFile = new SharedFile(fileName, ipAddress, port);

        // Get all the share files from the database.
        List<SharedFile> sharedFiles = sharedFileDao.getAllSharedFiles();

        // Check if the share file exists.
        for (SharedFile sharedFile : sharedFiles){
            if (newSharedFile.equals(sharedFile)){
                // Delete the identical share file record.
                return sharedFileDao.deleteSharedFile(sharedFile.getId());
            }
        }

        return "The shared file doesn't exist";
    }

    /**
     * Searches for shared files with a given filename in the P2P file sharing system.
     *
     * @param filename The name of the file to be searched.
     * @return A string containing the IDs of the shared files with the target filename.
     */
    public String findSharedFiles(String filename){
        // Get all the share files from the database.
        List<SharedFile> sharedFiles = sharedFileDao.getAllSharedFiles();

        String filesInfo = "";

        // Find all shared files with the target filename.
        for (SharedFile sharedFile : sharedFiles) {
            if (sharedFile.getFilename().equals(filename)) {
                filesInfo += sharedFile.getId() + " ";
            }
        }
        return filesInfo;
    }

    /**
     * Retrieves the socket address (IP address and port) of a shared file by its ID.
     *
     * @param id The ID of the shared file.
     * @return The socket address in the format "ip_address:port".
     */
    public String getSocketAddressById(int id){
        // Get the target file to share.
        SharedFile targetFile = sharedFileDao.getSharedFileById(id);

        return targetFile.getIpAddress() + ":" + targetFile.getPort();
    }
}
