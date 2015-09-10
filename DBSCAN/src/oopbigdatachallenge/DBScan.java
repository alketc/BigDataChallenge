package oopbigdatachallenge;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.gps.utils.LatLonPoint;
import org.gps.utils.LatLonUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;


// comment
public class DBScan {
	static String[] COLORS = {"ff000000", "ffff0000","ffffff00",  "ff0000ff", "ff00ff00", "ff99ff99",
			                                                          "ff000011", "ff9999ff", "ffff9999"};
	static String[] cities = {"Milano", "Torino", "Venezia", "Roma", "Bari", "Palermo", "Napoli"};
	static String[] col = {"0","1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
			"11", "12", "13", "14", "15", "16", "17", "18", "19", "20", 
			"21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
			"31", "32", "33", "34", "35", "36", "37", "38", "39", "40",
		    "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", 
		    "51", "52", "53", "54", "55", "56", "57", "58", "59", "60"}; 
	static Map<String, String>cityparam = new HashMap<String, String>();
	
	public static void main(String[] args) throws Exception {
		
	    cityparam.put("Napoli", "600,0");
	    cityparam.put("Milano", "500,0");
	    cityparam.put("Roma", "500,0");
	    cityparam.put("Bari", "800,0");
	    cityparam.put("Venezia", "800,0");
	    cityparam.put("Torino", "700,0");
	    cityparam.put("Palermo", "900,0");
		String test = "4";
		int LB = 0;
		int min_nr_comp_per_clust = 6;
		int dist_multi = 3;
		run(test, LB, cityparam,  min_nr_comp_per_clust, dist_multi);
	}
		public static void run(String test, int LB, Map<String, String>cityparam, int min_nr_comp_per_clust, int dist_multi) throws Exception{
			
		Map<Integer, Double>treemap = new TreeMap<Integer, Double>();
		Map<String, Map<Integer, Double>>treemap2 = new TreeMap<String,Map<Integer, Double>>();
		
		// for each city in cityparam hashmap
		for(String city_p : cityparam.keySet()){

			String [] val = cityparam.get(city_p).split(",");
			
			// declare variables for parameters of clustering
			int min_dist_of_clust = Integer.parseInt(val[0])*dist_multi;
			
            List<Company> allpoints = new ArrayList<Company>();
			Main2PrintClusters.loadCityData("data/CompaniesIntegratedDataset2.csv", allpoints, city_p);

			DBSCANClusterer<Company> dbscan = new DBSCANClusterer<Company>(min_dist_of_clust, min_nr_comp_per_clust, 
					new DistanceMeasure(){

				public double compute(double[] latlon1, double[] latlon2) {

					LatLonPoint p1 = new LatLonPoint(latlon1[0],latlon1[1]);
					LatLonPoint p2 = new LatLonPoint(latlon2[0],latlon2[1]);
					return LatLonUtils.getHaversineDistance(p1, p2);
				}
			});

			List<Cluster<Company>> cluster = dbscan.cluster(allpoints);
			
            //    id, cluster as list of companies -->   hashmap that assigns an integer as id to a cluster 
			Map<Integer, List<Company>> m = new TreeMap<Integer, List<Company>>();
			Map<Integer, Cluster> mtree = new TreeMap<Integer, Cluster>();
			int counter = 0; 
			
			for(Cluster<Company> c: cluster){

				List<Company> list_ci = new ArrayList<Company>();

				for(int i = 0; i < c.getPoints().size(); i++){
					//System.out.println(c.getPoints().get(i));
					list_ci.add(c.getPoints().get(i));
				}
				if(list_ci.size()<= LB)continue;
				m.put(counter, list_ci);
				counter++;
				mtree.put(counter, c);
			}
            // print kml files showing how the clustering looks
			printKML(mtree,city_p+"_DBscan.kml");
//			Main2PrintClusters.printCityKML2(mtree, city_p, "resultsDBSCAN/"+city_p+"_ClustersTestDist+Ateco"
//				+ "_"+min_dist_of_clust+".kml");
            //    id   , list of ateco codes for each cluster
			Map<Integer, List<Integer>>mateco = new HashMap<Integer, List<Integer>>();
			mateco = getAtecoPerCluster(mtree);
            
			// calculate the sectors given the ateco codes of the cluster
			// cluster id, sector, number of points for that sector
			Map<String, Map<String, Integer>>m3 = new HashMap<String, Map<String, Integer>>();
			System.out.println("KML and InfoCluster printed");
			m3 = Main2PrintClusters.getSectorDataPerCluster(mateco);

			// calculate p-values as nr. of companies per sector / tot nr of companies of the cluster
			// cluster id, list of p-values
			Map<String, List<Double>>pvalues = new HashMap<String, List<Double>>();
			pvalues =  Main2PrintClusters.getPValues(m3);
            
			// having the p-values --> calculate entropy per cluster
			// id_cl  , entropy of that cluster
			Map<String, Double>m4 = new TreeMap<String, Double>();
			m4 =  Main2PrintClusters.getEntropyPerCluster(pvalues);
			
			// print entropy data in csv files
			printClusterInfo(city_p, m4);
			printClusterInfoR(city_p, m4);

			for(String s: m4.keySet()){
				treemap.put(Integer.parseInt(s),m4.get(s));
			}
			Map<String, Double>cl_info = new HashMap<String, Double>();
			
		    treemap2.put(city_p, treemap);
			treemap = new TreeMap<Integer, Double>();
			
      }
		
		
		Map<String, Double>citymeanent = new TreeMap<String, Double>();
		
		citymeanent = Main2PrintClusters.getMedianaEntropyPerCity("boxplotdata/DBSCAN");
		for(String s : citymeanent.keySet()){
			System.out.println(s+" +++++++++++++++++++++++++++++++>>>>>>>>>>>>>>>>>> "+citymeanent.get(s));
		}
		printMC(citymeanent, "Test_"+test+"_MeanEntropyPerCity.csv");
		
		for(String s : treemap2.keySet()){
			for(int i = 0; i < treemap2.get(s).size(); i++){
			System.out.println(s+"-->>> "+treemap2.get(s));
			}
		}
		
		for(String s : treemap2.keySet()){
			//visClusterEntropy(s, treemap2.get(s));
		}
		visMeanEntropy(citymeanent);
		
		for(String city : citymeanent.keySet()){
			System.out.println(city+" has entropy ------------------------------------>>>> "+citymeanent.get(city));
		}
		
	}
		public static void printClusterInfoR(String city,Map<String, Double>m4  ) throws Exception{
			PrintWriter out = new PrintWriter(new FileWriter(new File("C:\\Users\\Alket\\Documents\\boxplotdata\\DBSCAN\\"+city+".csv")));
			out.println("cid,city,entropy");
			for(String s: m4.keySet()){
			//	System.out.println("Cluster "+s+" --> "+m4.get(s));
				out.println(s+","+city+","+m4.get(s));
			}
			out.close();
		}
		public static void printClusterInfo(String city,Map<String, Double>m4  ) throws Exception{
			PrintWriter out = new PrintWriter(new FileWriter(new File("boxplotdata/DBSCAN/"+city+".csv")));
			out.println("cid,entropy");
			for(String s: m4.keySet()){
			//	System.out.println("Cluster "+s+" --> "+m4.get(s));
				out.println(s+","+m4.get(s));
			}
			out.close();
		}
	
	public static void printMC(Map<String, Double>citymeanent, String file) throws Exception{
		PrintWriter out =  new PrintWriter(new FileWriter(new File(file)));
	    out.println("city,entropy,distance,nrcluster");;
		for(String s: citymeanent.keySet()){
	    	out.println(s+","+citymeanent.get(s)+","+cityparam.get(s));
	    }
	    out.close();;
	}
	
	public static void visMeanEntropy(Map<String, Double>tm){
		
		 DefaultCategoryDataset ds = new DefaultCategoryDataset();
	        int k = 0;
	        int c = 0;
		    for(String i : tm.keySet()){
//		    	k++;
//		    	if(k == 59){
//		    		k = 0;
//		    	}
		    	String cluster = i.toString();
		    //	System.out.println(i+"-->--> "+tm.get(i));
	             ds.addValue(tm.get(i), "A",i );
	             c++;
//	             if(c== 7) c = 0;
//	             System.out.println("aggiungo in ds cluster "+i+"->"+tm.get(i));
	        }
		    
	        JFreeChart bc = ChartFactory.createBarChart("MeanEntropyPerCity","","Counts", ds,PlotOrientation.VERTICAL,true,false,false);

	        CategoryPlot mainPlot = bc.getCategoryPlot();

	        JFreeChart combinedChart = new JFreeChart("MeanEntropyperCity", mainPlot);
	 
	        ChartFrame cf = new ChartFrame("MeanEntropyPerCity", combinedChart);
	        cf.setSize(800, 600);
	        cf.setVisible(true);
	}
	public static void visClusterEntropy(String city, Map<Integer, Double>tm){
		
		 DefaultCategoryDataset ds = new DefaultCategoryDataset();
	        int k = 0;
		    for(Integer i : tm.keySet()){
		    	k++;
		    	if(k == 40){
		    		k = 0;
		    	}
		    	String cluster = i.toString();
		    //	System.out.println(i+"-->--> "+tm.get(i));
	             ds.addValue(tm.get(i), "A", col[k]);
	             System.out.println("aggiungo in ds cluster "+i+"->"+tm.get(i));
	        }
		    
	        JFreeChart bc = ChartFactory.createBarChart(city,"","Counts", ds,PlotOrientation.VERTICAL,true,false,false);

	        CategoryPlot mainPlot = bc.getCategoryPlot();

	        JFreeChart combinedChart = new JFreeChart(city, mainPlot);
	 
	        ChartFrame cf = new ChartFrame(city, combinedChart);
	        cf.setSize(800, 600);
	        cf.setVisible(true);
	}
public static Map<Integer, List<Integer>> getAtecoPerCluster( Map<Integer, Cluster>mtree)throws Exception{
		
		Map<Integer, List<Integer>> m2 = new HashMap<Integer, List<Integer>>();
		
		for(Integer i : mtree.keySet()){
			List<Integer>li = m2.get(i);
			if(li == null ){
				li = new ArrayList<Integer>();
			}
			for(int j = 0; j < mtree.get(i).getPoints().size(); j++){
				//System.out.println(i+"--> "+mtree.get(i).getPoints().get(j));
				String [] company = mtree.get(i).getPoints().get(j).toString().split("\t");
				int ateco = Integer.parseInt(company[6]);
				li.add(ateco);
			}
			m2.put(i, li);
		}
	
		return m2;
	}
public static void printKML(Map<Integer, Cluster>map,String data)throws Exception{

	ArrayList<Company> p_arr = new ArrayList<Company>();	

	PrintWriter out = new PrintWriter(new FileWriter(data));	

	out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
	out.println("<kml>");
	out.println("<Document>");
	for(int i = 0; i < COLORS.length; i++){
		out.println("<Style id=\""+COLORS[i]+"\">");
		out.println("<IconStyle>");
		out.println("<color>"+COLORS[i]+"</color>");
		out.println("<scale>1.2</scale>");
		out.println("<Icon>");
		out.println("<href>http://maps.google.com/mapfiles/kml/shapes/shaded_dot.png</href>");
		out.println("</Icon>");
		out.println("</IconStyle>");
		out.println("<ListStyle>");
		out.println("</ListStyle>");
		out.println("</Style>");
	}	
	int k = 0;
	Map<Integer, Cluster>map2 = new HashMap<Integer, Cluster>();
	
	for(Cluster c: map.values()){
	
		k++;
		if (k == 3){
			k=0;     // cosi non mi da ArrayIndexOutOfBounds
		}
		for (int i = 0; i < c.getPoints().size(); i++) {
			out.println("<Placemark>");
			String [] r = c.getPoints().get(i).toString().split("\t");
			int ateco = Integer.parseInt(r[6]);
			String eta = r[8];
			String size = r[7];
			out.println("<description> ateco = "+ateco+", age = "+eta+", size = "+size+"</description>");
			out.println("<styleUrl>#"+COLORS[k]+"</styleUrl>");
			out.println("<Point>");
			//System.out.println("string obtained "+c.getPoints().get(i).toString());
			String [] company = c.getPoints().get(i).toString().split("\t");
			double lat = Double.parseDouble(company[2]);
			double lon = Double.parseDouble(company[3]);
			out.print(" <coordinates>"+lat+","+lon+",0 </coordinates>");
			out.println("</Point>");
			out.println("</Placemark>");
		}
	}
	out.println("</Document>");
	out.println("</kml>");
	out.close();
}
}
