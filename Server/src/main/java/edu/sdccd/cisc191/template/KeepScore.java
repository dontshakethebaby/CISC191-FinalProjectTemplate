package edu.sdccd.cisc191.template;

import org.apache.commons.lang.ArrayUtils;

public class KeepScore {

    /** Compares computer's words to user's guesses and returns an array of 1/0
     * integers representing a match/no match
     * @param comps The computer's related words list
     * @param guesses User inputted guesses of related words
     * @return The user's points for the current round */
    public static int[] calculatePoints(String[] comps, String[] guesses) {
        int[] points = new int[guesses.length];
        for (int guess = 0; guess < guesses.length; guess++) {
            if (ArrayUtils.contains(comps, guesses[guess])) {
                points[guess] = 1;
            }
            else {}
        }
        return points;
    }

    /*public static int[] lambdaPoints(String[] comps, String[] guesses) {
        numbers.forEach( (n) -> { System.out.println(n); } );
    }*/

}