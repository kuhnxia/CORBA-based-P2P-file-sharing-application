package Client.Helpers;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;

public class LocalIPAddressHelper {
    public static String getServerAddress () {
        List<InetAddress> inet4Addresses = getLocalIPAddresses();

        if (inet4Addresses.size() == 1){
            return inet4Addresses.get(0).getHostAddress();
        } else {
            Scanner sc = new Scanner(System.in);
            System.out.println("You are using not only one network interfaces, such as Ethernet, WiFi, Cellular, VPN.\n" +
                    "Choose the correct IP that the router assigned to you in your local network: \n");
            int i = 1;
            for (InetAddress inet4Address : inet4Addresses) {
                System.out.printf("Enter %d if your will use local IP: %s\n\n", i, inet4Address.getHostAddress());
                i++;
            }
            System.out.println("If you do not know which IP you will use to interact with other computers in your local network\n"
                    + "You can try to close Cellular or VPN, and keep only one of Wifi or Ethernet connections,\n"
                    + "then restart this program manually!\n");
            System.out.println("Your choice: ");
            return inet4Addresses.get(sc.nextInt()-1).getHostAddress();
        }

    }

    private static List<InetAddress> getLocalIPAddresses() {
        List<InetAddress> inet4Addresses = new ArrayList<>();

        try{
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();

                if (!networkInterface.isLoopback() && networkInterface.isUp()) {
                    Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();

                    while (addresses.hasMoreElements()) {
                        InetAddress address = addresses.nextElement();
                        if (address instanceof Inet4Address) {
                            inet4Addresses.add(address);
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return inet4Addresses;
    }
}
