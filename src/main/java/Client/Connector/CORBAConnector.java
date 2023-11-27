package Client.Connector;

import Client.CORBA.FileShare;
import Client.CORBA.FileShareHelper;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

/**
 * The CORBAConnector class provides methods for connecting to a FileShare server using CORBA.
 */
public class CORBAConnector{
    /**
     * Retrieves a FileShare server instance based on the provided IP address.
     *
     * @param ip The IP address of the FileShare server.
     * @return A FileShare object representing the server connection.
     */
    public static FileShare getFileShareServer(String ip) {
        FileShare fileShare = null;
        try {
            // Set the ORB properties programmatically
            java.util.Properties props = new java.util.Properties();
            props.put("org.omg.CORBA.ORBInitialPort", "1050");
            props.put("org.omg.CORBA.ORBInitialHost", ip);

            // create and initialize the ORB with the specified properties
            ORB orb = ORB.init(new String[]{}, props);

            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            fileShare = FileShareHelper.narrow(ncRef.resolve_str("FILE-SHARE"));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return fileShare;
    }
}