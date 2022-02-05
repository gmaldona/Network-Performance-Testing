package edu.oswego.cs.gmaldona.TCP;

import edu.oswego.cs.gmaldona.util.Constants;

import java.net.*;
import java.io.*;

public class Client {

    public static void main(String[] args) throws IOException {
        Connection clientConnection = new Connection();
        System.out.println("Server Connection To.");
    }

    private static class Connection {
        private Socket socket;

        public Connection() throws IOException {
            socket = new Socket(Constants.HOST, Constants.PORT);
        }
    }

}
