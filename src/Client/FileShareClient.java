package Client;

import Server.CORBA.FileShare;
import Server.CORBA.FileShareHelper;

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
            String message = fileShare.registerFile("User1", "File1", 00);
            System.out.println(message);
            message = fileShare.registerFile("User2", "File2", 11);
            System.out.println(message);

            // Test file search
            String searchResult = fileShare.findSharedFiles("User1");
            System.out.println("Search result: \n" + searchResult);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}