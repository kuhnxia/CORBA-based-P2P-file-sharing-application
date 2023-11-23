package Client;

import Client.Connector.CORBAConnector;
import Client.Helpers.LocalIPAddressHelper;
import Client.SocketThreads.SocketServerThread;
import Client.Helpers.LocalFileHelper;
import Client.CORBA.FileShare;

public class Test {
    public static void main(String[] args) {
        int port = 8080;
        String serverAddress = LocalIPAddressHelper.getServerAddress();
        System.out.println(serverAddress);

        String sourcePath = "/Users/rocky/Desktop/Github/CORBA-based-P2P-file-sharing-application/src/FileShare.idl";
        LocalFileHelper.copyFileToFolder(sourcePath);

        SocketServerThread socketServer = new SocketServerThread(port);
        socketServer.start();

        FileShare fileShare = CORBAConnector.getFileShareServer(serverAddress);

        // Test file registration
        String message = fileShare.registerFile("User1", "File1", 00);
        System.out.println(message);
        message = fileShare.registerFile("User2", "File2", 11);
        System.out.println(message);

        // Test file search
        String searchResult = fileShare.findSharedFiles("User1");
        System.out.println("Search result: \n" + searchResult);

        String m1 = fileShare.registerFile("dd", "localhost", 8991);
        String m2 = fileShare.registerFile("dd", "localhost", 8991);
        String m3 = fileShare.registerFile("dd", "localhost", 8991);
        System.out.println(m1);
        System.out.println(m2);
        System.out.println(m3);

    }
}
