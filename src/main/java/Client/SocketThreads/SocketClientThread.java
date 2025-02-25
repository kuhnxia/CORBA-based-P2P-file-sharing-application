package Client.SocketThreads;

import Client.Helpers.LocalFileHelper;

import java.io.*;
import java.net.*;

/**
 * The SocketClientThread class represents a client thread that connects to a server,
 * requests a file, and receives the file from the server.
 */
public class SocketClientThread extends Thread {
    private String fileName;
    private String ip;
    private int port;

    /**
     * Constructs a new SocketClientThread with the specified file name, IP address, and port.
     *
     * @param fileName The name of the file to request.
     * @param ip       The IP address of the server.
     * @param port     The port number of the server.
     */
    public SocketClientThread(String fileName, String ip, int port) {
        this.fileName = fileName;
        this.ip = ip;
        this.port = port;
    }

    /**
     * Runs the client thread, connecting to the server, requesting a file, and receiving the file.
     */
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

    /**
     * Receives a file from the server and saves it locally.
     *
     * @param fileName The name of the file to receive.
     * @param reader   The DataInputStream for reading from the server.
     * @return True if the file is received successfully, false otherwise.
     * @throws IOException If an I/O error occurs while receiving the file.
     */
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
