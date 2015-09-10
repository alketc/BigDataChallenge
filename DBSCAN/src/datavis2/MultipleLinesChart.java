package datavis2;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class MultipleLinesChart extends JFrame { // the class extends the JFrame class
String path1 = "data4graphics/distance";
String path2 = "data4graphics/clusters";	
	public MultipleLinesChart() {   // the constructor will contain the panel of a certain size and the close operations 
		super("Entropy chart"); // calls the super class constructor
		try{
		JPanel chartPanel = createChartPanel();   
		add(chartPanel, BorderLayout.CENTER);
		}catch(Exception e){
			e.getMessage();
		}
		
		
		setSize(640, 480);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}
	
	private JPanel createChartPanel() throws Exception{ // this method will create the chart panel containin the graph 
		String chartTitle = "Entropy by varying distance";
		String xAxisLabel = "Distance";
		String yAxisLabel = "Entropy";
		Map<String, Map<Double, Double>> m = new HashMap<String, Map<Double,Double>>();
		m = getVal4Vis("data4graphics/test2distance");
		System.out.println("size m = "+m.size());
		XYDataset dataset = createCityDataset(m);
		
		JFreeChart chart = ChartFactory.createXYLineChart(chartTitle, 
				xAxisLabel,yAxisLabel,  dataset);

		customizeChart(chart);
		
	
		return new ChartPanel(chart);
	}
	private JPanel createChartPanel2() throws Exception{ // this method will create the chart panel containin the graph 
		String chartTitle = "Entropy by varying distance";
		String xAxisLabel = "Distance";
		String yAxisLabel = "Entropy";
		Map<String, Map<Double, Double>> m = new HashMap<String, Map<Double,Double>>();
		m = getVal4Vis("data4graphics/cluster");
		System.out.println("size m = "+m.size());
		XYDataset dataset = createCityDataset(m);
		
		JFreeChart chart = ChartFactory.createXYLineChart(chartTitle, 
				xAxisLabel,yAxisLabel,  dataset);

		customizeChart(chart);
		
	
		return new ChartPanel(chart);
	}

	private XYDataset createDataset() {    // this method creates the data as time seris 
		XYSeriesCollection dataset = new XYSeriesCollection();
		XYSeries series1 = new XYSeries("Object 1");
		XYSeries series2 = new XYSeries("Object 2");
		XYSeries series3 = new XYSeries("Object 3");
		
		series1.add(1.0, 2.0);
		series1.add(2.0, 3.0);
		series1.add(3.0, 2.5);
		series1.add(3.5, 2.8);
		series1.add(4.2, 6.0);
		
		series2.add(2.0, 1.0);
		series2.add(2.5, 2.4);
		series2.add(3.2, 1.2);
		series2.add(3.9, 2.8);
		series2.add(4.6, 3.0);
		
		series3.add(1.2, 4.0);
		series3.add(2.5, 4.4);
		series3.add(3.8, 4.2);
		series3.add(4.3, 3.8);
		series3.add(4.5, 4.0);
		
		dataset.addSeries(series1);
		dataset.addSeries(series2);
		dataset.addSeries(series3);
		
		return dataset;
	}
	
	public static Map<String, Map<Double, Double>>getVal4Vis(String file) throws Exception{
		Map<String, Map<Double,Double>>m = new HashMap<String, Map<Double,Double>>();
		File f = new File(file);
		File[] files = f.listFiles();
		
	
		for(int i = 0; i < files.length; i++){
			
			BufferedReader br = new BufferedReader(new FileReader(files[i]));
			System.out.println(files[i].toString());
			br.readLine();
			String line;
			while((line = br.readLine())!= null){
				System.out.println(line);
				String [] r = line.split(",");
				String city = r[0];
				System.out.println("city i-esimo "+city);
				System.out.println(r[1]);
				double ent = Double.parseDouble(r[1]);
				System.out.println(ent+"--> "+r[2]);
				double dist = Double.parseDouble(r[2]);
				System.out.println(dist+" "+r[2]);
				System.out.println(ent+", "+dist);
				Map<Double, Double>map = m.get(city);
				System.out.println(map);
				if(map == null){
					System.out.println("map is null ");
					map = new HashMap<Double, Double>();
					map.put(ent, dist);
				}
				
					System.out.println("map is not null");
					map.put(ent, dist);
					m.put(city, map);
				
				
			}br.close();
			
		}
		
		return m;
	}
	
	private XYDataset createCityDataset( Map<String, Map<Double, Double>>m) throws Exception{ // this method creates the data as time seris 
	
		XYSeriesCollection dataset = new XYSeriesCollection();
		for(String city : m.keySet()){
			XYSeries series1 = new XYSeries(city);

			for(Double st : m.get(city).keySet()){
				series1.add(m.get(city).get(st), st);
			}

			dataset.addSeries(series1);
		}
		return dataset;
	}
	
	
	private void customizeChart(JFreeChart chart) throws Exception{   // here we make some customization
		XYPlot plot = chart.getXYPlot();
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

		// sets paint color for each series
		renderer.setSeriesPaint(0, Color.RED);
		renderer.setSeriesPaint(1, Color.GREEN);
		renderer.setSeriesPaint(2, Color.YELLOW);

		// sets thickness for series (using strokes)
		renderer.setSeriesStroke(0, new BasicStroke(4.0f));
		renderer.setSeriesStroke(1, new BasicStroke(3.0f));
		renderer.setSeriesStroke(2, new BasicStroke(2.0f));
		
		// sets paint color for plot outlines
		plot.setOutlinePaint(Color.BLUE);
		plot.setOutlineStroke(new BasicStroke(2.0f));
		
		// sets renderer for lines
		plot.setRenderer(renderer);
		
		// sets plot background
		plot.setBackgroundPaint(Color.DARK_GRAY);
		
		// sets paint color for the grid lines
		plot.setRangeGridlinesVisible(true);
		plot.setRangeGridlinePaint(Color.BLACK);
		
		plot.setDomainGridlinesVisible(true);
		plot.setDomainGridlinePaint(Color.BLACK);
		
	}
	
	public static void main(String[] args) throws Exception{
		
		MultipleLinesChart ml = new MultipleLinesChart();
		ml.setVisible(true);
			
	
	}
}
