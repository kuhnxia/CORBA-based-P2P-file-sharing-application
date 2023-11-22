package Client.SocketThreads;

import Client.Helpers.LocalFileHelper;

import java.io.*;
import java.net.Socket;

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
            Socket socket = new Socket(ip, port);
            DataInputStream reader = new DataInputStream(socket.getInputStream());
            DataOutputStream writer = new DataOutputStream(socket.getOutputStream());

            // Request a file from the server
            System.out.println("Requesting file: " + fileName);
            writer.writeUTF(fileName);

            // Receive the file from the server
            receiveFile(fileName, reader);

            // Close the connection
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveFile(String fileName, DataInputStream reader) throws IOException {
        long length = reader.readLong();

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
        } else {
            System.out.println("File not found");
        }
    }
}
