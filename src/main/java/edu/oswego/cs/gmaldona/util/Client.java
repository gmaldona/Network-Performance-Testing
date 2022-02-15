package edu.oswego.cs.gmaldona.util;

import java.io.IOException;

abstract public class Client {
    public abstract void sendMessage(String payload) throws IOException;
    public abstract boolean sendMessageForThroughput(String payload) throws IOException;
    public abstract boolean sendMessageForLatency(String payload) throws IOException;
    public abstract void close() throws IOException;
}
