package Server.Entity;

import java.util.Objects;

/**
 * The SharedFile class represents a file shared by clients in the P2P file sharing system.
 */
public class SharedFile {
    private int id;
    private String filename;
    private String ipAddress;
    private int port;

    /**
     * Constructs a SharedFile object with the specified filename, IP address, and port.
     *
     * @param filename  The name of the shared file.
     * @param ipAddress The IP address of the client sharing the file.
     * @param port      The port on which the client is sharing the file.
     */
    public SharedFile(String filename, String ipAddress, int port) {
        this.filename = filename;
        this.ipAddress = ipAddress;
        this.port = port;
    }

    // Getters and setters

    /**
     * Gets the ID of the shared file.
     *
     * @return The ID of the shared file.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the shared file.
     *
     * @param id The ID to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the filename of the shared file.
     *
     * @return The filename of the shared file.
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Sets the filename of the shared file.
     *
     * @param filename The filename to set.
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * Gets the IP address of the client sharing the file.
     *
     * @return The IP address of the client.
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * Sets the IP address of the client sharing the file.
     *
     * @param ipAddress The IP address to set.
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * Gets the port on which the client is sharing the file.
     *
     * @return The port number.
     */
    public int getPort() {
        return port;
    }

    /**
     * Sets the port on which the client is sharing the file.
     *
     * @param port The port number to set.
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Generates a string representation of the SharedFile object.
     *
     * @return A string representation of the object.
     */
    @Override
    public String toString() {
        return "SharedFile{" +
                "id=" + id +
                ", filename='" + filename + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", port=" + port +
                '}';
    }

    /**
     * Checks if this SharedFile object is equal to another object.
     *
     * @param o The object to compare.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SharedFile that = (SharedFile) o;
        return port == that.port &&
                filename.equals(that.filename) &&
                ipAddress.equals(that.ipAddress);
    }

    /**
     * Generates a hash code for the SharedFile object.
     *
     * @return The hash code value.
     */
    @Override
    public int hashCode() {
        return Objects.hash(filename, ipAddress, port);
    }
}
