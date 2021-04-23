package s285600.computationalmath.nonlinear_equations.utils;

import javax.swing.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

/**
 * @author Kir
 * Created on 13.03.2021
 */

public class PlotUtils {
    public static void drawEq(DoubleUnaryOperator f, String function, double left, double right, double rootXBisec, double rootXChords) {
        final XYSeries series = new XYSeries(function);
        for (double i = left - 50; i < right + 50; i += 0.1) {
            series.add(i, f.applyAsDouble(i));
        }

        XYSeriesCollection dataset = new XYSeriesCollection();

        dataset.addSeries(series);

        if (Double.isFinite(rootXBisec)) {
            final XYSeries bis_series = new XYSeries("Bisection");
            bis_series.add(rootXBisec, 0);
            dataset.addSeries(bis_series);
        }

        if (Double.isFinite(rootXChords)) {
            final XYSeries chord_series = new XYSeries("Chords");
            chord_series.add(rootXChords, 0);
            dataset.addSeries(chord_series);
        }

        final JFreeChart chart = ChartFactory.createXYLineChart(
                "Equation Graph",
                "X",
                "Y",
                null,
                PlotOrientation.VERTICAL,
                true,
                false,
                false
        );

        chart.setBackgroundPaint(Color.WHITE);

        final XYPlot plot = chart.getXYPlot();

        plot.setBackgroundPaint(Color.LIGHT_GRAY);

        plot.getDomainAxis().setRange(left - 5, right + 5);
        plot.getDomainAxis().setAutoRange(false);
        plot.setDomainZeroBaselineVisible(true);
        plot.setRangeZeroBaselineVisible(true);

        XYSplineRenderer r0 = new XYSplineRenderer();

        r0.setSeriesShapesVisible(0, false);
        r0.setSeriesLinesVisible(1, false);
        r0.setSeriesPaint(1, Color.MAGENTA);
        r0.setSeriesLinesVisible(2, false);
        r0.setSeriesPaint(2, Color.YELLOW);

        plot.setDataset(0, dataset);

        plot.setRenderer(r0);

        JFrame jf = new JFrame();
        jf.setContentPane(new ChartPanel(chart));
        jf.pack();
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.setVisible(true);
    }

    public static void drawSystem(DoubleUnaryOperator[] func, String[] str, double rootX, double rootY) {
        List<XYSeries> series = new ArrayList<>(4);
        for (int i = 0; i < str.length; i++) {
            series.add(new XYSeries(str[i]));
            series.add(new XYSeries(str[i] + "comp"));
        }

        for (double j = -100; j < 100; j += 0.1) {
            for (int i = 0; i < func.length; i++) {
                DoubleUnaryOperator f = func[i];
                if (f != null) {
                    series.get(i).add(j, f.applyAsDouble(j));
                }
            }
        }

        XYSeriesCollection dataset = new XYSeriesCollection();

        for (XYSeries s: series) {
            dataset.addSeries(s);
        }

        final XYSeries root = new XYSeries("Root");
        root.add(rootX, rootY);
        dataset.addSeries(root);


        final JFreeChart chart = ChartFactory.createXYLineChart(
                "System Graph",
                "X",
                "Y",
                null,
                PlotOrientation.VERTICAL,
                true,
                false,
                false
        );

        chart.setBackgroundPaint(Color.WHITE);

        final XYPlot plot = chart.getXYPlot();

        plot.setBackgroundPaint(Color.LIGHT_GRAY);
        plot.getDomainAxis().setRange(rootX - 10, rootX + 10);
        plot.getRangeAxis().setRange(rootY - 10, rootY + 10);
        plot.setDomainZeroBaselineVisible(true);
        plot.setRangeZeroBaselineVisible(true);

        XYSplineRenderer r = new XYSplineRenderer();
        r.setSeriesShapesVisible(0, false);
        r.setSeriesShapesVisible(1, false);
        r.setSeriesShapesVisible(2, false);
        r.setSeriesShapesVisible(3, false);

        r.setSeriesLinesVisible(0, true);
        r.setSeriesLinesVisible(1, true);
        r.setSeriesLinesVisible(2, true);
        r.setSeriesLinesVisible(3, true);

        r.setSeriesVisibleInLegend(1, false);
        r.setSeriesVisibleInLegend(3, false);
        r.setSeriesVisibleInLegend(4, false);

        r.setSeriesPaint(0, Color.RED);
        r.setSeriesPaint(1, Color.BLUE);

        r.setSeriesPaint(2, Color.BLUE);
        r.setSeriesPaint(3, Color.BLACK);

        r.setSeriesPaint(4, Color.YELLOW);


        plot.setDataset(0, dataset);
        plot.setRenderer(r);

        JFrame jf = new JFrame();
        jf.setContentPane(new ChartPanel(chart));
        jf.pack();
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.setVisible(true);
    }
}
