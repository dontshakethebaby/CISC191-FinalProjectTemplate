package edu.sdccd.cisc191.template;

import org.apache.commons.lang.ArrayUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static edu.sdccd.cisc191.template.Server.nrounds;

/** MainWords deals with the randomly chosen words that will be presented to user to guess
 * related words.
 * Module 11: Sorting
 * Module 14: Lambdas
 * Module 15: Stream API */
public class MainWords extends WordList{
    private int[] mainIndices; // list of indices of words randomly chosen from mainwords

    /** MainWords will be randomly chosen, sorted, and saved upon initialization */
    public MainWords(int nrounds) {
        this.bubbleSort(pickIndices(nrounds));

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
     * Module 15: Stream API allows this to be a one-liner
     * @param nrounds The number of rounds for the game
     */
    public int[] pickIndices(int nrounds) {
        IntStream stream = new Random().ints(0, 397).distinct().limit(nrounds);

        int[] rands = stream.toArray();
        return rands;
    }

    /** bubbleSort is a simple bubbleSort for the picked mainWords indices, this allowed me
     * to eliminate my max() method, as the max is now always the final value.
     * Module 11: Sorting
     * @rands the unsorted randomly selected numbers*/
    public void bubbleSort(int[] rands) {
        int temp;

        // over multiple passes
        for (int j = 0; j < rands.length; j++) {
            // a single pass
            for (int i = 0; i < rands.length - j - 1; i++) {
                if (rands[i] > rands[i+1]) {
                    temp = rands[i];
                    rands[i] = rands[i+1];
                    rands[i+1] = temp;
                }
            }
        }

        mainIndices = rands;
    }

    public int[] getIndices() {
        return mainIndices;
    }

    /** It takes in indices, reads from file, and extracts and saves the main words list.
     * Module 14: Lambdas
     * Module 15: Stream API
     * @param indices The randomly generated without repeats indices */
    public void saveList(int[] indices) throws FileNotFoundException {
        // https://mkyong.com/java/how-to-get-the-filepath-of-a-file-in-java
        Path path = Paths.get("Server\\src\\main\\java\\edu\\sdccd\\cisc191\\template\\mainWords.txt");

        File file = new File(path.toAbsolutePath().toUri());
        Scanner reader = new Scanner(file);
        reader.useDelimiter(",");

        String[] wordBank = new String[indices[indices.length-1] + 1];
        for (int i = 0; i < indices[indices.length-1] + 1; i++) {
            wordBank[i] = reader.next();
        }
        reader.close();

        // Unfortunately necessary because int[] won't work in code below
        Integer[] indicesInt = new Integer[indices.length];
        for (int i = 0; i < indices.length; i++) {
            indicesInt[i] = indices[i];
        }

        // https://stackoverflow.com/questions/36294051/java-stream-filter-items-of-specific-index
        List<String> wordBankList = Arrays.asList(wordBank);
        List<Integer> filterIndices = Arrays.asList(indicesInt);
        List<String> filteredList =
                IntStream.range(0, wordBankList.size())
                        .filter(i -> filterIndices.contains(i))
                        .mapToObj(wordBankList::get)
                        .collect(Collectors.toList());

        // list.toArray() is a better way but in the spirit of lambdas...
        String[] array = new String[indices.length];
        filteredList.forEach( (i) -> { array[filteredList.indexOf(i)] = (i); } );
        this.setList(array);
    }

}
