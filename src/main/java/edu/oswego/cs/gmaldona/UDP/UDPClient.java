package edu.oswego.cs.gmaldona.UDP;

import edu.oswego.cs.gmaldona.util.Constants;

import java.net.*;
import java.io.*;

public class UDPClient {
    private DatagramSocket socket;
    private InetAddress address;
    private byte[] buffer;

    public UDPClient() throws UnknownHostException, SocketException {
        socket = new DatagramSocket();
        address = InetAddress.getByName(Constants.HOST);
        System.out.println("Sending Packets to: " + Constants.HOST);
    }

    public String sendEcho(String msg) throws IOException {
        buffer = msg.getBytes();
        DatagramPacket packet
                = new DatagramPacket(buffer, buffer.length, address, Constants.PORT);
        socket.send(packet);
        packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        String received = new String(
                packet.getData(), 0, packet.getLength());
        return received;
    }

    public void close() {
        socket.close();
    }

}
