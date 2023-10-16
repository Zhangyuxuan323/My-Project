package ngordnet.ngrams;

import edu.princeton.cs.algs4.In;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 *
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author Josh Hug
 */
public class NGramMap {

    private static final int MIN_YEAR = 1400;
    private static final int MAX_YEAR = 2100;
    private Map<String, TimeSeries> wordTimeSeries;
    private TimeSeries totalCountPerYear;

    /**
     * Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME.
     */
    public NGramMap(String wordsFilename, String countsFilename) {
        wordTimeSeries = new HashMap<>();
        totalCountPerYear = new TimeSeries();

        // Parse counts file
        In countsFile = new In(countsFilename);
        while (countsFile.hasNextLine()) {
            String line = countsFile.readLine();
            String[] tokens = line.split(",");
            int year = Integer.parseInt(tokens[0]);
            double count = Double.parseDouble(tokens[1]);
            totalCountPerYear.put(year, count);
        }

        // Parse words file
        In wordsFile = new In(wordsFilename);
        while (wordsFile.hasNextLine()) {
            String line = wordsFile.readLine();
            String[] tokens = line.split("\t");
            String word = tokens[0];
            int year = Integer.parseInt(tokens[1]);
            double count = Double.parseDouble(tokens[2]);

            wordTimeSeries
                    .computeIfAbsent(word, k -> new TimeSeries())
                    .put(year, count);
        }
    }

    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other
     * words, changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy".
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        TimeSeries wordHistory = wordTimeSeries.get(word); // get the history for the word
        if (wordHistory == null) {
            return new TimeSeries(); // return an empty TimeSeries
        }
        return new TimeSeries(wordHistory, startYear, endYear); // return the sub-range using the constructor
    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy,
     * not a link to this NGramMap's TimeSeries. In other words, changes made
     * to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy".
     */
    public TimeSeries countHistory(String word) {
        TimeSeries wordHistory = wordTimeSeries.get(word); // get the history for the word
        if (wordHistory == null) {
            return new TimeSeries(); // return an empty TimeSeries
        }
        return new TimeSeries(wordHistory); // return a defensive copy using the constructor
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {
        return new TimeSeries(totalCountPerYear);
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends.
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        TimeSeries wordCounts = countHistory(word);
        TimeSeries totalCounts = totalCountHistory();

        TimeSeries result = new TimeSeries();

        for (int year = startYear; year <= endYear; year++) {
            if (wordCounts.containsKey(year) && totalCounts.containsKey(year)) {
                double wordCountForYear = wordCounts.get(year);
                double totalCountForYear = totalCounts.get(year);

                result.put(year, wordCountForYear / totalCountForYear);
            }
        }
        return result;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to
     * all words recorded in that year. If the word is not in the data files, return an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word) {
        TimeSeries wordCounts = countHistory(word);
        TimeSeries totalCounts = totalCountHistory();

        return wordCounts.dividedBy(totalCounts);
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS
     * between STARTYEAR and ENDYEAR, inclusive of both ends. If a word does not exist in
     * this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words,
                                          int startYear, int endYear) {
        TimeSeries summedWeights = new TimeSeries();

        for (String word : words) {
            TimeSeries wordWeight = weightHistory(word, startYear, endYear);
            summedWeights = summedWeights.plus(wordWeight);
        }

        return summedWeights;
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS.
     */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        TimeSeries summedWeights = new TimeSeries();

        for (String word : words) {
            TimeSeries wordWeight = weightHistory(word);
            summedWeights = summedWeights.plus(wordWeight);
        }

        return summedWeights;
    }


}
