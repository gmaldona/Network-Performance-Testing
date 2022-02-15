package edu.oswego.cs.gmaldona.TCP;

import edu.oswego.cs.gmaldona.util.Constants;
import edu.oswego.cs.gmaldona.util.NetworkingTools;

import java.net.*;
import java.io.*;

public class TCPClient {

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

    public void sendMessage(String payload) throws IOException {
        //System.out.println("Sending payload:  " + payload);
        out.println(payload);
    }

    public void sendMessageForThroughput(String payload) throws IOException {
        out.println(payload);
        String receivedPayload = "";
        while (!receivedPayload.equals("Received")) {receivedPayload = in.readLine(); }
    }

    public boolean sendMessageForLatency(String payload) throws IOException {
        out.println(payload);
        String echoedPayload = in.readLine();
        //System.out.println("echoed" + echoedPayload);
        return payload.equals(echoedPayload);
    }


    public Socket getSocket() {
        return socket;
    }

    public void close() throws IOException {
        out.close();
        in.close();
        socket.close();
    }

}
