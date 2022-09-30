package edu.sdccd.cisc191.template;

import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/** Object-general methods to send and receive through network.
 * Module 5: Inheritance, polymorphism */
public class Communication {
    private PrintWriter out;
    private BufferedReader in;

    /** Sends an object through a socket.
     * Module 7: PrintWriter output stream
     * Module 8: Networking, serialization
     * @param obj Object to send
     * @param socket Where to send */
    public void send(Object obj, Socket socket) throws IOException {
        out = new PrintWriter(socket.getOutputStream(), true);
        String json = JsonWriter.objectToJson(obj);
        out.println(json);
    }

    /** Receives an object through a socket.
     * Module 7: BufferedReader input stream
     * Module 8: Networking, serialization
     * @param socket Where to receive from
     * @return The object that was received */
    public Object receive(Socket socket) throws IOException {
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        Object obj = JsonReader.jsonToJava(in.readLine());
        return obj;
    }
}
