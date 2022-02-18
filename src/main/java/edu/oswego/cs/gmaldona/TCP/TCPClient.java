package edu.oswego.cs.gmaldona.TCP;

import edu.oswego.cs.gmaldona.util.Client;
import edu.oswego.cs.gmaldona.util.Constants;
import edu.oswego.cs.gmaldona.util.NetworkingTools;

import java.net.*;
import java.io.*;

public class TCPClient extends Client {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public TCPClient() {
        try {
            socket = new Socket(NetworkingTools.SERVER_HOST, Constants.PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception e) {e.printStackTrace();}
    }

    @Override
    public void sendMessage(String payload) throws IOException {
        out.println(payload);
    }

    @Override
    public boolean sendMessageForThroughput(String payload) throws IOException {
        out.println(payload);
        String receivedPayload = "";
        while (!receivedPayload.equals("Received")) {receivedPayload = in.readLine(); }
        return true;
    }

    @Override
    public boolean sendMessageForLatency(String payload) throws IOException {

        out.println(payload);
        String echoedPayload = in.readLine();
        String decryptedPayload = new String(NetworkingTools.XOREncrypt(echoedPayload));

        if (Constants.XOR_DEBUG && payload.length()==8) {
            System.out.println("Message sent:\t\t" + payload);
            System.out.println("Message received:\t" + decryptedPayload + "\n");
        }
        return payload.equals(decryptedPayload);
    }

    @Override
    public void close() throws IOException {
        out.close();
        in.close();
        socket.close();
    }

}
