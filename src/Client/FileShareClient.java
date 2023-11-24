package Client;

import Client.CORBA.FileShare;
import Client.Connector.CORBAConnector;
import Client.Helpers.LocalFileHelper;
import Client.Helpers.LocalIPAddressHelper;
import Client.SocketThreads.SocketClientThread;
import Client.SocketThreads.SocketServerThread;

import java.net.InetAddress;
import java.util.*;

public class FileShareClient {

    // Change it to the actual IP address of your CORBA file-sharing register server.
    private static final String CORBA_SERVER_IP_ADDRESS = "192.168.0.2";
    private static String socketServerAddress;
    private static int port;
    private static FileShare fileShare;
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("Get this computer's IP address in your local network ...");
        socketServerAddress = getSocketServerAddress();
        System.out.println("You socket server IP address is: " + socketServerAddress);

        System.out.println("What is your prefer port to start the file sharing socket server?");
        port = sc.nextInt();

       LocalFileHelper.createSharedFileDirectory(socketServerAddress, port);

        SocketServerThread socketServer = new SocketServerThread(port);
        socketServer.start();

        System.out.printf("Get the file register server from %s:1050\n", CORBA_SERVER_IP_ADDRESS);
        fileShare = CORBAConnector.getFileShareServer(CORBA_SERVER_IP_ADDRESS);

        while (true){
            try {
                System.out.println("\nPlease choose which option do you want: \n" +
                        "Enter 1 to register a file for sharing!\n" +
                        "Enter 2 to cancel a shared file\n" +
                        "Enter 3 to search and request a shared file from other clients\n");

                operation(sc.nextInt());

            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                sc.next(); // consume the invalid input to prevent an infinite loop
            }

        }
    }
    private static void operation(int choice) {
        switch (choice) {
            case 1:
                // Registering a file for sharing.
                registerFile();
                break;
            case 2:
                // Canceling a shared file.
                cancelSharing();
                break;
            case 3:
                // searching and requesting a shared file from other clients.
                searchAndRequestSharedFile();
                break;
            default:
                System.out.println("Invalid choice. Please enter a valid option.");
        }
    }

    private static void registerFile() {
        try {
            System.out.println("Please enter the absolute path of your file ready to share:");
            String sourcePath = sc.next();

            String fileName = LocalFileHelper.getFilenameFromPath(sourcePath);
            Boolean copied = LocalFileHelper.copyFileToSharedFolder(sourcePath);
            Thread.sleep(100);
            if (copied){
                String message = fileShare.registerFile(fileName, socketServerAddress, port);
                System.out.println(message);
            } else {
                System.out.println("It is not a valid file path");
            }
        } catch (InterruptedException e){
            System.out.println("Error: " + e.getMessage());
        }


    }

    private static void cancelSharing() {
        System.out.println("Please enter the file name you want to cancel sharing:");
        String fileName = sc.next();

        String message = fileShare.cancelSharing(fileName, socketServerAddress, port);
        System.out.println(message);

        LocalFileHelper.deleteFileFromSharedFolder(fileName);
    }

    private static void searchAndRequestSharedFile() {
        System.out.println("Please enter the file name you want to search:");
        String fileName = sc.next();

        String searchResult = fileShare.findSharedFiles(fileName);
        if (!searchResult.equals("")){
            System.out.printf("\nList all available file ids with the same target name %s: \n", fileName);

            String[] stringIds = searchResult.split(" ");
            List<Integer> fileIds = new ArrayList<>();
            for (String id : stringIds){
                System.out.println("File id: " + id);
                fileIds.add(Integer.parseInt(id));
            }

            while (true) {
                try {
                    System.out.println("\nPlease enter the correct id of the file you want to request for sharing!");
                    System.out.println("Enter 0 to go back!");
                    int fileId = sc.nextInt();
                    if (fileId == 0) break;

                    if (fileIds.contains(fileId)) {
                        // Get client information
                        String[] socketAddress = fileShare.getSocketAddressById(fileId).split(":");
                        String clientIP =socketAddress[0];
                        int clientPort = Integer.parseInt(socketAddress[1]);

                        // Request and send the file.
                        SocketClientThread socketClient = new SocketClientThread(fileName, clientIP, clientPort);
                        socketClient.start();

                        // Waiting the socket client get response back.
                        Thread.sleep(200);
                    }


                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid integer.");
                    sc.next(); // consume the invalid input to prevent an infinite loop
                } catch (InterruptedException e) {
                    System.out.println("Error: " + e);
                }

            }

        } else {
            System.out.println("No match result.");
        }

    }
    private static String getSocketServerAddress() {
        List<InetAddress> inet4Addresses = LocalIPAddressHelper.getLocalIPAddresses();

        if (inet4Addresses.size() == 1){
            return inet4Addresses.get(0).getHostAddress();
        } else {
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

            int choice = 0;
            while (choice <= 0 || choice >= i) {
                System.out.println("Please enter a right choice: ");
                try {
                    choice = sc.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid integer.");
                    sc.next(); // consume the invalid input to prevent an infinite loop
                }
            }
            return inet4Addresses.get(choice-1).getHostAddress();

        }

    }
}
