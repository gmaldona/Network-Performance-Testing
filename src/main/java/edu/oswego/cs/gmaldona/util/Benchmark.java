package edu.oswego.cs.gmaldona.util;

import edu.oswego.cs.gmaldona.TCP.TCPClient;
import edu.oswego.cs.gmaldona.UDP.UDPClient;

import java.io.IOException;
import java.util.HashMap;

public class Benchmark {

    public static void main(String[] args) throws IOException, InterruptedException {
        //System.out.println("Throughput Measurements");
        //TCPBenchmark.throughput().forEach( (key, value) ->System.out.println(key + ":\t" + value + "\tbps" ) );
        //System.out.println("\nLatency Measurements");
        //TCPBenchmark.averageLatency().forEach( (key, value) -> System.out.println("Packet size of " + key + ":\tWith a latency (seconds) of\t" + value) );
        //terminateTCPServer();
        averageLatency(new TCPClient()).forEach( (key, value) -> System.out.println("Packet size of " + key + ":\tWith a latency (seconds) of\t" + value) );
        throughput(new TCPClient()).forEach((key, value) -> System.out.println(key + ":\t" + value + "\tbps"));
        ;
    }

    public static HashMap<String, Double> throughput(Client client) throws IOException, InterruptedException {
        HashMap<String, Double> benchmarkResults = new HashMap<>();
        HashMap<Integer, Integer> testingPackets = new HashMap<>();
        testingPackets.put(1024, 1024);
        testingPackets.put(2048, 512);
        testingPackets.put(4096, 256);
        for (Integer round : testingPackets.keySet()) {
            client.sendMessage("~Throughput");
            Thread.sleep(500);
            String payload = NetworkingTools.generateRandomPayload(testingPackets.get(round));
            long startTime = System.nanoTime();
            for (int trial = 0; trial < round; trial++) {
                client.sendMessageForThroughput(payload);
            }
            client.sendMessage("~stop");
            double elapsedTime = (System.nanoTime() - startTime) / 1e9;
            benchmarkResults.put(round + "X" + testingPackets.get(round), (payload.length() * 8) / elapsedTime);
        }
        return benchmarkResults;
    }

    public static HashMap<Integer, Double> averageLatency(Client client) throws IOException, InterruptedException {
        double[] trialResults = new double[Constants.TRIALS];
        HashMap<Integer, Double> benchmarkResults = new HashMap<>();

        for (int payloadLength : Constants.PAYLOAD_LENGTHS) {
            for (int trial = 0; trial < Constants.TRIALS; trial++) {
                String payload = NetworkingTools.generateRandomPayload(payloadLength);
                client.sendMessage("~Latency");
                Thread.sleep(500);
                long startBenchmarkTime = System.nanoTime();
                client.sendMessageForLatency(payload);
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


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    private static class TCPBenchmark {
//
//        public static HashMap<String, Double> throughput() throws IOException, InterruptedException {
//            HashMap<String, Double> benchmarkResults = new HashMap<>();
//            HashMap<Integer, Integer> testingPackets = new HashMap<>();
//            testingPackets.put(1024, 1024);
//            testingPackets.put(2048, 512);
//            testingPackets.put(4096, 256);
//            for (Integer round : testingPackets.keySet()) {
//                TCPClient client = new TCPClient();
//                client.sendMessage("~Throughput");
//                Thread.sleep(500);
//                String payload = NetworkingTools.generateRandomPayload(testingPackets.get(round));
//                long startTime = System.nanoTime();
//                for (int trial = 0; trial < round; trial++) {
//                    client.sendMessageForThroughput(payload);
//                }
//                client.sendMessage("~stop");
//                double elapsedTime = (System.nanoTime() - startTime)/1e9;
//                benchmarkResults.put(round+"X"+testingPackets.get(round), (payload.length()*8)/elapsedTime);
//            }
//            return benchmarkResults;
//
//        }
//
//        public static HashMap<Integer, Double> averageLatency() throws IOException, InterruptedException {
//            double[] trialResults = new double[Constants.TRIALS];
//            HashMap<Integer, Double> benchmarkResults = new HashMap<>();
//
//            for (int payloadLength : Constants.PAYLOAD_LENGTHS) {
//                for (int trial = 0; trial < Constants.TRIALS; trial++) {
//                    String payload = NetworkingTools.generateRandomPayload(payloadLength);
//                    TCPClient client = new TCPClient();
//                    client.sendMessage("~Latency");
//                    Thread.sleep(500);
//                    long startBenchmarkTime = System.nanoTime();
//                    client.sendMessageForLatency(payload);
//                    double RTT = (System.nanoTime() - startBenchmarkTime) / 1e9;
//                    trialResults[trial] = RTT;
//
//                    client.close();
//                }
//                benchmarkResults.put(payloadLength, getAverageArray(trialResults));
//            }
//            return benchmarkResults;
//        }
//
//        private static double getAverageArray(double[] trials) {
//            double sum = 0.0;
//            for (double trial : trials) {
//                sum += trial;
//            }
//            return sum / trials.length;
//        }
//
//    }
//
//    private static class UDPBenchmark {
//
//        public static HashMap<String, Double> throughput() throws IOException, InterruptedException {
//            HashMap<String, Double> benchmarkResults = new HashMap<>();
//            HashMap<Integer, Integer> testingPackets = new HashMap<>();
//            testingPackets.put(1024, 1024);
//            testingPackets.put(2048, 512);
//            testingPackets.put(4096, 256);
//
//            for (Integer round : testingPackets.keySet()) {
//                UDPClient client = new UDPClient();
//                client.sendMessage("~Throughput");
//                Thread.sleep(500);
//                String payload = NetworkingTools.generateRandomPayload(testingPackets.get(round));
//                long startTime = System.nanoTime();
//                for (int trial = 0; trial < round; trial++) {
//                    client.sendMessageForThroughput(payload);
//                }
//                client.sendMessage("~stop");
//                double elapsedTime = (System.nanoTime() - startTime) / 1e9;
//                benchmarkResults.put(round+"X"+testingPackets.get(round), (payload.length()*8)/elapsedTime);
//
//            }
//        }
//
//        public static HashMap<Integer, Double> averageLatency() throws IOException, InterruptedException { }
//    }
//
//    private static void terminateTCPServer() throws IOException {
//        TCPClient client = new TCPClient();
//        client.sendMessage("~exit");
//    }
//
//    private static void terminateUDPServer() throws IOException {
//        UDPClient client = new UDPClient();
//        client.sendMessage("~exit");
//    }
//
//
//
//}
