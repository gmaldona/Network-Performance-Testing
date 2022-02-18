package edu.oswego.cs.gmaldona.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NetworkingTools {

    private static final int XOR_ENCODING_LENGTH = 8;
    private static final HashMap<String, String> networkingConf = getEncryptionKey();
    private static final String PRIVATE_ENCRYPTION_KEY = networkingConf.getOrDefault("KEY", "");
    public static final String SERVER_HOST = networkingConf.getOrDefault("SERVER", "127.0.0.1");

    public static HashMap<String, String> getEncryptionKey() {
        HashMap<String, String> config = new HashMap<>();
        try {
            File file = new File("netconf.txt");
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String[] parsedLine = scanner.nextLine().split("=");
                config.put(parsedLine[0], parsedLine[1]);
            }
        } catch (Exception e) {e.printStackTrace();}
        return config;
    }

    public static String generateRandomPayload(int payloadLength) {
        StringBuilder sb = new StringBuilder();
        for (int bytes = 1; bytes <= payloadLength; bytes++) {
            sb.append((char) (new Random().nextInt(25) + 97));
        }
        return sb.toString();
    }

    public static byte[] XOREncrypt(String payload) {

        ArrayList<Byte> encryptedBytes = new ArrayList<>();
        int XORCount = payload.length() / XOR_ENCODING_LENGTH;
        byte[] keyBytes = PRIVATE_ENCRYPTION_KEY.getBytes(StandardCharsets.UTF_8);
        for (int encryptionCount = 0; encryptionCount < XORCount; encryptionCount++) {
            byte[] payloadBytes = payload.substring(encryptionCount * XOR_ENCODING_LENGTH, (encryptionCount + 1) * XOR_ENCODING_LENGTH).getBytes(StandardCharsets.UTF_8);
            byte[] encryptedPayload = new byte[payloadBytes.length];
            for (int i = 0; i < encryptedPayload.length; i++) {
                encryptedPayload[i] = (byte) (payloadBytes[i] ^ keyBytes[i]);
            }
            for (byte b : encryptedPayload) {
                encryptedBytes.add(b);
            }
        }

        byte[] byteBuffer = new byte[encryptedBytes.size()];
        for (int b = 0; b < encryptedBytes.size(); b++) {
            byteBuffer[b] = encryptedBytes.get(b);
        }

        return byteBuffer;
    }

    public static void saveData(Benchmark.Protocols protocol, String clientHostname, String serverHostname,
                                HashMap<String, Double> throughputResults, HashMap<Integer, Double> latencyResults) {
        String throughputFilename = String.format("%s Throughput for %s to %s.csv", protocol, clientHostname, serverHostname);
        String latencyFilename = String.format("%s Latency for %s to %s.csv", protocol, clientHostname, serverHostname);

        try {
            StringBuilder sb = new StringBuilder();
            for (String key : throughputResults.keySet().stream().sorted().collect(Collectors.toList())) {
                sb.append(key).append(",").append(throughputResults.get(key)).append("\n");
            }

            File throughputData = new File("data/", throughputFilename);
            FileWriter writer = new FileWriter(throughputData);
            writer.write(sb.toString());
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            StringBuilder sb = new StringBuilder();
            for (Integer key : latencyResults.keySet().stream().sorted().collect(Collectors.toList())) {
                sb.append(key).append(",").append(latencyResults.get(key)).append("\n");
            }

            File latencyData = new File("data/", latencyFilename);
            FileWriter writer = new FileWriter(latencyData);
            writer.write(sb.toString());
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String XORDecrypt(String payload) {
        return "";
    }

}
