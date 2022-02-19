package edu.oswego.cs.gmaldona.TCP;

import edu.oswego.cs.gmaldona.util.Constants;
import edu.oswego.cs.gmaldona.util.NetworkingTools;

import java.io.*;
import java.net.*;

public class Server {

    public static void main(String[] args) throws IOException {
        new Connection();
    }

    private static class Connection {
        private ServerSocket serverSocket;
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        public Connection() throws IOException {
            System.out.println("-------======= Starting TCP Server -------=======");
            System.out.println(this);
            for (;;) {
                serverSocket = new ServerSocket(Constants.PORT);
                socket = serverSocket.accept();
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String testType = in.readLine();
                if (testType.equals("~Throughput")) throughputTest();
                else if (testType.equals("~Latency") ) latencyTest();
                else if (testType.equals("~exit")) { break; }
                serverSocket.close();
            }

        }

        public void latencyTest() throws IOException {
            System.out.println("-------======= Latency Testing ... -------=======");
            String payload = "";
            while (payload.equals("")) { payload = in.readLine(); }
            if (Constants.XOR_DEBUG&&payload.length()==8) { System.out.println(payload); }
            out.println(payload);
            socket.close();
        }
        public void throughputTest() throws IOException {
            System.out.println("-------======= Throughput Testing ... -------=======");
            for (;;) {
                String payload = in.readLine();
                if (Constants.XOR_DEBUG&&payload.length()==8) { System.out.println(payload); }
                if (payload.equals("~stop")) break;
                out.println("Received");
            }
        }

        public String toString() {
            try {
                return "Server " + Inet4Address.getLocalHost().getHostAddress() + " started on PORT=" + Constants.PORT;
            } catch (UnknownHostException e) {
                e.printStackTrace();
                return "Server on PORT=" + Constants.PORT;
            }
        }
    }
}
