# CORBA-based-P2P-file-sharing-application

- This project includes two programs.
- One for CORBA File Share Register Server
- Another for CORBA File Share Client with Sockets.

## Quick Start

### Start FileRegisterServer on a computer.

- Waiting Maven auto-building completed.
- Choose Java 8 as the Project JDK.
- Installed MySQL and Workbench.
- Prepared a MySQL connection with hostname, port, username, and password.
- Place the values of the following three constants in [DatabaseConnector.java](src/main/java/Server/Connector/DatabaseConnector.java) to your MySQL connection setting.
```
private static final String URL = "jdbc:mysql://localhost:3306/";
private static final String USERNAME = "root";
private static final String PASSWORD = "MyNewPass";
```
- Then Run [FileRegisterServer.java](src/main/java/Server/FileRegisterServer.java)
- Note the server IP of the FileRegisterServer.

### Start one or more FileShareClient on the same or other computers at the same local networks.

- Waiting Maven auto-building completed.
- Choose Java 8 as the Project JDK.
- Place the value of the constant in [FileShareClient.java](src/main/java/Client/FileShareClient.java) to your real server IP.
```
private static final String CORBA_SERVER_IP_ADDRESS = "198.18.0.1";
```
- Then run [FileShareClient.java](src/main/java/Client/FileShareClient.java).
- If you are facing problems, please read [Setup Details](#setup-details) for solutions.

## Setup Details

### A Maven
- This project is using Maven for Dependency management!
- See dependencies in `pom.xml`

### A. JDK

#### 1. JDK choice

- Java 8 is the best choice because the CORBA module has been removed from Java 11 or later.
- Some JDK versions report errors.
- Java 8.0.392-amzn from SDKMan is working fine for me.

### B. JDBC

#### 1. Add JDBC Driver to the Project

- Right-click on your project in the Project Explorer on the left side.
- Choose "Open Module Settings" or press `F4` to open the Project Structure.
- In the Project Structure window, select "Modules" on the left.
- Click on the `Dependencies` tab.
- Click on the `+` button to add a new dependency.
- Choose `JARs or directories...`
- Navigate to the project folder, select the JAR file (e.g., `mysql-connector-j-8.2.0.jar`), and click `OK`.
- Click `OK` again to close the Project Structure window.

### 2. Install MySQL and WorkBench on your computer.

### 3. Change the values of the following three constants to your local setting.



### C. CORBA

#### 1. Generate the stub and skeleton code (Already done)

`FileShare.idl`:
```aidl
module Server {
  module CORBA {
    interface FileShare {
        string registerFile(in string fileName, in string ipAddress, in long port);
        string cancelSharing(in string fileName, in string ipAddress, in long port);
        string findSharedFiles(in string filename);
        string getSocketAddressById(in long id);
    };
  };
};
```

There is an idlj program that comes with the JDK for generating the stub and skeleton code in Java
```shell
 idlj -fall FileShare.idl
```
The java files generated by the idlj program are under `src/Server/CORBA` package.

#### 3. Running the application
1. Start the ORB server
    ```shell
    orbd -ORBInitialPort 1050 -ORBInitialHost localhost
    ```

2. Add the below program argument when you create a new server/client application at **Run/Debug Configurations**.
    ```shell
    -ORBInitialPort 1050 -ORBInitialHost localhost
    ```

