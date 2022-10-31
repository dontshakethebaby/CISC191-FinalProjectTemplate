package edu.sdccd.cisc191.template;

import java.util.Arrays;
import java.util.Scanner;

/** UserWords deals with user inputted words.
 * Module 5: Inheritance */
public class UserWords extends WordList {
    static final String[] cardinals = {"one", "two", "three", "four", "five"};
    final String[] ordinals = {"first", "second", "third", "fourth", "fifth"};

    /** Collects user guesses by prompting and validating each guess.
     * @param mainWord The relevant main word
     * @param nguesses The number of guesses
     * @param round Round index of current round
     * @return User guesses for the round */
    public String[] collectGuesses(String mainWord, int nguesses, int round) {

        String[] roundGuesses = new String[nguesses];
        Scanner stdin = new Scanner(System.in);

        System.out.println("ROUND " + cardinals[round - 1].toUpperCase());
        System.out.println("The main word for round " + cardinals[round - 1] + " is " + mainWord + ".");

        for (int guess = 0; guess < nguesses; guess++) {
            // Initial prompt for mth guess
            System.out.println("Give a " + ordinals[guess] + " word associated with " + mainWord + ":");
            String userWord = stdin.nextLine().toLowerCase();

            /* Validity checks for user input, re-prompt and replace userWord if it's > 2 words
            long or is a (weak) substring of the main word, or contains full main word */
            while ((occurs(mainWord.toLowerCase(),userWord))|(userWord.split(" ").length > 2)
                    |(occurs(userWord, mainWord.toLowerCase()))) {
                if (userWord.split(" ").length > 2) {
                    System.out.println("Too many words entered, please provide a guess that is two words or less: ");
                    userWord = stdin.nextLine();
                }
                else if (occurs(userWord, mainWord.toLowerCase())) {
                    System.out.println("Please give a word that does not contain the main word: ");
                    userWord = stdin.nextLine();
                }
                else {
                    System.out.println("Please give a word that is NOT a substring of the main word: ");
                    userWord = stdin.nextLine();
                }
            }
            roundGuesses[guess] = userWord;
        }
        return roundGuesses;
    }

    /** Reveals round information to the user including computer words (optional), how many
     * words they matched, and points. Points are calculated elsewhere.
     * @param main The main word for current round
     * @param comps The computer words for that main word
     * @param guesses User's guessed words
     * @roundPoints Calculated points for each user guess */
    public void roundReveal(String main, String[] comps, String[] guesses, int[] roundPoints) {
        Scanner stdin = new Scanner(System.in);
        String pointPlural = "";

        // Asks and validates user response to whether they want to see common words list and executes
        System.out.println("Would you like to see the common words list for this round? (Y/N): ");
        String seeList = stdin.nextLine().toLowerCase();
        while (!(occurs(seeList, "y")|(occurs(seeList, "n")))) {
            System.out.println("Please enter valid input (Y/N): ");
            seeList = stdin.nextLine().toLowerCase();
        }
        if (occurs(seeList,"y")) {
            System.out.println("The common word list for " + main + " is: ");
            for (int i = 0; i < comps.length; i++) {
                System.out.println(comps[i]);
            }
        }
        else {
        }

        // The guess by guess summary of points
        int sum = Arrays.stream(roundPoints).sum();
        for (int guess = 0; guess < guesses.length; guess++) {
            if (roundPoints[guess] == 1) {
                pointPlural = "point";
            }
            else {
                pointPlural = "points";
            }
            System.out.println("You received " + roundPoints[guess] + " " + pointPlural + " for "
                    + guesses[guess] + ",");
        }

        // Summary of round points
        if (sum == 1) {
            pointPlural = "point";
        }
        else {
            pointPlural = "points";
        }
        System.out.println("For a total of " + Arrays.stream(roundPoints).sum() + " " + pointPlural
                + " for the round.");
    }

    /** Recursive method to create a string of repeating characters
     * Module 10: Recursion
     * @param str String you'd like to repeat
     * @param times Number of times to repeat
     * @return The original string repeated the most recent number of times */
    public static String repeat(String str, int times) {
        if (times > 1) {
            return repeat(str, times - 1) + str;
        }
        else {
            return str;
        }
    }
}
