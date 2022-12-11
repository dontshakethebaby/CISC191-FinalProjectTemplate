package edu.sdccd.cisc191.template;

import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

/** ConnectionToClient contains all of the threading so that the game can be two-player.
 * It is heavily based on the text examples of games:
 * https://math.hws.edu/javanotes/c12/s5.html
 * https://math.hws.edu/javanotes/source/chapter12/netgame/common/Hub.java
 * Module 13: Concurrency/Threads */
public class ConnectionToClient {
    /* Volatile things can be accessed by many threads without causing errors */
    volatile boolean closed = false;
    private PrintWriter out;
    private BufferedReader in;
    int id;
    Socket clientSocket;
    LinkedBlockingQueue<Object> objectsToSend;
    LinkedBlockingQueue<Object> objectsReceived;
    private Thread sendThread;
    private Thread receiveThread;

    /* LinkedBlockingQueue is a special type of queue that will halt the execution
    of the program until something from it can be removed, here it is used to wait
    for something to be sent/received before moving on. */
    ConnectionToClient(Socket clientSocket, int id) {
        this.clientSocket = clientSocket;
        this.id = id;
        objectsToSend = new LinkedBlockingQueue<Object>();
        objectsReceived = new LinkedBlockingQueue<Object>();
        sendThread = new SendThread();
        sendThread.start();
        receiveThread = new ReceiveThread();
        receiveThread.start();
    }

    /** Puts the object to send into the outgoing queue */
    void send(Object Obj) throws Exception {
        objectsToSend.put(Obj);
    }

    /** Waits for a received object and then returns it */
    Object receive() throws InterruptedException {
        return objectsReceived.take();
    }

    /** close closes send/receive threads and socket */
    void close() throws IOException {
        closed = true;
        sendThread.interrupt();
        receiveThread.interrupt();
        clientSocket.close();
    }

    boolean isClosed(){
        return closed;
    }

    /** As long as the connection is open, continually wait for something to be sent */
    private class SendThread extends Thread {
        public void run() {
            while (!closed) {
                try {
                    out = new PrintWriter(clientSocket.getOutputStream(), true);
                    Object message = objectsToSend.take();
                    String json = JsonWriter.objectToJson(message);
                    out.println(json);
                } catch (Exception e) {
                }
            }
        }
    }

    /** As long as the connection is open, continually wait until something can be received */
    private class ReceiveThread extends Thread {
        public void run() {
            while (!closed) {
                try {
                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    Object message = JsonReader.jsonToJava(in.readLine()); // blocks until something can be received
                    objectsReceived.put(message);
                    }
                catch(Exception e){
                    System.out.println("Connection to " + id + " closed");
                    closed = true;
                }
            }
        }
    }
}
