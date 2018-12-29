package pl.ark.chr.buginator.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arek on 2016-12-25.
 */
public class ChartData {

    private List<String> labels = new ArrayList<>();
    private List<String> series = new ArrayList<>();
    private List<List<Long>> data = new ArrayList<>();

    public ChartData(String... seriesNames) {
        for (String seriesName : seriesNames) {
            series.add(seriesName);
        }
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<String> getSeries() {
        return series;
    }

    public void setSeries(List<String> series) {
        this.series = series;
    }

    public List<List<Long>> getData() {
        return data;
    }

    public void setData(List<List<Long>> data) {
        this.data = data;
    }
}
