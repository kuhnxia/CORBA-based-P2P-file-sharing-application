package Client.Connector;

import Server.CORBA.FileShare;
import Server.CORBA.FileShareHelper;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

public class CORBAConnector{
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