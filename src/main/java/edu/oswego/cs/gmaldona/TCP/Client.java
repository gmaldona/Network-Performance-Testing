package edu.oswego.cs.gmaldona.TCP;

import edu.oswego.cs.gmaldona.util.Constants;
import edu.oswego.cs.gmaldona.util.NetworkingTools;

import java.net.*;
import java.io.*;

public class Client {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public Client() throws IOException {
        socket = new Socket(NetworkingTools.SERVER_HOST, Constants.PORT);
        out    = new PrintWriter(socket.getOutputStream(), true);
        in     = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void sendMessage(String payload) throws IOException {
        //System.out.println("Message To Server: " + payload);
        out.println(payload);
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