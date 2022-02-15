package edu.oswego.cs.gmaldona.util;

import java.util.HashMap;

public class Constants {
    public static final int PORT = 26923;
    public static final String HOST = NetworkingTools.SERVER_HOST;
    public static final int[] PAYLOAD_LENGTHS = new int[]{ 8, 64, 256, 1024 };
    public static final int TRIALS = 3;
}
