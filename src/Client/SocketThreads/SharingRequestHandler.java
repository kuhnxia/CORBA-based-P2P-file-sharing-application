package Client.SocketThreads;

import Client.Helpers.LocalFileHelper;

import java.io.*;
import java.net.Socket;
import java.net.SocketAddress;

class SharingRequestHandler implements Runnable {
    private Socket clientSocket;
    private String serverIP;
    private int serverPort;

    public SharingRequestHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.serverIP = clientSocket.getLocalAddress().getHostAddress();
        this.serverPort = clientSocket.getLocalPort();
    }

    @Override
    public void run() {
        try {
            DataInputStream reader = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream writer = new DataOutputStream(clientSocket.getOutputStream());

            // Read the requested file name from the client
            String fileName = reader.readUTF();
            System.out.printf("A client requested file: %s by %s:%d\n",
                    fileName, serverIP, serverPort);

            // Send the requested file to the client
            sendFile(fileName, writer);

            // Close the connection
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendFile(String fileName, DataOutputStream writer) throws IOException {
        File file = LocalFileHelper.createNewFileForSending(fileName,
                serverIP, serverPort);

        if (file.exists()) {
            long length = file.length();
            writer.writeLong(length);

            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[4096];

                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    writer.write(buffer, 0, bytesRead);
                }
            }
        } else {
            // If the file does not exist, send an error message
            writer.writeLong(-1);
        }
    }
}
