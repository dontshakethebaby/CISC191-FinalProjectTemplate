package edu.sdccd.cisc191.template;

import org.apache.commons.lang.ArrayUtils;

import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class WordManager {

    /* Given a word list, pick returns n words from that list, randomly selected
    but without repeats */
    public static String[] pick(String[] words, int rounds) {
        String[] mainWords = new String[rounds];
        int rand;

        for (int i = 0; i < rounds; i++) {
            rand = ThreadLocalRandom.current().nextInt(0, words.length);
            while (ArrayUtils.contains(mainWords,words[rand])) {                    // while a duplicate, re-roll
                rand = ThreadLocalRandom.current().nextInt(0, words.length);
            }
            mainWords[i] = words[rand];
        }
        return mainWords;
    }

    /* Returns true if a substring occurs within a string, because I'm counting splits,
    the original string needs to be padded. */
    public static boolean occurs(String str, String substr) {
        boolean doesContain = false;
        str = "0" + str + "0";
        String[] array = str.split(substr);
        if (array.length > 1) {
            doesContain = true;
        }
        return doesContain;
    }

    /* Takes in array of common words and array of user guesses and returns an array of
    0/1 ints based on whether each guess was wrong/right. */
    public static int[] calculatePoints(String[] commonWords, String[] guesses) {
        int[] points = new int[guesses.length];
        for (int guess = 0; guess < guesses.length; guess++) {
            if (ArrayUtils.contains(commonWords, guesses[guess])) {
                points[guess] = 1;
            }
            else {}
        }
        return points;
    }

}