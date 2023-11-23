package Client.SocketThreads;

import Client.Helpers.LocalFileHelper;

import java.io.*;
import java.net.Socket;
import java.net.SocketAddress;

class SharingRequestHandler implements Runnable {
    private Socket clientSocket;

    public SharingRequestHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            DataInputStream reader = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream writer = new DataOutputStream(clientSocket.getOutputStream());

            // Read the requested file name from the client
            String fileName = reader.readUTF();
            /*
            String socketAddress = clientSocket.getLocalSocketAddress().toString();
            int port = clientSocket.getLocalPort();
            System.out.printf("Client %d requested file: %s\n", port, fileName);
            */
            System.out.printf("A client requested file: %s\n", fileName);

            // Send the requested file to the client
            sendFile(fileName, writer);

            // Close the connection
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendFile(String fileName, DataOutputStream writer) throws IOException {
        File file = LocalFileHelper.createNewFileForSending(fileName);

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
