package Server.Entity;

import java.util.Objects;
public class SharedFile {
    private int id;
    private String filename;
    private String ipAddress;
    private int port;
    public SharedFile(String filename, String ipAddress, int port) {
        this.filename = filename;
        this.ipAddress = ipAddress;
        this.port = port;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }


    @Override
    public String toString() {
        return "SharedFile{" +
                "id=" + id +
                ", filename='" + filename + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", port=" + port +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SharedFile that = (SharedFile) o;
        return port == that.port &&
                filename.equals(that.filename) &&
                ipAddress.equals(that.ipAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filename, ipAddress, port);
    }
}
