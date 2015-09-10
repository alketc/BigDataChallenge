package datavis2;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.statistics.BoxAndWhiskerCalculator;
import org.jfree.data.statistics.BoxAndWhiskerXYDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerXYDataset;
import org.jfree.date.DateUtilities;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;


public class MyBoxPlot extends ApplicationFrame { // the MyBoxPlot library will extend the ApplicationFrame class form JFreeChart library

static String[] cities = {"Milano", "Torino", "Venezia", "Roma", "Bari", "Palermo", "Napoli"};	

public MyBoxPlot(String titel) {  // build the constructor of the class
super(titel);  // call the constructor of ApplicationFrame

final BoxAndWhiskerXYDataset dataset = createDataset();
final JFreeChart chart = createChart(dataset);

final ChartPanel chartPanel = new ChartPanel(chart);
chartPanel.setPreferredSize(new java.awt.Dimension(700, 400));
setContentPane(chartPanel);

}

private BoxAndWhiskerXYDataset createDataset() { // this method will generate the interval of random values to be represented as boxplots 
 final int ENTITY_COUNT = 14;

 DefaultBoxAndWhiskerXYDataset dataset = new 
		  DefaultBoxAndWhiskerXYDataset("My BoxPlot Graphics");

 for (int i = 0; i <  ENTITY_COUNT; i++) {
	  Date date = DateUtilities.createDate(2003, 7, i + 1, 12, 0);
	  List values = new ArrayList();
	  for (int j = 0; j < 10; j++) {
		  values.add(new Double(Math.random() ));
		  values.add(new Double( Math.random()));
	  }
	  dataset.add(date, 
			  BoxAndWhiskerCalculator.calculateBoxAndWhiskerStatistics(values));
 }
 return dataset;
}

private JFreeChart createChart(final BoxAndWhiskerXYDataset dataset) { // this method creates the chart itself   
 JFreeChart chart = ChartFactory.createBoxAndWhiskerChart(
		  "Box and Whisker Chart", "Time", "Value", dataset, true);
 chart.setBackgroundPaint(new Color(249, 231, 236));

 return chart;
}

public static void main(final String[] args) { // finaly we visualize it using the main method by

 final MyBoxPlot demo = new MyBoxPlot(""); // creating the MyBoxPlot object  called demo  
 demo.pack();      // call the method pack() in it
 RefineryUtilities.centerFrameOnScreen(demo);  // center it in the screen 
 demo.setVisible(true);   // and make it visible 
}
}
