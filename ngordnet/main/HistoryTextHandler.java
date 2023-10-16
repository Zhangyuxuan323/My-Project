package ngordnet.main;

import ngordnet.browser.NgordnetQuery;
import ngordnet.browser.NgordnetQueryHandler;
import ngordnet.ngrams.NGramMap;
import ngordnet.ngrams.TimeSeries;

import java.util.List;

public class HistoryTextHandler extends NgordnetQueryHandler {
    private NGramMap map;

    public HistoryTextHandler(NGramMap map) {
        this.map = map;
    }
    @Override
    public String handle(NgordnetQuery query) {
        List<String> words = query.words();
        int startYear = query.startYear();
        int endYear = query.endYear();

        StringBuilder response = new StringBuilder();

        for (String word : words) {
            TimeSeries wordHistory = map.weightHistory(word, startYear, endYear);
            response.append(word).append(": ").append(wordHistory.toString()).append("\n");
        }

        return response.toString();
    }
}
