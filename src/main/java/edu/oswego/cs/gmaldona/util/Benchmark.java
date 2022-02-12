package edu.oswego.cs.gmaldona.util;

import edu.oswego.cs.gmaldona.TCP.Client;

import java.io.IOException;
import java.util.HashMap;

public class Benchmark {

    public static void main(String[] args) throws IOException {
        TCPBenchmark.averageLatency().forEach( (key, value) -> System.out.println("Packet Size Of: " + key + ": " + value + " seconds latency") );
        terminateServer();
    }

    private static class TCPBenchmark {

        public static String throughput() throws IOException {
            StringBuilder benchmarkResults = new StringBuilder();

            for (int payloadLength : Constants.PAYLOAD_LENGTHS) {
                String payload = NetworkingTools.generateRandomPayload(payloadLength);
                Client client = new Client();
                long startBenchmarkTime = System.nanoTime();
                client.sendMessage(payload);
                double elapsedTime = (System.nanoTime() - startBenchmarkTime) / 1e9;

                double throughput = payloadSizeInBits(payload) / elapsedTime;
                benchmarkResults.append(throughput).append(" bps").append("/");

                client.close();
            }

            return benchmarkResults.toString();
        }

        public static HashMap<Integer, Double> averageLatency() throws IOException {
            double[] trialResults = new double[Constants.TRIALS];
            HashMap<Integer, Double> benchmarkResults = new HashMap<>();

            for (int payloadLength : Constants.PAYLOAD_LENGTHS) {
                for (int trial = 0; trial < Constants.TRIALS; trial++) {
                    String payload = NetworkingTools.generateRandomPayload(payloadLength);
                    Client client = new Client();
                    long startBenchmarkTime = System.nanoTime();
                    client.sendMessage(payload);
                    double RTT = (System.nanoTime() - startBenchmarkTime) / 1e9;
                    trialResults[trial] = RTT;

                    client.close();
                }
                benchmarkResults.put(payloadLength, getAverageArray(trialResults));
            }
            return benchmarkResults;
        }

        private static double getAverageArray(double[] trials) {
            double sum = 0.0;
            for (double trial : trials) {
                sum += trial;
            }
            return sum / trials.length;
        }

    }

    private static void terminateServer() throws IOException {
        Client client = new Client();
        client.sendMessage("~exit");
    }

    private static int payloadSizeInBits(String payload) {
        return payload.length() * 8;
    }

}
