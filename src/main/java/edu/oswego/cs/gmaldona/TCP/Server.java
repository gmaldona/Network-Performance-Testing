package edu.oswego.cs.gmaldona.TCP;

import edu.oswego.cs.gmaldona.util.Constants;
import edu.oswego.cs.gmaldona.util.NetworkingTools;

import java.io.*;
import java.net.*;

public class Server {

    public static void main(String[] args) throws IOException {
        new C();
    }

    private static class C {
        private ServerSocket serverSocket;
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        public C() throws IOException {
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
            out.println(payload);
            socket.close();
        }
        public void throughputTest() throws IOException {
            System.out.println("-------======= Throughput Testing ... -------=======");
            for (;;) {
                String payload = in.readLine();
                if (payload.equals("~stop")) break;
                out.println("Received");
            }
        }
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
                System.out.println("starting server");
                socket = serverSocket.accept();
                System.out.println("Client Connection.");
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String testingType = listenForPayload();

                if (testingType.equals("~Throughput")) {

                } else if (testingType.equals("~Latency")) {
                   latencyTest();
                }

                if (checkForShutdown("~exit")) { System.out.println("EXITING");socket.close() ; break ; };
            }
            serverSocket.close();
        }

//        public double throughputTest() {
//
//        }

        public void latencyTest() throws IOException {

            System.out.println("Latency Test");
            String payload = "";
            while (payload.equals("")) { payload = in.readLine(); }
            out.println(payload);

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
