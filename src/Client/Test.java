package Client;

import Client.Helpers.LocalIPAddressHelper;
import Client.SocketThreads.SocketServerThread;
import Client.Helpers.LocalFileHelper;

public class Test {
    public static void main(String[] args) {
        int port = 8080;
        String serverAddress = LocalIPAddressHelper.getServerAddress();
        System.out.println(serverAddress);

        String sourcePath = "/Users/rocky/Desktop/Github/CORBA-based-P2P-file-sharing-application/src/FileShare.idl";
        LocalFileHelper.copyFileToFolder(sourcePath);

        SocketServerThread socketServer = new SocketServerThread(port);
        socketServer.start();

        System.out.println("Continue!!!!");

    }
}
