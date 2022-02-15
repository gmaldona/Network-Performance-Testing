package edu.oswego.cs.gmaldona.UDP;

import edu.oswego.cs.gmaldona.util.Constants;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

public class Server {

    public static void main(String[] args) throws SocketException {
        new Connection();
    }

    private static class Connection {
        private DatagramSocket socket;
        private final byte[] buffer = new byte[256];

        public Connection() throws SocketException {
            System.out.println("-------======= Starting UDP Server -------=======");
            System.out.println(this);
            socket = new DatagramSocket(Constants.PORT);
            for (;;) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                try {
                    socket.receive(packet);
                } catch (IOException e) {e.printStackTrace();}

                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                packet = new DatagramPacket(buffer, buffer.length, address, port);
                String received = new String(packet.getData(), 0, packet.getLength());

                if (received.contains("~exit")) { break; }
                try {
                    System.out.println("Message from client: " + received);
                    socket.send(packet);
                } catch (IOException e) { e.printStackTrace(); }
                Arrays.fill(buffer, (byte) 0);
            }
            socket.close();
        }

        public void latencyTest() throws IOException {
            System.out.println("-------======= Latency Testing ... -------=======");

        }
        public void throughputTest() throws IOException {
            System.out.println("-------======= Throughput Testing ... -------=======");

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
