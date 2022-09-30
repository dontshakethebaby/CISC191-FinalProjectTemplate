package edu.sdccd.cisc191.template;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class Test {

    @org.junit.jupiter.api.Test
    // picks random words without repeats placed in an array
    void pick() {
        // Because it randomizes I will just test that length of output array is appropriate
        String[] words = {"a","b","c","d"};
        int rounds = 3;
        assertEquals(3, WordManager.pick(words,rounds).length);
    }

    @org.junit.jupiter.api.Test
    void occurs() {
        String str = "peppermint";
        String substr = "mint";
        assertEquals(true, WordManager.occurs(str,substr));
    }

    @org.junit.jupiter.api.Test
    void occursNot() {
        String str = "peppermint";
        String substr = "berry";
        assertEquals(false, WordManager.occurs(str,substr));
    }

    @org.junit.jupiter.api.Test
    void getCommonWords() throws FileNotFoundException {
        String[] commonIce = {"water", "frost", "glacier", "glass", "freeze", "icicle", "cool"};
        assertArrayEquals(commonIce, DataManager.getCommonWords(172,7));
    }

    @org.junit.jupiter.api.Test
    void calculatePoints() {
        // for main word "PIE"
        String[] commonWords = {"pastry", "biscuit", "cake", "crust", "tart"};
        String[] guesses = {"apple","pumpkin","crust"};
        int[] shouldBe = {0,0,1};
        assertArrayEquals(shouldBe, WordManager.calculatePoints(commonWords,guesses));
    }

    @org.junit.jupiter.api.Test
    void pickMain() throws FileNotFoundException {
        // Another randomizer so just checking that output is formatted correctly
        String word = DataManager.pickMain();
        assertTrue(word.contains("_"));
    }


}