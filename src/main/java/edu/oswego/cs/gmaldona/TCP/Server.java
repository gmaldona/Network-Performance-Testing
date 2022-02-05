package edu.oswego.cs.gmaldona.TCP;

import edu.oswego.cs.gmaldona.util.Constants;

import java.io.*;
import java.net.*;

public class Server {

    public static void main(String[] args) throws IOException {
        Connection serverConnection = new Connection();
        System.out.println("Client Connection.");

    }

    private static class Connection {
        private ServerSocket serverSocket;
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        public Connection() throws IOException {
            System.out.println(this);
            serverSocket = new ServerSocket(Constants.PORT);
            for (;;) {
                socket = serverSocket.accept();
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String payload = listenForPayload();
                if (checkForShutdown(payload)) { socket.close() ; break ; };
                //System.out.println("Message from Client: " + payload);
                socket.close();
            }
            serverSocket.close();
        }

        public String listenForPayload() throws IOException {
            String payload = "";
            while (payload.equals("")) {
                payload = in.readLine();
            }
            return payload;
        }

        public String toString() {
            try {
                return "Server " + Inet4Address.getLocalHost().getHostAddress() + " started on PORT=" + Constants.PORT;
            } catch (UnknownHostException e) {
                e.printStackTrace();
                return "Server on PORT=" + Constants.PORT;
            }
        }

        public boolean checkForShutdown(String payload) {
            return payload.equals("~exit");
        }
    }

}
