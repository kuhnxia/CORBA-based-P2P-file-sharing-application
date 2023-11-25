# CORBA-based-P2P-file-sharing-application

- This project includes two programs.
- One for CORBA File Share Register Server
- Another for CORBA File Share Client with Sockets.
- The two programs can be separated and work well.

## Quick Start

### A. Start FileRegisterServer on a computer.

- Waiting Maven auto-building completed.
- Choose Java 8 as the Project JDK.
- Installed MySQL and Workbench.
- Prepared a MySQL connection with hostname, port, username, and password.
- Place the values of the following three constants in [DatabaseConnector.java](src/main/java/Server/Connector/DatabaseConnector.java) to your MySQL connection setting.
```java
private static final String URL = "jdbc:mysql://localhost:3306/";
private static final String USERNAME = "root";
private static final String PASSWORD = "MyNewPass";
```
- Then Run [FileRegisterServer.java](src/main/java/Server/FileRegisterServer.java)
- Note the server IP of the FileRegisterServer.

### B. Start one or more FileShareClient on the same or other computers at the same local networks.

- Waiting Maven auto-building completed.
- Choose Java 8 as the Project JDK.
- Place the value of the constant in [FileShareClient.java](src/main/java/Client/FileShareClient.java) to your real CORBA server IP.
```java
private static final String CORBA_SERVER_IP_ADDRESS = "198.18.0.1";
```
- Then run [FileShareClient.java](src/main/java/Client/FileShareClient.java).
- Go to the root of your computer to find the shared and received files i.e. 
```shell
Users/rocky/CORBA-based-P2P-file-sharing-application
```
- If you are facing problems, please read [Setup Details](#setup-details) for solutions.

## Setup Details

### A. Maven
- This project is using Maven for Dependency management!
- See dependencies in [pom.xml](pom.xml)

### B. JDK
- Java 8 is the best choice because the CORBA module has been removed from Java 11 or later.
- Some JDK versions report errors.
- Java 8.0.392-amzn downloaded from SDKMan is working fine for me.

### C. JDBC

#### 1. JDBC Driver
- `mysql-connector-java` has already added to [pom.xml](pom.xml). You can change the version.

#### 2. Ensure MySQL and WorkBench installed on your computer.

#### 3. Change the MySQL setting to your MySQL connection setting.
- Place the values of the following three constants in [DatabaseConnector.java](src/main/java/Server/Connector/DatabaseConnector.java).
```java
private static final String URL = "jdbc:mysql://localhost:3306/";
private static final String USERNAME = "root";
private static final String PASSWORD = "MyNewPass";
```

### D. CORBA

#### 1. Generate the stub and skeleton code (Already done)
- There is an `idlj` program that comes with the JDK for generating the stub and skeleton code in Java
- Use the following command to execute [FileShare.idl](src/main/java/FileShare.idl)
```shell
 idlj -fall src/main/java/FileShare.idl
```

#### 2. Running the CORBA Server 
- It has been added to the Java main method
- No need execution in Command Line
- No need adding argument in Run/Debug Configurations
- **However, you need add your real CORBA server IP to [FileShareClient.java](src/main/java/Client/FileShareClient.java)**
##### a) Start the ORB server
- [FileShareClient.java](src/main/java/Client/FileShareClient.java)
```java
Runtime.getRuntime().exec("orbd -ORBInitialPort 1050 -ORBInitialHost localhost");
```
```java
// Set the ORB properties programmatically
java.util.Properties props = new java.util.Properties();
props.put("org.omg.CORBA.ORBInitialPort", "1050");
props.put("org.omg.CORBA.ORBInitialHost", "localhost");
// create and initialize the ORB with the specified properties
ORB orb = ORB.init(args, props);
```
##### b) Get the ORB server
- **Change to your real CORBA server IP in [FileShareClient.java](src/main/java/Client/FileShareClient.java)**
```java
private static final String CORBA_SERVER_IP_ADDRESS = "198.18.0.1";
System.out.printf("Get the file register server from %s:1050\n", CORBA_SERVER_IP_ADDRESS);
fileShare = CORBAConnector.getFileShareServer(CORBA_SERVER_IP_ADDRESS);
```
- Then following codes in [CORBAConnector.java](src/main/java/Client/Connector/CORBAConnector.java) will work.
```java
// Set the ORB properties programmatically
java.util.Properties props = new java.util.Properties();
props.put("org.omg.CORBA.ORBInitialPort", "1050");
props.put("org.omg.CORBA.ORBInitialHost", ip);
// create and initialize the ORB with the specified properties
ORB orb = ORB.init(new String[]{}, props);
```
### E. Sockets

#### 1. Server Socket
- Server socket thread will be running at the backend until you stop the client program.
- It can be connected by different IPs if you have more than one interfaces, such as Ethernet, Wi-Fi, VPN, Cellular.
- The client will ask you to choose the correct IP you will use in your local network to test.
- There will be a new child thread to interact with a new client socket.

#### 2. Client Socket
- When request file sharing, a new client socket thread will be created and connect to the server socket.
- The program will continue until last client socket thread dead.
