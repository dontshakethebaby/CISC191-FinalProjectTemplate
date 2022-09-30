package edu.sdccd.cisc191.template;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

// https://www.geeksforgeeks.org/different-ways-reading-text-file-java/
public class DataManager {

    public static String pickMain() throws FileNotFoundException {

        int index = ThreadLocalRandom.current().nextInt(0, 400);
        String[] mainList = new String[index + 1];

        File file = new File("C:\\Users\\disaf\\IdeaProjects\\CISC191-FinalProjectTemplate\\mainlist.txt");
        Scanner sc = new Scanner(file);
        sc.useDelimiter(",");

        // Reads all words up to and including index
        for (int i = 0; i < index + 1; i++) {
            mainList[i] = sc.next();
        }

        String mainWord = mainList[mainList.length - 1] + "_" + index;
        return mainWord;
    }

    // Gets the associated words list for the main word
    public static String[] getCommonWords(int mainIndex, int level) throws FileNotFoundException {
        String[] lineWords = new String[mainIndex + 1];

        File file = new File("C:\\Users\\disaf\\IdeaProjects\\CISC191-FinalProjectTemplate\\associatedlists.txt");
        Scanner sc = new Scanner(file);
        sc.useDelimiter("_");

        // Read and save every line up to and including the one we actually want (fix later to only save wanted)
        for (int i = 0; i < mainIndex + 1; i++) {
            lineWords[i] = sc.next();
        }
        // remove first char which is a leading comma
        lineWords[lineWords.length - 1] = lineWords[lineWords.length - 1].substring(1);

        // Split into array
        String[] commonList = lineWords[lineWords.length - 1].split(",");

        String[] array = new String[level];
        for (int i = 0; i < level; i++) {
            array[i] = commonList[i];
        }

        return array;
    }

    // Reads userguesses file line by line
    public static String getGuess(int guessIndex) throws IOException, InterruptedException {
        File file = new File(
                "C:\\Users\\disaf\\IdeaProjects\\CISC191-FinalProjectTemplate\\userguesses.txt");
        BufferedReader br
                = new BufferedReader(new java.io.FileReader(file));
        String[] guesses = new String[3];

        for (int guess = 0; guess < guesses.length; guess++) {
            guesses[guess] = br.readLine();
        }

        return guesses[guessIndex];
    }

}
