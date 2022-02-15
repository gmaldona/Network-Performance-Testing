package edu.oswego.cs.gmaldona.UDP;

import edu.oswego.cs.gmaldona.util.Client;
import edu.oswego.cs.gmaldona.util.Constants;

import java.net.*;
import java.io.*;

public class UDPClient extends Client {
    private DatagramSocket socket;
    private InetAddress address;
    private byte[] buffer;

    public UDPClient() throws UnknownHostException, SocketException {
        socket = new DatagramSocket();
        address = InetAddress.getByName(Constants.HOST);
        System.out.println("Sending Packets to: " + Constants.HOST);
    }

    public void sendMessage(String payload) throws IOException {
        buffer = payload.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, Constants.PORT);
        socket.send(packet);
    }

    public boolean sendMessageForThroughput(String payload) throws IOException {
        return sendAndReceive(payload).contains("Received");
    }

    public boolean sendMessageForLatency(String payload) throws IOException {
        return sendAndReceive(payload).equals(payload);
    }

    private String sendAndReceive(String payload) throws IOException {
        buffer = payload.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, Constants.PORT);
        socket.send(packet);
        packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        return new String(packet.getData(), 0, packet.getLength());
    }

    public void close() throws IOException {socket.close();}

}
