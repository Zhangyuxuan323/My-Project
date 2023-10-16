package ngordnet.ngrams;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * An object for mapping a year number (e.g. 1996) to numerical data. Provides
 * utility methods useful for data analysis.
 *
 * @author Josh Hug
 */
public class TimeSeries extends TreeMap<Integer, Double> {

    private static final int MIN_YEAR = 1400;
    private static final int MAX_YEAR = 2100;

    /**
     * Constructs a new empty TimeSeries.
     */
    public TimeSeries() {
        super();
    }

    /**
     * Constructs a TimeSeries that is a defensive copy of the given TimeSeries.
     * @param ts The TimeSeries to copy.
     */
    public TimeSeries(TimeSeries ts) {
        super(ts);
    }

    /**
     * Creates a copy of TS, but only between STARTYEAR and ENDYEAR,
     * inclusive of both end points.
     */
    public TimeSeries(TimeSeries ts, int startYear, int endYear) {
        super();
        if (startYear > endYear){
            throw new IllegalArgumentException("Start year cannot be greater than end year.");
        }
        for (int year = startYear; year <= endYear; year++){
            if (ts.containsKey(year)){
                this.put(year,ts.get(year));
            }
        }
    }

    /**
     * Returns all years for this TimeSeries (in any order).
     */
    public List<Integer> years() {
        return new ArrayList<>(this.keySet());
    }

    /**
     * Returns all data for this TimeSeries (in any order).
     * Must be in the same order as years().
     */
    public List<Double> data() {
        return new ArrayList<>(this.values());
    }

    /**
     * Returns the year-wise sum of this TimeSeries with the given TS. In other words, for
     * each year, sum the data from this TimeSeries with the data from TS. Should return a
     * new TimeSeries (does not modify this TimeSeries).
     *
     * If both TimeSeries don't contain any years, return an empty TimeSeries.
     * If one TimeSeries contains a year that the other one doesn't, the returned TimeSeries
     * should store the value from the TimeSeries that contains that year.
     */
    public TimeSeries plus(TimeSeries ts) {
        TimeSeries result = new TimeSeries();

        // Adding values of this TimeSeries.
        for (Integer year : this.keySet()) {
            double value = this.get(year);
            if (ts.containsKey(year)) {
                value += ts.get(year);
            }
            result.put(year, value);
        }

        // Adding values of ts that are not in this TimeSeries.
        for (Integer year : ts.keySet()) {
            if (!result.containsKey(year)) {
                result.put(year, ts.get(year));
            }
        }

        return result;
    }

    /**
     * Returns the quotient of the value for each year this TimeSeries divided by the
     * value for the same year in TS. Should return a new TimeSeries (does not modify this
     * TimeSeries).
     *
     * If TS is missing a year that exists in this TimeSeries, throw an
     * IllegalArgumentException.
     * If TS has a year that is not in this TimeSeries, ignore it.
     */
    public TimeSeries dividedBy(TimeSeries ts) {
        TimeSeries result = new TimeSeries();

        for (Integer year : this.keySet()) {
            if (!ts.containsKey(year)) {
                throw new IllegalArgumentException("TS is missing a year that exists in this TimeSeries.");
            }
            double quotient = this.get(year) / ts.get(year);
            result.put(year, quotient);
        }

        return result;
    }

}
