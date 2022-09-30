package edu.sdccd.cisc191.template;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/** Deals with all the Server-side actions (backend) like generating main words, computer words,
 * and calculating round points. Based on Prof's Server/Client template.
 * Module 5: Polymorphism, overriding
 * Module 6: Interfaces
 * Module 8: Networking */
public class Server extends Communication implements Connection {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    static int level = 20;
    static int nrounds = 3;
    static String[][] compsArray;

        @Override
        public void start(int port) throws Exception {
            serverSocket = new ServerSocket(port);
            clientSocket = serverSocket.accept();
        }

        @Override
        public void stop() throws IOException {
        clientSocket.close();
        serverSocket.close();

    }

    public static void main(String[] args) throws Exception {
        Server server = new Server();
        server.start(4444);

        // generates and sends the mainwords for the game
        MainWords mains = new MainWords(nrounds);
        server.send(mains, server.clientSocket);

        // gets and sends the computer words for the game
        ComputerWords comps = new ComputerWords(mains, level);
        String[][] compsArray = comps.getCompsArray();
        server.send(compsArray, server.clientSocket);

        /* Server side round loop: receives guesses, calculates points, and sends
        points each round. */
        for (int i = 0; i < nrounds; i++) {
            String[] guesses = (String[]) server.receive(server.clientSocket);

            int [] roundPts = KeepScore.calculatePoints(compsArray[i], guesses);
            server.send(roundPts, server.clientSocket);
        }
        server.stop();
    }
} //end class Server