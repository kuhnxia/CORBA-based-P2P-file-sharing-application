package Client.SocketThreads;

import Client.Helpers.LocalFileHelper;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class SocketClientThread extends Thread {
    private String fileName;
    private String ip;
    private int port;

    public SocketClientThread(String fileName, String ip, int port) {
        this.fileName = fileName;
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket();

            SocketAddress socketAddress = new InetSocketAddress(ip, port);
            //In case, spend too much time to connect an invalid socket address.
            int timeout = 1000;
            socket.connect(socketAddress, timeout);
            System.out.println("Socket connection successful!");

            DataInputStream reader = new DataInputStream(socket.getInputStream());
            DataOutputStream writer = new DataOutputStream(socket.getOutputStream());

            // Request a file from the server
            System.out.printf("Requesting file: %s from %s:%d\n", fileName, ip, port);
            writer.writeUTF(fileName);

            // Receive the file from the server
            Boolean received = receiveFile(fileName, reader);
            if (received) {
                System.out.println("File received successfully!");
            } else {
                System.out.println("File not found!");
            }


            // Close the connection
            socket.close();
        } catch (IOException e) {
            System.out.println("Error:" + e.getMessage());
        }
    }

    private boolean receiveFile(String fileName, DataInputStream reader) throws IOException {
        long length = reader.readLong();
        Boolean received = false;

        if (length != -1) {
            File file = LocalFileHelper.createNewFileForReceiving(fileName);

            try (FileOutputStream fos = new FileOutputStream(file)) {
                byte[] buffer = new byte[4096];

                int bytesRead;
                while (length > 0 && (bytesRead = reader.read(buffer, 0, (int) Math.min(buffer.length, length))) != -1) {
                    fos.write(buffer, 0, bytesRead);
                    length -= bytesRead;
                }
            }
            received = true;
        }
        return received;
    }
}
