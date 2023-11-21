package Server;

import Server.Connecter.DatabaseConnector;
import Server.FileShare.FileShareImpl;

public class test {
    public static void main(String[] args) {
        String message = null;
        DatabaseConnector.initialize();
        FileShareImpl fileShare = new FileShareImpl();

        message = fileShare.registerFile("ddd", "localhost", 8090);
        System.out.println(message);
        message = fileShare.registerFile("ddd", "localhost", 8990);
        System.out.println(message);
        message = fileShare.cancelSharing("ddd", "localhost", 8990);
        System.out.println(message);
        message = fileShare.registerFile("ddd", "localhost", 9090);
        System.out.println(message);
        message = fileShare.findSharedFiles("ddd");
        if (!message.equals("")) {
            System.out.println("Available files to download: ");
            System.out.println(message);
        } else {
            System.out.println("No match result.");
        }
        message = fileShare.findSharedFiles("ggg");
        if (!message.equals("")) {
            System.out.println("Available files to download: ");
            System.out.println(message);
        } else {
            System.out.println("No match result.");
        }
        message = fileShare.getSocketAddressById(10);
        System.out.println(message);

    }
}
