package edu.oswego.cs.gmaldona.TCP;

import edu.oswego.cs.gmaldona.util.Constants;

import java.io.*;
import java.net.*;

public class Server {

    public static void main(String[] args) throws IOException {
        System.out.println("Server " + Inet4Address.getLocalHost().getHostAddress() + " started on PORT=" + Constants.PORT);
        Connection serverConnection = new Connection();
        System.out.println("Client Connection.");

    }

    private static class Connection {
        private ServerSocket serverSocket;
        private Socket socket;

        public Connection() throws IOException {
            serverSocket = new ServerSocket(Constants.PORT);
            socket = serverSocket.accept();
        }
    }

}
