package coded.lens.nash;

import javaslang.collection.Stream;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import javax.swing.*;
import java.awt.geom.Rectangle2D;

public class ConverganceVisualisation extends ApplicationFrame {

    public static final int NUMBER_OF_STRATEGIES = 10;
    public static final int WINDOW_SIZE = NUMBER_OF_STRATEGIES * NUMBER_OF_STRATEGIES;
    public static final int NUM_SAMPLE_SETS = 4;

    public ConverganceVisualisation(String title, XYDataset dataset) {
        super(title);

        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setMouseWheelEnabled(true);

        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
    }

    private static JFreeChart createChart(XYDataset dataset) {
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Nash Games converging for N = " + NUMBER_OF_STRATEGIES + " strategies",
                "Button shuffle learning step",
                "Average difference in scores over window of size: " + WINDOW_SIZE,
                dataset,
                PlotOrientation.VERTICAL,
                true,                     // include legend
                true,                     // tooltips
                false                     // urls
        );

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setDomainZeroBaselineVisible(true);
        plot.setRangeZeroBaselineVisible(true);
        plot.getDomainAxis().setLowerMargin(0.0);
        plot.getDomainAxis().setUpperMargin(0.0);
        plot.setDomainPannable(true);
        plot.setRangePannable(true);

        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setLegendLine(new Rectangle2D.Double(-4.0, -3.0, 8.0, 6.0));
        return chart;
    }

    /*
     * OS X screen shot cmd-shift-3
     */
    public static void main(String[] args) {
        XYSeriesCollection dataset = new XYSeriesCollection();

        for (int i = 0; i < NUM_SAMPLE_SETS; i++) {
            XYSeries series = new XYSeries("Game " + i);

            Tournament tournament = new Tournament(NUMBER_OF_STRATEGIES);
            ScoreAnalysis scoreAnalysis = new ScoreAnalysis();

            scoreAnalysis
                    .avgMeanSqrsOverWindow(WINDOW_SIZE, tournament.scores())
                    .zip(Stream.from(WINDOW_SIZE))
                    .sliding(1, WINDOW_SIZE)
                    .take(WINDOW_SIZE * 10)
                    .map(ls -> ls.head())
                    .forEach(t -> series.add(t._2, t._1));

            dataset.addSeries(series);
        }


        ConverganceVisualisation demo = new ConverganceVisualisation("Convergance in a random Nash game", dataset);
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }

}