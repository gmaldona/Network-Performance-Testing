package edu.oswego.cs.gmaldona.UDP;

import edu.oswego.cs.gmaldona.util.Constants;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

public class Server {

    public static void main(String[] args) throws IOException {
        new Connection();
    }

    private static class Connection {
        private DatagramSocket socket;
        private final byte[] buffer = new byte[256];

        public Connection() throws IOException {
            System.out.println("-------======= Starting UDP Server -------=======");
            System.out.println(this);
            socket = new DatagramSocket(Constants.PORT);
            for (;;) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                try { socket.receive(packet); }
                catch (IOException e) {e.printStackTrace();}

                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                packet = new DatagramPacket(buffer, buffer.length, address, port);
                String testType = new String(packet.getData(), 0, packet.getLength());

                if (testType.contains("~Throughput")) throughputTest();
                else if (testType.contains("~Latency") ) latencyTest();
                else if (testType.contains("~exit")) { break; }

                Arrays.fill(buffer, (byte) 0);
            }
            socket.close();
        }

        public void latencyTest() throws IOException {
            System.out.println("-------======= Latency Testing ... -------=======");
            Arrays.fill(buffer, (byte) 0);
            while (buffer[0] == (byte) 0) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                try { socket.receive(packet); }
                catch (IOException e) { e.printStackTrace(); }

                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                packet = new DatagramPacket(buffer, buffer.length, address, port);
                String payload = new String(packet.getData(), 0, packet.getLength());
                if (Constants.XOR_DEBUG && payload.length()==8) { System.out.println(payload); }
                try { socket.send(packet); }
                catch (IOException e) {e.printStackTrace();}

            }
        }
        public void throughputTest() throws IOException {
            System.out.println("-------======= Throughput Testing ... -------=======");
            Arrays.fill(buffer, (byte) 0);
            String bufferStr = new String(buffer, 0, buffer.length);
            while (! bufferStr.contains("~stop")) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                try { socket.receive(packet); }
                catch (IOException e) { e.printStackTrace(); }

                InetAddress address = packet.getAddress();
                int port = packet.getPort();

                String newBuff = "Received";
                packet = new DatagramPacket(newBuff.getBytes(), newBuff.length(), address, port);
                if (Constants.XOR_DEBUG&&bufferStr.length()==8) { System.out.println(bufferStr); }
                try { socket.send(packet); }
                catch (IOException e) {e.printStackTrace();}
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
