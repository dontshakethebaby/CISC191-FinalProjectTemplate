package edu.sdccd.cisc191.template;

import org.apache.commons.lang.ArrayUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

/** MainWords deals with the randomly chosen words that will be presented to user to guess
 * related words.
 * Module 5: Inheritance */
public class MainWords extends WordList{
    // index and indices always refer to the numbering of the mainword within the datafile (0 to 396)

    private Integer[] mainIndices; // list of indices of words randomly chosen from mainwords

    /** MainWords will be randomly chosen and saved upon initialization */
    public MainWords(int nrounds) {
        this.pickIndices(nrounds);

        try {
            this.saveList(this.getIndices());
        } catch(FileNotFoundException e) {
            System.out.println("Cannot find mainWords.txt");
        }
    }

    // Dummy constructor required for JSON serialization to work properly
    public MainWords() {
    }

    /** pickIndices randomly generates as many integers as nrounds without replacement.
     * Module 9: Collections, needed a filled array of ints outside of 0 - 397 bounds for,
     * "while a duplicate, re-roll" to work properly.
     * @param nrounds The number of rounds for the game
     */
    public void pickIndices(int nrounds) {
        int rand;
        Integer[] indices = Collections.nCopies(nrounds, -1)
                .toArray(new Integer[nrounds]);

        for (int i = 0; i < nrounds; i++) {
            rand = ThreadLocalRandom.current().nextInt(0, 397);

            while (ArrayUtils.contains(indices,rand)) {                    // while a duplicate, re-roll
                rand = ThreadLocalRandom.current().nextInt(0, 397);
            }

            indices[i] = rand;
        }
        mainIndices = indices;
    }

    public Integer[] getIndices() {
        return mainIndices;
    }

    /** Find the max of an Integer array. Used for saveList to stop reading mainWords.txt asap.
     * @param indices The indices of the mainWords within the .txt source file
     * @return biggest of the provided Int array */
    public int max(Integer[] indices) {
        int max = indices[0];

        for (int i = 1; i < indices.length; i++) {
            if (indices[i] > max) {
                max = indices[i];
            }
        }
        return max;
    }

    /** It takes in indices, reads from file, and saves the main words list.
     * Module 7: Input streams, reads from a .txt file
     * @param indices The randomly generated without repeats indices */
    public void saveList(Integer[] indices) throws FileNotFoundException {
        String[] myList = new String[max(indices) + 1];
        // https://mkyong.com/java/how-to-get-the-filepath-of-a-file-in-java
        Path path = Paths.get("Server\\src\\main\\java\\edu\\sdccd\\cisc191\\template\\mainWords.txt");

        File file = new File(path.toAbsolutePath().toUri());
        Scanner reader = new Scanner(file);
        reader.useDelimiter(",");

        for (int i = 0; i < max(indices) + 1; i++) {
            myList[i] = reader.next();
        }
        reader.close();

        String[] array = new String[indices.length];
        for (int i = 0; i < indices.length; i++) {
            array[i] = myList[indices[i]];
            array[i] = array[i].trim();
        }

        this.setList(array);
    }

}
