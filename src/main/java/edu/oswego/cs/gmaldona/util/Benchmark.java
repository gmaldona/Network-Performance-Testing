package edu.oswego.cs.gmaldona.util;

import edu.oswego.cs.gmaldona.TCP.Client;

import java.io.IOException;

public class Benchmark {

    public static void main(String[] args) throws IOException {
        TCP();
    }

    public static String TCP() throws IOException {

        for (int payloadLength : Constants.payloadLengths) {
            Client client = new Client();
            client.sendMessage(RandomPayload.generate(payloadLength));
        }

        terminateServer();

        return "";
    }

    private static void terminateServer() throws IOException {
        Client client = new Client();
        client.sendMessage("~exit");
    }

}
