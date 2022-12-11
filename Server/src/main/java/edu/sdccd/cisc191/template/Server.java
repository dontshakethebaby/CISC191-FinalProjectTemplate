package edu.sdccd.cisc191.template;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/** Deals with all the Server-side actions (backend) like generating main words
 * and calculating round points. Based on Prof's Server/Client template. */
public class Server extends Communication implements Connection {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    static int level = 20;
    static int nrounds = 3;

    static int numberOfPlayers = 2;

    ConnectionToClient[] clients = new ConnectionToClient[numberOfPlayers];

        @Override
        public void start(int port) throws Exception {
            int ctr = 0;
            serverSocket = new ServerSocket(port);
            while(ctr < numberOfPlayers) {
                clientSocket = serverSocket.accept();
                clients[ctr] = new ConnectionToClient(clientSocket,ctr);
                ctr++;
            }
        }

        @Override
        public void stop() throws IOException {
        for(int i = 0; i < numberOfPlayers; i++){
            clients[i].close();
        }
        serverSocket.close();
        }

    public static void main(String[] args) throws Exception {
        String [][] userGuesses = new String[2][3];
        Server server = new Server();
        server.start(4444);

        // generates and sends the mainwords for the game
        MainWords mains = new MainWords(nrounds);

        for(int i = 0; i < numberOfPlayers; i++) {
            server.clients[i].send(mains.getList());
        }

        /* Server side round loop: receives guesses, calculates points, and sends
        points each round. */
        for (int i = 0; i < nrounds; i++) {
            //String[] guesses = (String[]) server.receive(server.clientSocket);
            for(int j = 0; j < numberOfPlayers; j++) {
                userGuesses[j] = (String[]) server.clients[j].receive();
            }

            for(int j = 0; j < numberOfPlayers; j++) {
                int [] roundPts = KeepScore.calculatePoints(userGuesses[1-j],userGuesses[j]);
                server.clients[j].send(roundPts);
            }
        }

        while(true){
            if(server.clients[0].isClosed() && server.clients[1].isClosed()){
                server.stop();
                break;
            }
        }
    }
} //end class Server