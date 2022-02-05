package edu.oswego.cs.gmaldona.util;

import edu.oswego.cs.gmaldona.TCP.Client;

import java.io.IOException;

public class Benchmark {

    public static void main(String[] args) throws IOException {
        System.out.println(TCP());
    }

    public static String TCP() throws IOException {

        StringBuilder benchmarkResults = new StringBuilder();

        for (int payloadLength : Constants.payloadLengths) {
            String payload = RandomPayload.generate(payloadLength);
            Client client = new Client();
            long startBenchmarkTime = System.nanoTime();
            client.sendMessage(payload);
            double elapsedTime = (System.nanoTime() - startBenchmarkTime) / 1e9;

            double throughput = (payloadSizeInBits(payload)/1e6) / elapsedTime;
            benchmarkResults.append(throughput).append(" Mbps").append("/");
        }

        terminateServer();

        return benchmarkResults.toString();
    }

    private static void terminateServer() throws IOException {
        Client client = new Client();
        client.sendMessage("~exit");
    }

    private static int payloadSizeInBits(String payload) {
        return payload.length() * 8;
    }

}
