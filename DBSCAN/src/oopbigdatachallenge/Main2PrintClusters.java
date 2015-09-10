package oopbigdatachallenge;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class Main2PrintClusters {
	
	static String[] COLORS = {"ff000000", "ffff0000","ffffff00",  "ff0000ff", "ff00ff00", "ff99ff99", 
			                  "ff000011", "ff9999ff", "ffff9999", "ff88ff00", "ffff3333"};	
	static String[] cities = {"Milano", "Torino", "Venezia", "Roma", "Bari", "Palermo", "Napoli"};
	public static Map<String, String>sector = new TreeMap<String, String>();
	public static Map<String, Double>size = new HashMap<String, Double>();
	public static Map<String, Double>onecluster_ent = new HashMap<String, Double>();
	public static Map<String,Map<Integer, Double>>cityclusters = new HashMap<String , Map<Integer, Double>>();
	public static Map<Integer, String>shape = new HashMap<Integer, String>();
	public static Map<String, Double>citymeanent = new HashMap<String, Double>();
	
	public static void main (String[]args)throws Exception{
		
		onecluster_ent.put("Milano", 0.563);
		onecluster_ent.put("Roma", 0.368);
		onecluster_ent.put("Venezia", 0.545);
		onecluster_ent.put("Torino", 0.553);
		onecluster_ent.put("Palermo", 0.346);
		onecluster_ent.put("Bari", 0.512);
		onecluster_ent.put("Napoli",0.455);
		double dist = 1500;
		int lb = 5;
		run(dist, lb);
		
	}
	public static void run(double dist, int lb) throws Exception{
		onecluster_ent.put("Milano", 0.563);
		onecluster_ent.put("Roma", 0.368);
		onecluster_ent.put("Venezia", 0.545);
		onecluster_ent.put("Torino", 0.553);
		onecluster_ent.put("Palermo", 0.346);
		onecluster_ent.put("Bari", 0.512);
		onecluster_ent.put("Napoli",0.455);
		
		
		String data = "data/CompaniesIntegratedDataset2.csv";
		
		for(int str = 0; str < cities.length; str ++){
		
		String city = cities[str];
	    
		Map<Integer, Cluster> map = new HashMap<Integer,Cluster>();
        ArrayList<Company> p_arr = new ArrayList<Company>();  
	    // load data for city str 
		loadCityData(data, p_arr, city); 
      
		 System.out.println(city+" --> "+p_arr.size());
    
		for (int i = 0; i < p_arr.size(); i++) {

			System.out.println("Points remaining -----------"+p_arr.size());
			if(p_arr.size() == 0)
				break;
			Object[] c_rem = Cluster.cluster(i,p_arr, dist);
		  //Object[] c_rem = Cluster.cluster2(i,p_arr, dist);
			
			map.put(i, (Cluster)c_rem[0]);
			p_arr = (ArrayList<Company>)c_rem[1];
	    }
		
		Map<Integer, Cluster> newmap = new HashMap<Integer,Cluster>();
		// clusters with a nr of points > LB
		newmap = getClusterByLB(map, lb);
	    // print KML
		printCityKML2(newmap, city, "results/"+city+"_ClustersTestDist+Ateco"
				+ "_"+dist+".kml");
        // get ateco list per cluster
		Map<Integer, List<Integer>> m2 = new HashMap<Integer, List<Integer>>();
		m2 =  getAtecoPerCluster("InfoCluster.csv",newmap);

		// having the ateco codes organize data per sector 
		//  id_cl,       sector, nr of companies
		Map<String, Map<String, Integer>>m3 = new TreeMap<String, Map<String, Integer>>();
		System.out.println("KML and InfoCluster printed");
		m3 = getSectorDataPerCluster(m2);
		
		// calculate p-values as nr. of companies per sector / tot nr of companies of the cluster
		Map<String, List<Double>>pvalues = new HashMap<String, List<Double>>();
		pvalues = getPValues(m3);
		
		// having the p-values --> calculate entropy per cluster
		// id_cl  , entropy
		Map<String, Double>m4 = new HashMap<String, Double>();
		m4 = getEntropyPerCluster(pvalues);
		
		Map<Integer, Double>convert = new HashMap<Integer, Double>();
		for(String s : m4.keySet()){
			convert.put(Integer.parseInt(s), m4.get(s));
		}
		
		// city and cluster entropies of that city 
		cityclusters.put(city, convert);
		
		printClusterInfo(city, m4, "boxplotdata/H/"+city+".csv");
		printClusterInfoR(city,m4, "C:/Users/Alket/Documents/boxplotdata/H/"+city+".csv");

		citymeanent = getMedianaEntropyPerCity("boxplotdata/H");
		print(citymeanent, "MeanEntropyPerCity.csv");
		}
		
		// visualize histogram of clusters entropy for each city
		
		for(String s : cityclusters.keySet()){
			//datavis2.DataVis.visClusterEntropy(s, cityclusters.get(s),onecluster_ent );
		}
		// visualize mean entropy per city histogram 
		datavis2.DataVis.visMeanEntropy(citymeanent);
	}
	
	public static void printClusterInfo(String city,Map<String, Double>m4 , String file ) throws Exception{
		PrintWriter out = new PrintWriter(new FileWriter(new File(file)));
		out.println("cid,entropy");
		for(String s: m4.keySet()){
		//	System.out.println("Cluster "+s+" --> "+m4.get(s));
			out.println(s+","+m4.get(s));
		}
		out.close();
	}
	public static void printClusterInfoR(String city,Map<String, Double>m4  , String file) throws Exception{
		PrintWriter out = new PrintWriter(new FileWriter(new File(file)));
		out.println("cid,city,entropy");
		for(String s: m4.keySet()){
		//	System.out.println("Cluster "+s+" --> "+m4.get(s));
			out.println(s+","+city+","+m4.get(s));
		}
		out.close();
	}
	public static Map<Integer, Cluster>getClusterByLB(Map<Integer, Cluster> oldmap, int LB){
		Map<Integer, Cluster> newmap = new HashMap<Integer,Cluster>();
		for(Integer i : oldmap.keySet()){
			if(oldmap.get(i).cluster.size()<=LB){ // if size of cluster < LB continue;
				System.out.println("cluster scartato di grandezza "+oldmap.get(i).cluster.size());
				continue;
			}
			else newmap.put(i, oldmap.get(i));
			
		}
		return newmap;
		

	}
	public static void print(Map<String, Double>citymeanent, String file) throws Exception{
		PrintWriter out =  new PrintWriter(new FileWriter(new File(file)));
	    out.println("city,entropy,distance,nrcluster");;
		for(String s: citymeanent.keySet()){
	    	out.println(s+","+citymeanent.get(s));
	    }
	    out.close();;
	}
	
	public static Map<String, Double> getMedianaEntropyPerCity(String file) throws Exception{
		
		Map<String, Double>m = new HashMap<String, Double>();
		
		File f = new File(file);
		File [] folder = f.listFiles();
		
		for(int i = 0; i < folder.length; i++){
			DescriptiveStatistics d = new DescriptiveStatistics();
		    	BufferedReader br = new BufferedReader(new FileReader(folder[i]));
		    	String line;
		    	String city = folder[i].getName();
		    	String cityname = city.substring(0, city.indexOf(".csv"));
		    	//double oce_val = oce.get(cityname);
		    	br.readLine();
		    	while((line = br.readLine())!= null){
		    		String [] r = line.split(",");
		    		double e = Double.parseDouble(r[1]);
		    		d.addValue(e);
		    	}
		    	br.close();
		    	double mediana = d.getMean();
		    	m.put(cityname, mediana);
		}
		
		return m;
		
	}
	public static Map<String, Double> getEntropyPerCluster(Map<String, List<Double>>p){
		
		Map<String , Double>m = new HashMap<String, Double>();
        
		for(String s : p.keySet()){
			
			double entropy = 0;
			
			for(int i = 0; i < p.get(s).size(); i++){
				
			    entropy = entropy - (p.get(s).get(i) * Math.log(p.get(s).get(i)));
			}
			m.put(s, entropy);
		}
		
		return m;
	}
	public static Map<String, List<Double>>getPValues(Map<String, Map<String, Integer>>sectordata) throws Exception{
		
		Map<String, List<Double>>pvalues = new HashMap<String, List<Double>>();
		 
		
		for(String s: sectordata.keySet()){
			double sum =0;
			List<Double>li = new ArrayList<Double>();
			
			for(String s2 : sectordata.get(s).keySet()){
				  sum += sectordata.get(s).get(s2);
				 
			}
	        
			for(String s2 : sectordata.get(s).keySet()){
				li.add((sectordata.get(s).get(s2)/sum));
			}
			pvalues.put(s, li);
		}
		
		return pvalues;
	}
	     
	public static Map<String, Map<String, Integer>> getSectorDataPerCluster( Map<Integer, List<Integer>> m2){
		 Map<String, Map<String, Integer>>m3 = new HashMap<String, Map<String, Integer>>();
         String s1 = "primario";
		 String s2 = "secondario";
		 String s3 = "terziario";
		 
		 for (Integer s: m2.keySet()) {
			
			 Map<String, Integer>mi = new HashMap<String,Integer>();
			 
			 for (int i = 0; i < m2.get(s).size(); i++) {
				  
				 int ateco_i = m2.get(s).get(i);
				
				 if(ateco_i < 10 ){
					// System.out.println("-->ateco <10 "+ateco_i);
						 Integer is1 = mi.get(s1);
						 if(is1 == null){
						   mi.put(s1,1);
						 }
						 else mi.put(s1, is1+1);
					 }
				 
				 else if(ateco_i >= 10 && ateco_i <= 32){
					// System.out.println("-->ateco >10 <32 "+ateco_i);
						Integer is2 = mi.get(s2);
						if(is2 == null){
							mi.put(s2, 1);
						}
						else mi.put(s2, is2+1);
					 } 
				 
				 else if(ateco_i >= 33 && ateco_i <= 99){
					// System.out.println("-->ateco >33 < 99 "+ateco_i);
						 Integer is3 = mi.get(s3);
						 if(is3 == null){
						   mi.put(s3, 1);
						 }
                         else mi.put(s3,is3+1);
				 }
				
			 }
			// System.out.println("--> "+mi.size());
			 m3.put(s.toString(), mi);
		 }
		// System.out.println(m3.size());
		 for(String str : m3.keySet()){
			// System.out.println(str+", "+m3.get(str).toString());
		 }
		 return m3;
	}
		 
	public static Map<Integer, List<Integer>> getAtecoPerCluster(String file, Map<Integer, Cluster>m)throws Exception{
		
		PrintWriter out = new PrintWriter(new FileWriter(new File(file)));
		String line;
		
		Map<Integer, List<Integer>> ateco2cluster = new HashMap<Integer, List<Integer>>();
		
		for(int i : m.keySet()){ // for each cluster
			
			List<Integer>li = ateco2cluster.get(i);
			
			for(int j = 0; j < m.get(i).cluster.size();j++){ // for each point in the cluster
				
				out.println(j+","+i+","+m.get(i).cluster.get(j).ateco);
			    
			    int ateco = m.get(i).cluster.get(j).ateco;
			   
			    if(li == null){
			    	li = new ArrayList<Integer>();
			    	li.add(ateco);
			    }
			    else{
			    	li.add(ateco);
			    }
			}
			ateco2cluster.put(i, li);
		}
		out.close();
		return ateco2cluster;
	}
	public static void printCityKML(Map<Integer, Cluster>map,String city, String data)throws Exception{

		sector.put("primario", "ff0000ff");
		sector.put("terziario","ffff0000");
		sector.put( "secondario","ff00ff00");
	
//		sector.put("n.d.", "ff000000");
		
		size.put("micro", 0.5);
		size.put("piccola", 1.0);
		size.put("media", 1.5);
		size.put("grande", 2.5);
		size.put("n.d.", 1.1);
		
		PrintWriter out = new PrintWriter(new FileWriter(data));	

		out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		out.println("<kml>");
		out.println("<Document>");
		
		// generate the different styles
		
		for(String sec : sector.keySet()){
			for(String siz : size.keySet()){
				
				out.println("<Style id=\""+sector.get(sec)+"-"+size.get(siz)+"\">");
				out.println("<IconStyle>");
				out.println("<color>"+sector.get(sec)+"</color>");
				out.println("<scale>"+size.get(siz)+"</scale>");
				out.println("<Icon>");
				out.println("<href>http://maps.google.com/mapfiles/kml/shapes/shaded_dot.png</href>");
				out.println("</Icon>");
				out.println("</IconStyle>");
				out.println("<ListStyle>");
				out.println("</ListStyle>");
				out.println("</Style>");
		    }
		}	
		
		for (Cluster c : map.values()) {
			for(int j = 0; j < c.cluster.size(); j++){
			out.println("<Placemark>");
			out.println("<description>"+c.cluster.get(j).ateco+"</description>");
			String csize = c.cluster.get(j).getDim(); // find company dimension
			//System.out.println(" com size "+csize);
			double psize = size.get(csize);
			int ateco = c.cluster.get(j).getAteco();
			//System.out.println(ateco+" ateco");
			String settore = "";
			if(ateco < 10) settore = "primario";
			if(ateco >= 10 && ateco <= 32) settore = "secondario";
			if(ateco >= 33) settore = "terziario";
			if(ateco == 10000)settore = "n.d.";
			String color = sector.get(settore);
			//System.out.println(color);
			
			out.println("<styleUrl>#"+color+"-"+psize+"</styleUrl>");
			out.println("<Point>");
			out.print(" <coordinates>"+c.cluster.get(j).getLat()+","+c.cluster.get(j).getLon()+",0 </coordinates>");
			out.println("</Point>");
			out.println("</Placemark>");
			}
		}
		
		out.println("</Document>");
		out.println("</kml>");
		out.close();
	}
	public static void printCityKML2(Map<Integer, Cluster>map,String city, String data)throws Exception{

		sector.put("primario", "ff0000ff");
		sector.put( "secondario","ff00ff00");
		sector.put("terziario","ffff0000");
//		sector.put("n.d.", "ff000000");
//		
		size.put("micro", 0.5);
		size.put("piccola", 1.0);
		size.put("media", 1.5);
		size.put("grande", 2.5);
		size.put("n.d.", 1.1);
		
		PrintWriter out = new PrintWriter(new FileWriter(data));	

		out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		out.println("<kml>");
		out.println("<Document>");
	    // generate the different styles
		

		 for(int j = 0; j < COLORS.length; j++){
				out.println("<Style id=\""+COLORS[j]+"\">");
				out.println("<IconStyle>");
				out.println("<color>"+COLORS[j]+"</color>");
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
		for(Cluster c : map.values()){
			  k++;
				if (k == 8){
					k=0;     // cosi non mi da ArrayIndexOutOfBounds
				}
			for(int i = 0; i < c.cluster.size(); i++){
			out.println("<Placemark>");
			out.println("<description>"+"ateco ="+c.cluster.get(i).ateco+", cluster = "+c.id+"</description>");
			String csize = c.cluster.get(i).getDim(); // find company dimension
			//if(csize.equals("micro")|| csize.equals("n.d.")|| csize.equals("piccola")) continue;
			//System.out.println(" com size "+csize);
			double psize = size.get(csize);
			int ateco = c.cluster.get(i).getAteco();
			//System.out.println(ateco+" ateco");
			String settore = "";
			if(ateco < 10) settore = "primario";
			if(ateco >= 10 && ateco <= 32) settore = "secondario";
			if(ateco >= 33) settore = "terziario";
			if(ateco == 10000)settore = "n.d.";
			String color = sector.get(settore);
			//System.out.println("il K vale ---------------------> "+k);
			
			out.println("<styleUrl>#"+COLORS[k]+"</styleUrl>");
			out.println("<Point>");
			out.print(" <coordinates>"+c.cluster.get(i).getLat()+","+c.cluster.get(i).getLon()+",0 </coordinates>");
			out.println("</Point>");
			out.println("</Placemark>");
			}
		}
		
		out.println("</Document>");
		out.println("</kml>");
		out.close();
	}
	public static void printCityKML3(Map<Integer, Cluster>map,String city, String data)throws Exception{

		sector.put("primario", "ff0000ff");
		sector.put( "secondario","ff00ff00");
		sector.put("terziario","ffff0000");
	    sector.put("n.d.", "ff000000");
		//		
		size.put("micro", 0.5);
		size.put("piccola", 1.0);
		size.put("media", 1.5);
		size.put("grande", 2.0);
		//size.put("n.d.", 1.1);

		PrintWriter out = new PrintWriter(new FileWriter(data));	

		out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		out.println("<kml>");
		out.println("<Document>");
		// generate the different styles

				for(String sec : sector.keySet()){
					
					for(int i = 1; i < 5; i++){
						
						    out.println("<Style id=\""+sector.get(sec)+"-"+i+"\">");
							out.println("<IconStyle>");
							out.println("<color>"+sector.get(sec)+"</color>");
							out.println("<scale>"+i+"</scale>");
							out.println("<Icon>");
							out.println("<href>http://maps.google.com/mapfiles/kml/shapes/shaded_dot.png</href>");
							out.println("</Icon>");
							out.println("</IconStyle>");
							out.println("<ListStyle>");
							out.println("</ListStyle>");
							out.println("</Style>");
						}
			     }
		
		int k = 1;
		for(Cluster c : map.values()){
			k++;
			if (k == 5){
				k=1;     // cosi non mi da ArrayIndexOutOfBounds
			}
			for(int i = 0; i < c.cluster.size(); i++){
				out.println("<Placemark>");
				out.println("<description>"+c.cluster.get(i).ateco+"</description>");
				String csize = c.cluster.get(i).getDim(); // find company dimension
				
				//double psize = size.get(csize);
				int ateco = c.cluster.get(i).getAteco();
				//System.out.println(ateco+" ateco");
				String settore = "";
				if(ateco < 10) settore = "primario";
				if(ateco >= 10 && ateco <= 32) settore = "secondario";
				if(ateco >= 33) settore = "terziario";
				if(ateco == 10000)settore = "n.d.";
				String color = sector.get(settore);
				//System.out.println("il K vale ---------------------> "+k);

				out.println("<styleUrl>#"+color+"-"+(k-1)+"</styleUrl>");
				out.println("<Point>");
				out.print(" <coordinates>"+c.cluster.get(i).getLat()+","+c.cluster.get(i).getLon()+",0 </coordinates>");
				out.println("</Point>");
				out.println("</Placemark>");
			}
		}

		out.println("</Document>");
		out.println("</kml>");
		out.close();
	}

	public static void loadCityData(String file, List<Company>m, String city) throws Exception{

		
		String line; 
	
		BufferedReader br = new BufferedReader(new FileReader(new File(file)));
		br.readLine();
		while((line = br.readLine())!= null){
			//System.out.println(line);
			
			String [] r = line.split("\t");
			String citta = r[4];
			
			if(citta.equals(city)){
			String id = r[0];
			double lat = Double.parseDouble(r[1]); 
			double lon = Double.parseDouble(r[2]); 
			
			String k = r[3]; 
		
				String name = r[5];
				int a = Integer.parseInt(r[6]); 
				if(a == 10000 ) continue;
				String dim = r[7]; 
				String age = r[8]; 
				if(dim.equals("n.d.")||dim.equals("micro")|| dim.equals("piccola")||dim.equals("media"))continue;
//				if(dim.equals("n.d.")||dim.equals("micro"))continue;
				m.add(new Company(id, name,lat,lon,k,citta,a,dim,age));
			}
			
		}br.close();
	

	}
}
