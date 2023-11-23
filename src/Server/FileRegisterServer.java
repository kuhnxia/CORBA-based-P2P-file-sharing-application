package Server;

import Server.CORBA.FileShare;
import Server.CORBA.FileShareHelper;
import Server.FileShare.FileShareImpl;
import Server.Helper.LocalIPAddressHelper;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import java.io.IOException;

public class FileRegisterServer {
    public static void main(String[] args) {
        // Show your ip address in the local network.
        String serverAddress = LocalIPAddressHelper.getServerAddress();
        System.out.println("Register Server IP: " + serverAddress);

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
            Thread.sleep(2000);

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
}
