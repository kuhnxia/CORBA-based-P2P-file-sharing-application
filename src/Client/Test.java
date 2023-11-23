package Client;

import Client.Connector.CORBAConnector;
import Client.Helpers.LocalIPAddressHelper;
import Client.SocketThreads.SocketServerThread;
import Client.Helpers.LocalFileHelper;
import Client.CORBA.FileShare;

import java.net.InetAddress;
import java.util.List;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        int port = 8080;
        String serverAddress = getServerAddress();
        System.out.println(serverAddress);

        String sourcePath = "/Users/rocky/Desktop/Github/CORBA-based-P2P-file-sharing-application/src/FileShare.idl";
        LocalFileHelper.copyFileToSharedFolder(sourcePath);

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

    public static String getServerAddress () {
        List<InetAddress> inet4Addresses = LocalIPAddressHelper.getLocalIPAddresses();

        if (inet4Addresses.size() == 1){
            return inet4Addresses.get(0).getHostAddress();
        } else {
            Scanner sc = new Scanner(System.in);
            System.out.println("You are using not only one network interfaces, such as Ethernet, WiFi, Cellular, VPN.\n" +
                    "Choose the correct IP that the router assigned to you in your local network: \n");
            int i = 1;
            for (InetAddress inet4Address : inet4Addresses) {
                System.out.printf("Enter %d if you will use local IP: %s\n\n", i, inet4Address.getHostAddress());
                i++;
            }
            System.out.println("If you do not know which IP you will use to interact with other computers in your local network\n"
                    + "You can try to close Cellular or VPN, and keep only one of Wifi or Ethernet connections,\n"
                    + "then restart this program manually!\n");
            System.out.println("Your choice: ");
            return inet4Addresses.get(sc.nextInt()-1).getHostAddress();
        }

    }
}
