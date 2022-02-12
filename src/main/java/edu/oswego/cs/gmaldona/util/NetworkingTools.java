package edu.oswego.cs.gmaldona.util;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NetworkingTools {

    private static final int XOR_ENCODING_LENGTH = 8;
    private static final HashMap<String, String> networkingConf = getEncryptionKey();
    private static final String PRIVATE_ENCRYPTION_KEY = networkingConf.getOrDefault("KEY", "");
    public static final String SERVER_HOST = networkingConf.getOrDefault("SERVER", "127.0.0.1");

    private static HashMap<String, String> getEncryptionKey() {
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

    public static String convertPayloadToBinary(String payload) {
        StringBuilder sb = new StringBuilder();
        for (int index = 0; index < payload.length(); index++ ) {
            StringBuilder binary = new StringBuilder();
            int binaryLength = Integer.toBinaryString(payload.charAt(index)).length();
            if (binaryLength < XOR_ENCODING_LENGTH) {
                for (int fillZeros = 0; fillZeros < XOR_ENCODING_LENGTH - binaryLength; fillZeros++) { binary.append("0"); }
            }
            sb.append(binary.append(Integer.toBinaryString(payload.charAt(index))).toString());
        }
        return sb.toString();
    }

    public static String generateRandomPayload(int payloadLength) {
        StringBuilder sb = new StringBuilder();
        for (int bytes = 1; bytes <= payloadLength; bytes++) {
            sb.append((char) (new Random().nextInt(25) + 97));
        }
        return sb.toString();
    }

    public static String XOREncrypt(String payload) {
        StringBuilder encryptedString = new StringBuilder();
        int XORCount = payload.length() / XOR_ENCODING_LENGTH;
        byte[] keyBytes = PRIVATE_ENCRYPTION_KEY.getBytes(StandardCharsets.UTF_8);
        for (int encryptionCount = 0; encryptionCount < XORCount; encryptionCount++) {
            byte[] payloadBytes = payload.substring(encryptionCount * XOR_ENCODING_LENGTH, (encryptionCount + 1) * XOR_ENCODING_LENGTH).getBytes(StandardCharsets.UTF_8);
            byte[] encryptedPayload = new byte[payloadBytes.length];
            for (int i = 0; i < encryptedPayload.length; i++) {
                encryptedPayload[i] = (byte) (payloadBytes[i] ^ keyBytes[i]);
            }
            for (byte b : encryptedPayload) {
                encryptedString.append((char) b);
            }

        }
        return encryptedString.toString() ;
    }

    public static String XORDecrypt(String payload) {
        return "";
    }

}
