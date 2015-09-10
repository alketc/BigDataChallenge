package datavis2;

import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.BoxAndWhiskerToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.Log;
import org.jfree.util.LogContext;

import oopbigdatachallenge.Main2Vis;

public class BoxAndWhiskerDemo extends ApplicationFrame {

    /** Access to logging facilities. */
    private static final LogContext LOGGER = Log.createContext(BoxAndWhiskerDemo.class);
    static Map<String,Map<String, Map<String, Double>>>m = new HashMap<String,Map<String, Map<String, Double>>>();
    static String [] datafiles = {"H", "DBSCAN"};
    /**
     * Creates a new demo.
     *
     * @param title  the frame title.
     */
    public BoxAndWhiskerDemo(final String title) throws Exception{

        super(title);
        
     //  final BoxAndWhiskerCategoryDataset dataset = createSampleDataset();
        Main2Vis.getData(m, datafiles);
        final BoxAndWhiskerCategoryDataset dataset = createBoxPlotDataset (m );
        final CategoryAxis xAxis = new CategoryAxis("Type");
        final NumberAxis yAxis = new NumberAxis("Value");
        yAxis.setAutoRangeIncludesZero(true);
        final BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
        renderer.setFillBox(false);
       // renderer.setToolTipGenerator(new BoxAndWhiskerToolTipGenerator());
        final CategoryPlot plot = new CategoryPlot(dataset, xAxis, yAxis, renderer);

        final JFreeChart chart = new JFreeChart(
            "Box-and-Whisker Demo",
            new Font("SansSerif", Font.BOLD, 10),
            plot,
            true
        );
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(750, 470));
        setContentPane(chartPanel);

    }

    /**
     * Creates a sample dataset.
     * 
     * @return A sample dataset.
     */
    private BoxAndWhiskerCategoryDataset createSampleDataset() {
        
        final int seriesCount = 3;
        final int categoryCount = 4;
        final int entityCount = 22;
        
        final DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();
        for (int i = 0; i < seriesCount; i++) {
            for (int j = 0; j < categoryCount; j++) {
                final List list = new ArrayList();
                // add some values...
                for (int k = 0; k < entityCount; k++) {
                    final double value1 = 10.0 + Math.random() * 100;
                    list.add(new Double(value1));
                    final double value2 = 11.25 + Math.random()* 100; // concentrate values in the middle
                    list.add(new Double(value2));
                }
                LOGGER.debug("Adding series " + i);
                LOGGER.debug(list.toString());
                dataset.add(list, "Series " + i, " Type " + j);
            }
            
        }

        return dataset;
    }
 private BoxAndWhiskerCategoryDataset createBoxPlotDataset(Map<String,Map<String, Map<String, Double>>>m) {
        
        
        
       final DefaultBoxAndWhiskerCategoryDataset dataset 
            = new DefaultBoxAndWhiskerCategoryDataset();
        for (String i : m.keySet()) { // per ogni algoritmo 
           System.out.println("per ogni alg : "+i);
        	for (String si: m.get(i).keySet()) { // perogni città 
        		System.out.println("Perogni città "+si);
                final List list = new ArrayList();
                // add some values...
                for (String sij : m.get(i).get(si).keySet()) {// perogni intervallo di valori (entropie) di quella città
                    final double value1 = m.get(i).get(si).get(sij);
                    System.out.println("------------------------------------>>>>>>>>>>>>>>>>> "+value1);
                    list.add(new Double(value1));
//                   final double value2 = m.get(i).get(si).get(sij); // concentrate values in the middle
//                    list.add(new Double(value2));
                }
                LOGGER.debug("Adding series " + i);
                LOGGER.debug(list.toString());
                dataset.add(list, " " + i, " " + si);
            }
            
        }

       return dataset;
    }

    // ****************************************************************************
    // * JFREECHART DEVELOPER GUIDE                                               *
    // * The JFreeChart Developer Guide, written by David Gilbert, is available   *
    // * to purchase from Object Refinery Limited:                                *
    // *                                                                          *
    // * http://www.object-refinery.com/jfreechart/guide.html                     *
    // *                                                                          *
    // * Sales are used to provide funding for the JFreeChart project - please    * 
    // * support us so that we can continue developing free software.             *
    // ****************************************************************************
    
    /**
     * For testing from the command line.
     *
     * @param args  ignored.
     */
    public static void main(final String[] args) throws Exception {

        //Log.getInstance().addTarget(new PrintStreamLogTarget(System.out));
        final BoxAndWhiskerDemo demo = new BoxAndWhiskerDemo("Box-and-Whisker Chart Demo");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }

}
