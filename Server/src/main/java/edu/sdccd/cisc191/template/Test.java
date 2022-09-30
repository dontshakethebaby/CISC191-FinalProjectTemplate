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
    @org.junit.jupiter.api.Test
    void max() {
        Integer[] numbers = new Integer[]{88,17,37};
        MainWords mains = new MainWords(3);
        int max = mains.max(numbers);
        assertEquals(88,max);
    }

    // ComputerWords Tests
    @org.junit.jupiter.api.Test
    void retrieveList() throws IOException {
        MainWords mains = new MainWords(1);
        ComputerWords comps = new ComputerWords(mains, 7);
        String[] commonIce = {"water", "frost", "glacier", "glass", "freeze", "icicle", "cool"};
        assertArrayEquals(commonIce, comps.retrieveList("ICE",7));
    }

    @org.junit.jupiter.api.Test
    void clean() {
        // clean out specified strings
        String src = "{\"word\":\"join\",\"score\":23.46255493699426,\"from\":\"cn5,ol,wn,wn,wn,wn\"}," +
                "{\"word\":\"connection\",\"score\":10.275796838109397,\"from\":\"cn5,ol,w2v,wn\"}," +
                "{\"word\":\"connect\",\"score\":9.99871342981061,\"from\":\"cn5,ol,w2v,wn\"}";
        String[] s = {"score","wiki","swiki","w2v","\"wn\"","wn,",",wn","\"ol\"",",ol","ol,","cn5","from"};
        src = ComputerWords.clean(src, s, s.length - 1);

        // clean out specified characters
        char[] c = {':','\"','{','}',',','.','0','1','2','3','4','5','6','7','8','9'};
        src = ComputerWords.clean(src, c, c.length - 1);

        assertEquals("  word   join                                " +
                "word   connection                                  " +
                "word   connect                             ",src);
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