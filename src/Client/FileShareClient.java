package Client;

import Client.CORBA.FileShare;
import Client.Connector.CORBAConnector;
import Client.Helpers.LocalFileHelper;
import Client.Helpers.LocalIPAddressHelper;
import Client.SocketThreads.SocketServerThread;

import java.net.InetAddress;
import java.util.List;
import java.util.Scanner;

public class FileShareClient {

    // Change the ip address to your corba file sharing register server.
    private static final String CORBA_SERVER_IP_ADDRESS = "192.168.0.4";
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Get this computer's IP address in your local network ...");
        String serverAddress = getServerAddress();
        System.out.println("You socket server IP address is: " + serverAddress);

        System.out.println("What is your prefer port to start the file sharing socket server?");
        int port = sc.nextInt();
        SocketServerThread socketServer = new SocketServerThread(port);
        socketServer.start();

        System.out.printf("Get the file register server from %s:1050\n", CORBA_SERVER_IP_ADDRESS);
        FileShare fileShare = CORBAConnector.getFileShareServer(CORBA_SERVER_IP_ADDRESS);

        String sourcePath = "/Users/rocky/Desktop/Github/CORBA-based-P2P-file-sharing-application/src/FileShare.idl";
        LocalFileHelper.copyFileToFolder(sourcePath);





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
