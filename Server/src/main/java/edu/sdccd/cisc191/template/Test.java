package edu.sdccd.cisc191.template;

import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

class Test {

    @org.junit.jupiter.api.Test
    void occurs() {
        String str = "peppermint";
        String substr = "mint";
        assertEquals(true, WordList.occurs(str,substr));
    }

    @org.junit.jupiter.api.Test
    void occursNot() {
        String str = "peppermint";
        String substr = "berry";
        assertEquals(false, WordList.occurs(str,substr));
    }

    // MainWords tests
    @org.junit.jupiter.api.Test
    // picks random words without repeats placed in an array
    void pickIndices() {
        // Because it randomizes I will just test that length of output array is appropriate
        int rounds = 5;
        MainWords mains = new MainWords(rounds);
        assertEquals(5,mains.getIndices().length);
    }

    // KeepScore Test
    @org.junit.jupiter.api.Test
    void calculatePoints() {
        // for main word "PIE"
        String[] commonWords = {"pastry", "biscuit", "cake", "crust", "tart"};
        String[] guesses = {"apple","pumpkin","crust"};
        int[] shouldBe = {0,0,1};
        assertArrayEquals(shouldBe, KeepScore.calculatePoints(commonWords,guesses));
    }



}