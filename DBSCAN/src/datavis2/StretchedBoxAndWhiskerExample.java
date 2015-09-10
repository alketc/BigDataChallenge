package datavis2;

import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;


public class StretchedBoxAndWhiskerExample{

DefaultBoxAndWhiskerCategoryDataset dataset;
JFreeChart chart;
ChartPanel chartPanel;
JFrame frame;
JScrollPane scrollPane;

public StretchedBoxAndWhiskerExample() {
    createCategoryBoxplot();

    frame = new JFrame();
    scrollPane = new JScrollPane(chartPanel);
    scrollPane.setPreferredSize(new Dimension(800,700));
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    frame.add(scrollPane);
    frame.pack();
    frame.setVisible(true);
}

private void createCategoryBoxplot(){   
    dataset = createCategoryDataset();
    CategoryAxis xAxis = new CategoryAxis("");
    NumberAxis yAxis = new NumberAxis("Score");

    BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
    CategoryPlot plot = new CategoryPlot(dataset, xAxis, yAxis, renderer);
    createJFreeChart(plot,"Test");

    // Design
    renderer.setFillBox(false);
    renderer.setMeanVisible(false);

    chart.setBackgroundPaint(Color.white); 
    plot.setBackgroundPaint(Color.lightGray); 
    plot.setDomainGridlinePaint(Color.white); 
    plot.setDomainGridlinesVisible(true); 
    plot.setRangeGridlinePaint(Color.white);
    plot.getRangeAxis().setRange(-10.5, 10.5);


    chartPanel = new ChartPanel(chart);
    chartPanel.setPreferredSize(new Dimension(3250,600));
}

private DefaultBoxAndWhiskerCategoryDataset createCategoryDataset() {

    dataset = new DefaultBoxAndWhiskerCategoryDataset();
    ArrayList<Double> values = createSampleData();
    ArrayList<String> categories = createSampleCategories();
    for (int i=0;i<=3;i++){
        for (String category : categories){
            dataset.add(values,i,category);
        }
    }
    return dataset;
}


private ArrayList<String> createSampleCategories() {
    ArrayList<String> tmp = new ArrayList<String>();
    for (int i=0;i<=20;i++){
        tmp.add("Category"+i);
    }
    return tmp;
}

private ArrayList<Double> createSampleData() {
    ArrayList<Double> tmp = new ArrayList<Double>();
    for (int i=0;i<10;i++){
        tmp.add(5.0);
        tmp.add(7.0);
        tmp.add(2.0);
        tmp.add(4.0);
    }
    return tmp;
}

private void createJFreeChart(CategoryPlot plot, String title){
    chart = new JFreeChart(title, plot);
}

public static void main(String[] args) throws IOException { 
    StretchedBoxAndWhiskerExample demo = new StretchedBoxAndWhiskerExample();

}
}
