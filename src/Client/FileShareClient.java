package Client;

import Helper.*;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

public class FileShareClient {

    public static void main(String[] args) {
        try {
            // create and initialize the ORB
            ORB orb = ORB.init(args, null);
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            FileShare fileShare = FileShareHelper.narrow(ncRef.resolve_str("FILE-SHARE"));

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