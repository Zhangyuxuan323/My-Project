package ngordnet.main;

import ngordnet.browser.NgordnetQuery;
import ngordnet.browser.NgordnetQueryHandler;
import ngordnet.ngrams.NGramMap;
import ngordnet.ngrams.TimeSeries;
import ngordnet.plotting.Plotter;
import org.knowm.xchart.XYChart;

import java.util.ArrayList;

public class HistoryHandler extends NgordnetQueryHandler {

    NGramMap map;

    public HistoryHandler(NGramMap map){
        this.map = map;
    }
    @Override
    public String handle(NgordnetQuery q) {
        System.out.println("Got query that looks like:");
        System.out.println("Words: " + q.words());
        System.out.println("Start Year: " + q.startYear());
        System.out.println("End Year: " + q.endYear());
        String wordFile = "./data/ngrams/top_14377_words.csv";
        String countFile = "./data/ngrams/total_counts.csv";

        NGramMap ngm = new NGramMap(wordFile, countFile);
        ArrayList<String> words = new ArrayList<>();
        words.add("cat");
        words.add("dog");

        ArrayList<TimeSeries> lts = new ArrayList<>();
        for (String word : words) {
            lts.add(ngm.weightHistory(word, 2000, 2020));
        }

        XYChart chart = Plotter.generateTimeSeriesChart(words, lts);
        String s = Plotter.encodeChartAsString(chart);
        return s;

    }
}
