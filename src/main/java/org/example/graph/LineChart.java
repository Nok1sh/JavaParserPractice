package org.example.graph;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

public class LineChart extends JFrame {

    private Map<Integer, Integer> data;

    public LineChart(String title, Map<Integer, Integer> data) {
        super(title);
        this.data = data;

        XYSeries series = new XYSeries("количество голов");


        for (var item : data.entrySet()){
            series.add(item.getValue(), item.getKey());
        }


        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "График зависимости количества голов от стоимости трансфера",
                "Стоимость трансфера",
                "Количество голов",
                dataset
        );

        NumberAxis xAxis = (NumberAxis) chart.getXYPlot().getDomainAxis();

        NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
        numberFormat.setMaximumFractionDigits(0);
        numberFormat.setGroupingUsed(true);

        xAxis.setNumberFormatOverride(numberFormat);

        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
    }

}
