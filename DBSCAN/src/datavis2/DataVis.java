package datavis2;

import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class DataVis {
	
	static String[] cities = {"Milano", "Torino", "Venezia", "Roma", "Bari", "Palermo", "Napoli"};
	
	static String[] col = {"0","1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
			"11", "12", "13", "14", "15", "16", "17", "18", "19", "20", 
			"21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
			"31", "32", "33", "34", "35", "36", "37", "38", "39", "40",
		    "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", 
		    "51", "52", "53", "54", "55", "56", "57", "58", "59", "60",
		    "61", "62", "63", "64", "65", "66", "67", "68", "69", "70",
		    "71", "72", "73", "74", "75", "76", "77", "78", "79", "80",
		    "81", "82", "83", "84", "85", "86", "87", "88", "89", "90",
		    "91", "92", "93", "94", "95", "96", "97", "98", "99", "100", 
		    "101", "102", "103", "104", "105", "106", "107", "108", "109", "110",  
		    "111", "112", "113", "114", "115", "116", "117", "118", "119", "120",
		    "121", "122", "123", "124", "125", "126", "127", "128", "129", "130",
		    "131", "132", "133", "134", "135", "136", "137", "138", "139", "140",
		    "141", "142", "143", "144", "145", "146", "147", "148", "149", "150",
		    "151", "152", "153", "154", "155", "156", "157", "158", "159", "160",
		    "161", "162", "163", "164", "165", "166", "167", "168", "169", "170",
		    "171", "172", "173", "174", "175", "176", "177", "178", "179", "180",
		    "181", "182", "183", "184", "185", "186", "187", "188", "189", "190",
		    "191", "192", "193", "194", "195", "196", "197", "198", "199", "200",
		    "201", "202", "203", "204", "205", "206", "207", "208", "209", "210",
		    "211", "212", "213", "214", "215", "216", "217", "218", "219", "220",
//		    "101", "102", "103", "104", "105", "106", "107", "108", "109", "230",
//		    "101", "102", "103", "104", "105", "106", "107", "108", "109", "240",
//		    "101", "102", "103", "104", "105", "106", "107", "108", "109", "250",
//		    "101", "102", "103", "104", "105", "106", "107", "108", "109", "260",
		    };
	public static void visClusterEntropy(String city, Map<Integer, Double>tm, Map<String, Double>entot){
		
		 DefaultCategoryDataset ds = new DefaultCategoryDataset();
	        int k = 0;
		    for(Integer i : tm.keySet()){
		    	k++;
		    	if(k == 219){
		    		k = 0;
		    	}
		    	String cluster = i.toString();
		    	double totentropy = entot.get(city);
		    //	System.out.println(i+"-->--> "+tm.get(i));
	             ds.addValue((tm.get(i)/totentropy), "A", col[k]);
	             System.out.println("aggiungo in ds cluster "+i+"->"+tm.get(i));
	        }
		    
	        JFreeChart bc = ChartFactory.createBarChart(city,"","Counts", ds,PlotOrientation.VERTICAL,true,false,false);

	        CategoryPlot mainPlot = bc.getCategoryPlot();

	        JFreeChart combinedChart = new JFreeChart(city, mainPlot);
	 
	        ChartFrame cf = new ChartFrame(city, combinedChart);
	        cf.setSize(800, 600);
	        cf.setVisible(true);
	}
	
	
	public static void visMeanEntropy(Map<String, Double>tm){
		
		 DefaultCategoryDataset ds = new DefaultCategoryDataset();
	        int k = 0;
	        int c = 0;
		    for(String i : tm.keySet()){
		    	System.out.println("città che considero "+i);
		    	k++;
		    	if(k == 219){
		    		k = 0;
		    	}
		    	String cluster = i.toString();
		    //	System.out.println(i+"-->--> "+tm.get(i));
	             ds.addValue(tm.get(i), "A",i );
	             System.out.println("Valore tm "+tm.get(i)+" ,--> "+cities[c]);
	             c++;
	             
	             System.out.println("aggiungo in ds cluster "+i+"->"+tm.get(i));
	        }
		    
	        JFreeChart bc = ChartFactory.createBarChart("MeanEntropyPerCity","","Counts", ds,PlotOrientation.VERTICAL,true,false,false);

	        CategoryPlot mainPlot = bc.getCategoryPlot();

	        JFreeChart combinedChart = new JFreeChart("MeanEntropyperCity", mainPlot);
	 
	        ChartFrame cf = new ChartFrame("MeanEntropyPerCity", combinedChart);
	        cf.setSize(800, 600);
	        cf.setVisible(true);
	}
	

}
