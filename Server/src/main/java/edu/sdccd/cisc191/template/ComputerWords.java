package edu.sdccd.cisc191.template;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

/** ComputerWords contains methods dealing with the computer generated list of related words.
 * Lists are extracted from https://relatedwords.org/
 * Module 5: Inheritance */
public class ComputerWords extends WordList {
    private String[][] compsArray;

    /** This constructor creates the computer words array from main words by calling retrieveList method.
     * @param mains contains list of main words
     * @param level the length of the computer list to generate; represents the difficulty
     * level of the game. Longer list, easier to match words. */
    public ComputerWords(MainWords mains, int level) {
        int rounds = mains.getList().length;
        compsArray = new String[rounds][level];

        try {
            for (int round = 0; round < rounds; round++) {
                compsArray[round] = this.retrieveList(mains.getList()[round], level);
            }
        } catch (IOException e) {
            System.out.println("Cannot access related words website.");
        }
    }

    public ComputerWords() {
    }

    /** retrieveList method extracts the raw data from the website, cleans the data, and removes words
     * that are invalid for reasons of game balance.
     * Module 7: Input stream, reading from URL
     * @param mainWord A single main word
     * @return The cleaned list of computer's related words */
    public String[] retrieveList (String mainWord, int level) throws IOException {
        System.setProperty("http.agent","Chrome"); // this line from stackoverflow
        mainWord = mainWord.toLowerCase();
        String src = "";

        URL url = new URL("https://relatedwords.org/relatedto/" + mainWord);
        url.openStream();
        Scanner input = new Scanner(url.openStream());

        // places all the raw data in a string
            while (input.hasNext() == true) {
                src += input.next() + " ";
            }
        input.close();

        String[] array = src.split("preloadedDataEl",2);
        src = array[1];
        array = src.split("score\":0.0",2);
        src = array[0];
        System.out.println(src);
        String[] s = {"score","wiki","swiki","w2v","\"wn\"","wn,",",wn","\"ol\"",",ol","ol,","cn5","from"};
        src = clean(src, s, s.length - 1);
        char[] c = {':','\"','{','}',',','.','0','1','2','3','4','5','6','7','8','9'};
        src = clean(src, c, c.length - 1);
        array = src.split("word");

        /* This next section removes words that are invalid from the game's perspective such as
        more than 2 words ("Antarctic Treaty System"), or containing the main word ("strawberry"
        for "BERRY"). It also makes the array a standard size so each common words list is the same
        size for scoring fairness.
         */
        String prefix = mainWord;
        if (mainWord.length() > 6) {
            prefix = mainWord.substring(0, 5);     // First 5 letters
        }
        else { }

        String[] newArray = new String[level];
        int newIndex = 0;

        /* This loop removes whitespace around each word and conducts the validity checks.
        If the word passes these checks, it is placed in newArray, this process stops once
        the desired length of the array is reached. */
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i].trim();
            /* str.split(findStr).length from
            https://stackoverflow.com/questions/767759/occurrences-of-substring-in-a-string */
            if ((array[i].split(" ").length > 2)|(occurs(array[i],prefix))
                    |(occurs(array[i],mainWord))) {
            }
            else {
                if (newIndex < level) {
                    newArray[newIndex] = array[i];
                    newIndex ++;
                }
                else break;
            }
        }
        return newArray;
    }

    /** Overloaded method clean recursively removes characters (or strings) from raw data
     * Module 5: Polymorphism (overloading)
     * Module 10: Recursion
     * @param src The raw data we wish to clean
     * @param c An array where each element is a character (or string) we want to remove
     * @param i An index for that array
     * @return The raw data with the most recent unwanted character removed */
    public static String clean(String src, char[] c, int i) {
        src = src.replace(c[i],' ');
        if (i > 0) {
            return clean(src, c, i - 1);
        }
        else {
            return src;
        }
    }
    public static String clean(String src, String[] s, int i) {
        src = src.replaceAll(s[i],"");
        if (i > 0) {
            return clean(src, s, i - 1);
        }
        else {
            return src;
        }
    }

    public String[][] getCompsArray() {
        return compsArray;
    }
}
