package datavis2;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;

/** @see http://stackoverflow.com/questions/6844759 */
public class BoxAndWiskerDemo2 {

	String [] algs = {"DBSCAN", "H"};
    private static final int COLS = 2; // categorie
    private static final int VISIBLE = 2;
    private static final int ROWS = 7;
    private static final int VALUES = 10;
    private static final Random rnd = new Random();
    private List<String> columns;
    private List<List<List<Double>>> data;
    private DefaultBoxAndWhiskerCategoryDataset dataset;
    private CategoryPlot plot;
    private ChartPanel chartPanel;
    private JPanel controlPanel;
    private int start = 0;

    public BoxAndWiskerDemo2() {
        createData();
        try{
        createDataset2();
        }catch(Exception e){
        	e.getMessage();
        }
        createChartPanel();
        createControlPanel();
    }

    private void createData() {
        columns = new ArrayList<String>(COLS);
        data = new ArrayList<List<List<Double>>>();
        for (int i = 0; i < COLS; i++) {
            String name = "Category" + String.valueOf(i + 1);
            columns.add(name);
            List<List<Double>> list = new ArrayList<List<Double>>();
            for (int j = 0; j < ROWS; j++) {
                list.add(createValues());
            }
            data.add(list);
        }
    }

    private List<Double> createValues() {
        List<Double> list = new ArrayList<Double>();
        for (int i = 0; i < VALUES; i++) {
            list.add(rnd.nextGaussian());
        }
        return list;
    }

    private void createDataset() {
        dataset = new DefaultBoxAndWhiskerCategoryDataset();
        for (int i = start; i < start + VISIBLE; i++) {
            List<List<Double>> list = data.get(i);
            int row = 0;
            for (List<Double> values : list) {
                String category = columns.get(i);
                dataset.add(values, "s" + row++, category);
            }
        }
        for (int i = 0; i < 1; i++) {
			System.out.println(dataset.getRowCount());
		}
    }
    private void createDataset2() throws Exception{
    	
        dataset = new DefaultBoxAndWhiskerCategoryDataset();
        
        for(String s: algs){
        	File folder = new File("boxplotdata/"+s);
        	File[] f = folder.listFiles();
        	
        	for(int i = 0; i < f.length; i++){
        		
        		BufferedReader br = new BufferedReader(new FileReader(f[i]));
        		br.readLine();
        		String line; 
        		List<Double> li = new ArrayList<Double>();
        		while((line = br.readLine())!= null){
        			
        			String [] r = line.split(",");
        			double val_ent = Double.parseDouble(r[1]);
        			li.add(val_ent);
        			String category = f[i].getName();
        			dataset.add(li, s, category);
        			
        		}br.close();
        	}
        	
        }
        
       
    }

    private void createChartPanel() {
        CategoryAxis xAxis = new CategoryAxis("Category");
        NumberAxis yAxis = new NumberAxis("Value");
        BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
        plot = new CategoryPlot(dataset, xAxis, yAxis, renderer);
        JFreeChart chart = new JFreeChart("BoxAndWhiskerDemo", plot);
        chartPanel = new ChartPanel(chart);
        print();
    }

    private void createControlPanel() {
        controlPanel = new JPanel();
        controlPanel.add(new JButton(new AbstractAction("\u22b2Prev") {

            @Override
            public void actionPerformed(ActionEvent e) {
                start -= VISIBLE;
                if (start < 0) {
                    start = 0;
                    return;
                }
                createDataset();
                plot.setDataset(dataset);
            }
        }));
        controlPanel.add(new JButton(new AbstractAction("Next\u22b3") {

            @Override
            public void actionPerformed(ActionEvent e) {
                start += VISIBLE;
                if (start > COLS - VISIBLE) {
                    start = COLS - VISIBLE;
                    return;
                }
                createDataset();
                plot.setDataset(dataset);
            }
        }));
    }

    public ChartPanel getChartPanel() {
        return chartPanel;
    }

    public JPanel getControlPanel() {
        return controlPanel;
    }
    public void print(){
    	   
     		for(List<List<Double>>d1 : data){
     		//	System.out.println(d1);
     			for(int i = 0; i < d1.size(); i++){
     			System.out.println(d1.get(i));
     		}
     		}
    }

    public static void main(String[] args) throws IOException {
    	
    	
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                BoxAndWiskerDemo2 demo = new BoxAndWiskerDemo2();
                frame.add(demo.getChartPanel(), BorderLayout.CENTER);
                frame.add(demo.getControlPanel(), BorderLayout.SOUTH);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}
