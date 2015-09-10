package oopbigdatachallenge;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Main2Entropy {


	static String[] COLORS = {"ff000000", "ffff0000","ffffff00",  "ff0000ff", "ff00ff00", "ff99ff99", 
			                  "ff000011", "ff9999ff", "ffff9999", "ff88ff00", "ffff3333"};	
	static String[] cities = {"Milano", "Torino", "Venezia", "Roma", "Bari", "Palermo", "Napoli"};
	
	static Map<String, String>cityparam = new HashMap<String, String>();
	
	public static Map<String, String>sector = new TreeMap<String, String>();
	public static Map<String, Double>size = new HashMap<String, Double>();
	public static Map<String, Double>onecluster_ent = new HashMap<String, Double>();
	public static Map<String,Map<Integer, Double>>cityclusters = new HashMap<String , Map<Integer, Double>>();
	public static Map<Integer, String>shape = new HashMap<Integer, String>();
	public static Map<String, Double>citymeanent = new HashMap<String, Double>();

	public static void main (String[]args)throws Exception{
		    cityparam.put("Napoli", "900,0");
		    cityparam.put("Milano", "1200,0");
		    cityparam.put("Roma", "1300,0");
		    cityparam.put("Bari", "700,0");
		    cityparam.put("Venezia", "700,0");
		    cityparam.put("Torino", "800,0");
		    cityparam.put("Palermo", "700,0");
	

		double dist = 3000;
		int lb = 4;
		
		Main2PrintClusters.run(dist, lb);
		int min_nr_comp_per_clust =4;
		int dist_multi = 9; 
		DBScan.run("1", lb, cityparam, min_nr_comp_per_clust, dist_multi);
		
		datavis2.BoxAndWiskerDemo2.main(args);

	}

}
