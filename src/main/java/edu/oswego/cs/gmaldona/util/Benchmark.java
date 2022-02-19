package edu.oswego.cs.gmaldona.util;

import edu.oswego.cs.gmaldona.TCP.TCPClient;
import edu.oswego.cs.gmaldona.UDP.UDPClient;

import java.io.IOException;
import java.net.InetAddress;
import java.text.DecimalFormat;
import java.util.HashMap;

public class Benchmark {

    public static void main(String[] args) throws IOException, InterruptedException {

        System.out.println("-------======= Starting " + args[0] +" Benchmark ... -------=======");
        Protocols protocols = null;
        if (args[0].equals("TCP"))      protocols = Benchmark.Protocols.TCP;
        else if (args[0].equals("UDP")) protocols = Benchmark.Protocols.UDP;
        else { System.out.println("Client Protocol Not Selected ... Exiting ..."); System.exit(1); }

        HashMap<Integer, Double> latencyMeasurements = averageLatency(protocols);
        HashMap<String, Double> throughputMeasurements = throughput(protocols);

        latencyMeasurements.forEach( (key, value) -> System.out.println("Packet size of " + key + ":\tWith a latency (seconds) of\t" + Constants.dflat.format(value)) );
        Thread.sleep(1000);
        throughputMeasurements.forEach((key, value) -> System.out.println(key + ":\t" + Constants.dfthru.format(value) + "\tbps"));

        NetworkingTools.saveData(Protocols.TCP, InetAddress.getLocalHost().getHostAddress(), Constants.HOST, throughputMeasurements, latencyMeasurements);

    }

    public static HashMap<String, Double> throughput(Protocols protocols) throws IOException, InterruptedException {
        HashMap<String, Double> benchmarkResults = new HashMap<>();
        HashMap<Integer, Integer> testingPackets = new HashMap<>();
        testingPackets.put(1024, 1024);
        testingPackets.put(2048, 512);
        testingPackets.put(4096, 256);
        for (Integer round : testingPackets.keySet()) {
            Client client = protocols.equals(Benchmark.Protocols.TCP) ? new TCPClient() : new UDPClient();
            client.sendMessage("~Throughput");
            Thread.sleep(500);
            String payload = NetworkingTools.generateRandomPayload(testingPackets.get(round));
            long startTime = System.nanoTime();
            for (int trial = 0; trial < round; trial++) {
                String encryptedPayload = new String(NetworkingTools.XOREncrypt(payload));
                if (Constants.XOR_DEBUG && payload.length()==8) { System.out.println("Unencrypted Payload:\t" + payload); }
                client.sendMessageForThroughput(encryptedPayload);
            }
            client.sendMessage("~stop");
            double elapsedTime = (System.nanoTime() - startTime) / 1e9;
            benchmarkResults.put(round + "X" + testingPackets.get(round), ( round * (payload.length() * 8) ) / elapsedTime);
        }
        return benchmarkResults;
    }

    public static HashMap<Integer, Double> averageLatency(Protocols protocols) throws IOException, InterruptedException {
        double[] trialResults = new double[Constants.TRIALS];
        HashMap<Integer, Double> benchmarkResults = new HashMap<>();

        for (int payloadLength : Constants.PAYLOAD_LENGTHS) {
            for (int trial = 0; trial < Constants.TRIALS; trial++) {
                String payload = NetworkingTools.generateRandomPayload(payloadLength);
                String encryptedPayload = new String(NetworkingTools.XOREncrypt(payload));
                if (Constants.XOR_DEBUG && payload.length()==8) { System.out.println("Unencrypted Payload:\t" + payload); }
                Client client = protocols.equals(Benchmark.Protocols.TCP) ? new TCPClient() : new UDPClient();
                client.sendMessage("~Latency");
                Thread.sleep(500);
                long startBenchmarkTime = System.nanoTime();
                client.sendMessageForLatency(encryptedPayload);
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

    public enum Protocols { TCP, UDP }
}
