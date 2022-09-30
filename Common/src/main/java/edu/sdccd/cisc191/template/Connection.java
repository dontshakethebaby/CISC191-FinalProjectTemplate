package edu.sdccd.cisc191.template;

import java.io.IOException;

/** Starts and stops connections for server and client.
 * Module 6: Interfaces */
public interface Connection {

    public void start(int port) throws Exception;
    public void stop() throws IOException;

}
