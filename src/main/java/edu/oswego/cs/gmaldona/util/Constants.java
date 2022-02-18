package edu.oswego.cs.gmaldona.util;

public class Constants {
    public static final int PORT = 26923;
    public static final String HOST = NetworkingTools.SERVER_HOST;
    public static final int[] PAYLOAD_LENGTHS = new int[]{ 8, 64, 256, 1024 };
    public static final int TRIALS = 3;
    public static boolean XOR_DEBUG = false;
}
