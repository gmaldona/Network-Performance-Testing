package edu.oswego.cs.gmaldona.util;

import java.util.Random;

public class RandomPayload {

    public static String generate(int payloadLength) {
        StringBuilder sb = new StringBuilder();
        for (int bytes = 1; bytes <= payloadLength; bytes++) {
            sb.append((char)(new Random().nextInt(25) + 97));
        }
        return sb.toString();
    }

}
