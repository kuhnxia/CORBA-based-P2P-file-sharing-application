package Server;

import Server.CORBA.FileShare;
import Server.CORBA.FileShareHelper;
import Server.Connector.DatabaseConnector;
import Server.FileShare.FileShareImpl;
import Server.Helper.LocalIPAddressHelper;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import java.io.IOException;
import java.net.InetAddress;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * The FileRegisterServer class represents the server that registers file-sharing services
 * using CORBA in the P2P file-sharing system.
 */
public class FileRegisterServer {
    /**
     * The main method to start the File Register Server.
     *
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        System.out.println("Get this computer's IP address in your local network ...");
        String serverAddress = getServerAddress();
        System.out.println("Register Server IP: " + serverAddress);

        //Initialize the database if not.
        DatabaseConnector.initialize();

        try{
            // Launch the Object Request Broker Daemon (ORBD)
            new Thread(() -> {
                try {
                    Runtime.getRuntime().exec("orbd -ORBInitialPort 1050 -ORBInitialHost localhost");
                } catch (IOException e) {
                    System.err.println("ERROR: " + e);
                }
            }).start();

            System.out.println("Waiting ORBD to launch...");
            Thread.sleep(1000);

            // Set the ORB properties programmatically
            java.util.Properties props = new java.util.Properties();
            props.put("org.omg.CORBA.ORBInitialPort", "1050");
            props.put("org.omg.CORBA.ORBInitialHost", "localhost");

            // create and initialize the ORB with the specified properties
            ORB orb = ORB.init(args, props);

            // get reference to rootpoa & activate the POAManager
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();

            // create servant
            FileShareImpl fileShare = new FileShareImpl();

            // get object reference from the servant
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(fileShare);
            FileShare href = FileShareHelper.narrow(ref);

            org.omg.CORBA.Object objRef =  orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            NameComponent path[] = ncRef.to_name( "FILE-SHARE" );
            ncRef.rebind(path, href);

            System.out.println("File register Server ready and waiting ...");

            // wait for invocations from clients
            orb.run();
        }

        catch (Exception e) {
            System.err.println("ERROR: " + e);
        }

        System.out.println("Exiting ...");
    }

    /**
     * Retrieves the server's IP address from available network interfaces.
     *
     * @return The IP address of the server.
     */
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
