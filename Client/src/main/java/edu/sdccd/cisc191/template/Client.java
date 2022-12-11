package edu.sdccd.cisc191.template;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.LinkedHashMap;

/** Deals with all the Client-side actions (frontend) such as explaining game, communicating
 * with the user through console, collecting user guesses, revealing round info to user. Based
 * on Prof's Server/Client template. */

public class Client extends Communication implements Connection {
    int identification;
    private Socket clientSocket;
    private String ip;
    static int nguesses = 3; // three guesses
    static int nrounds;
    static LinkedHashMap<String, String[]> guessMap;
    public Client(String IP) {
        ip = IP;
    }

    @Override
    public void start(int port) throws IOException {
        clientSocket = new Socket(ip, port);
    }
    @Override
    public void stop() throws IOException {
        clientSocket.close();
    }

    /** Client side main explains game and communicates with server side at appropriate times. */
    public static void main(String[] args) throws IOException {
        Client client = new Client("localhost");
        client.start(4444);

        // get game's main words from server
        String[] mains = (String[]) client.receive(client.clientSocket);
        System.out.println("HERE");
        nrounds = mains.length;

        // explains game
        System.out.println("This is a word game, for each round you will be given a main word, and then you will guess ");
        System.out.println(UserWords.cardinals[nguesses - 1] + " words that are associated with that word. ") ;
        System.out.println("Another player will do the same with the same main words. Your goal is to match as many words as ");
        System.out.println("possible from a list of words commonly associated with that main word.");
        System.out.println("Rules: ");
        System.out.println("1. Compound words are okay, ex: \"ice cream\", but must be no longer than two words.");
        System.out.println("2. Your guess cannot be a substring of the main word, ex: \"dino\" for \"DINOSAUR\".");
        System.out.println("3. Your guess cannot contain the entire main word, ex: \"strawberry\" is not ");
        System.out.println("allowed for \"BERRY\", but \"cherry\" is okay. \n" );
        System.out.println("GAME BEGINS");

        /* Client side round loop: collects user guesses, sends guesses,
        * receives round points, stores guesses and points in a LinkedHashMap,
        * reveals round's results to user, repeats. */
        UserWords users = new UserWords();
        guessMap = new LinkedHashMap<>(); // Linked preserves order
        int sumTotal = 0;
        for (int i = 0; i < nrounds; i++) {
            String[] userGuesses = users.collectGuesses(mains[i], nguesses, i + 1);
            client.send(userGuesses, client.clientSocket);

            System.out.println("Waiting for other player's response...");
            int[] roundPts = (int[]) client.receive(client.clientSocket);

            String[] array = new String[nguesses];
            for (int j = 0; j < nguesses; j++) {
                array[j] = userGuesses[j] + " (" + roundPts[j] + ") ";
            }
            sumTotal += Arrays.stream(roundPts).sum();
            guessMap.put(mains[i], array);

            users.roundReveal(mains[i], userGuesses, roundPts);
        }

        // Formats and displays the final table
        System.out.printf("%n %n %s %n", UserWords.repeat("-", 80));
        System.out.printf("%s SUMMARY TABLE %s", UserWords.repeat(" ",33), UserWords.repeat(" ", 33));
        System.out.printf("%n %s %n", UserWords.repeat("-", 80));
        for (String i : guessMap.keySet()) {
            System.out.printf(" %-15s", i);
            for (int j = 0; j < nguesses; j++) {
                System.out.printf("%-20s", guessMap.get(i)[j]);
            }
            System.out.printf("%n %s %n", UserWords.repeat("-", 80));
        }
        System.out.println("Total Score = " + sumTotal);
        System.out.println("(Out of a possible " + nrounds*nguesses + " points)");
        System.out.println("END GAME");

        client.stop();
    }
} //end class Client

