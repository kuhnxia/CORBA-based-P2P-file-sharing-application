package Server.FileShare;

import Server.CORBA.FileSharePOA;
import Server.Dao.SharedFileDao;
import Server.Entity.SharedFile;

import java.util.List;

public class FileShareImpl extends FileSharePOA {
    SharedFileDao sharedFileDao = new SharedFileDao();
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

    public String findSharedFiles(String filename){
        // Get all the share files from the database.
        List<SharedFile> sharedFiles = sharedFileDao.getAllSharedFiles();

        String filesInfo = "";

        // Find all shared files with the target filename.
        for (SharedFile sharedFile : sharedFiles) {
            if (sharedFile.getFilename().equals(filename)) {
                filesInfo += "file id: " + sharedFile.getId() + "\n";
            }
        }
        return filesInfo;
    }

    public String getSocketAddressById(int id){
        // Get the target file to share.
        SharedFile targetFile = sharedFileDao.getSharedFileById(id);

        return targetFile.getIpAddress() + ":" + targetFile.getPort();
    }
}
