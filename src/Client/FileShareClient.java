package Client;

import Server.Helper.FileShare;
import Server.Helper.FileShareHelper;

public class FileShareClient {

    public static void main(String[] args) {
        try {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, null);

            // Replace with the actual IOR obtained from the server
            String ior = "IOR:...";
            org.omg.CORBA.Object obj = orb.string_to_object(ior);
            FileShare fileShare = FileShareHelper.narrow(obj);

            // Test file registration
            fileShare.registerFile("User1", "File1");
            fileShare.registerFile("User2", "File2");

            // Test file search
            String searchResult = fileShare.searchFiles("File1");
            System.out.println("Search result: " + searchResult);

            // Test file download
            String owner = fileShare.getOwner("File1");
            boolean downloadResult = fileShare.downloadFile("Requester", "File1", owner);
            if (downloadResult) {
                System.out.println("File downloaded successfully.");
            } else {
                System.out.println("File download failed.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}