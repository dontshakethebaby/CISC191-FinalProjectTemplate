package edu.sdccd.cisc191.template;

/** Contains important methods for all types of WordLists: computer, main, and user.
 * Module 5: Abstract class */
abstract class WordList {
    private String[] wordList;

    public WordList() {
        wordList = new String[]{""};
    }

    /** Returns true if a substring occurs within a string, because I'm counting splits,
     * the original string needs to be padded. I had some original reason for not using contains
     * in certain contexts that I unfortunately don't remember.
     * @param str The bigger string we're looking at
     * @param substr The smaller string we are looking for within the str
     * @return */
    public static boolean occurs(String str, String substr) {
        boolean doesContain = false;
        str = "0" + str + "0";
        String[] array = str.split(substr);
        if (array.length > 1) {
            doesContain = true;
        }
        return doesContain;
    }

    public void setList(String[] words) {
        wordList = words;
    }
    public String[] getList() {
        return wordList;
    }

}
