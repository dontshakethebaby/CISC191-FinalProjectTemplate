package edu.sdccd.cisc191.template;

import static org.junit.jupiter.api.Assertions.*;

class Test {

    @org.junit.jupiter.api.Test
    void repeat() {
        String str = UserWords.repeat("*",5);
        assertEquals("*****",str);
    }
}