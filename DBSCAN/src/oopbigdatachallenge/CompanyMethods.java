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

import org.gps.utils.LatLonPoint;
import org.gps.utils.LatLonUtils;

public class CompanyMethods {
	
	static String[] COLORS = {"ff0000ff", "ff00ff00", "ffff0000", "ff000000"};	
	public static Map<String, String>sector = new HashMap<String, String>();
	public static Map<String, Double>size = new HashMap<String, Double>();
	
public static void print(String file, Map<String, Integer>m) throws Exception{
		
		PrintWriter out = new PrintWriter(new FileWriter(new File(file)));
		
		
		for(String s : m.keySet()){
			
			out.println(s+","+m.get(s));
		}
		out.close();
	}

public static void printCityKML(Map<String, List<Company>>map,String city, String data)throws Exception{

	sector.put("primario", "ff0000ff");
	sector.put( "secondario","ff00ff00");
	sector.put("terziario","ffff0000");
	sector.put("n.d.", "ff000000");
	
	size.put("micro", 0.5);
	size.put("piccola", 1.0);
	size.put("media", 1.5);
	size.put("grande", 2.5);
	size.put("n.d.", 1.1);
	PrintWriter out = new PrintWriter(new FileWriter(data));	

	out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
	out.println("<kml>");
	out.println("<Document>");
	
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
	
	for (int i = 0; i < map.get(city).size(); i++) {
		out.println("<Placemark>");
		
		String csize = map.get(city).get(i).getDim(); // find company dimension
		//System.out.println(" com size "+csize);
		double psize = size.get(csize);
		int ateco = map.get(city).get(i).getAteco();
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
		out.print(" <coordinates>"+map.get(city).get(i).getLat()+","+map.get(city).get(i).getLon()+",0 </coordinates>");
		out.println("</Point>");
		out.println("</Placemark>");
	}
	//      }
	out.println("</Document>");
	out.println("</kml>");
	out.close();
}

public static void loadCityData(String file, Map<String,List<Company>>m, String city) throws Exception{

	List<Company>l = new ArrayList<Company>();
	String line; 
	int counter = 0;
	BufferedReader br = new BufferedReader(new FileReader(new File(file)));
	br.readLine();
	while((line = br.readLine())!= null){
		//System.out.println(line);

		String [] r = line.split("\t");
		String id = r[0];
		double lat = Double.parseDouble(r[1]); 
		double lon = Double.parseDouble(r[2]); 
		String k = r[3]; 
		String citta = r[4];
		if(citta.equals(city)){
			String name = r[5];
			int a = Integer.parseInt(r[6]); 
			String dim = r[7]; 
			String age = r[8]; 
			if(dim.equals("n.d.")||dim.equals("micro")||dim.equals("piccola"))continue;
			l.add(new Company(id, name,lat,lon,k,citta,a,dim,age));
		}
		counter++;
		if(counter == 500000) break;
	}br.close();
	m.put(city, l);

}
    public static void loadComData(String file, List<Company>l) throws Exception{
    	
    	String line; 
		int counter = 0;
		BufferedReader br = new BufferedReader(new FileReader(new File(file)));
		br.readLine();
		while((line = br.readLine())!= null){
			//System.out.println(line);
			
			String [] r = line.split("\t");
			String id = r[0];
			double lat = Double.parseDouble(r[1]); 
			double lon = Double.parseDouble(r[2]); 
			String k = r[3]; 
			String citta = r[4];
			String name = r[5];
			int a = Integer.parseInt(r[6]); 
		    String dim = r[7]; 
			String age = r[8]; 
			counter++;
			
			l.add(new Company(id, name,lat,lon,k,citta,a,dim,age));
			if(counter == 10000) break;
		}br.close();
    	
    }
    public static void printKML(ArrayList <Company> list,String data)throws Exception{
		
    	
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
	  for(int i = 0; i < list.size(); i++){
    	  
		  	out.println("<Placemark>");
				out.println("<styleUrl>#"+COLORS[k]+"</styleUrl>");
				//out.println("<scale>"+1.1+"</scale>");
				out.println("<Point>");
		        out.print(" <coordinates>"+list.get(i).getLat()+","+list.get(i).getLon()+",0 </coordinates>");
		        out.println("</Point>");
				out.println("</Placemark>");
			}
	  
	         out.println("</Document>");
		     out.println("</kml>");
		     out.close();
}	
	
//    public static boolean haversineDistSector(Company a, Company b){
//		
//	    LatLonPoint gp1 = new LatLonPoint(a.lat, a.lon);
//		LatLonPoint gp2 = new LatLonPoint(b.lat,b.lon );
//		
//		int sec_a = a.getAteco();
//		int sec_b = b.getAteco();
//		
//	    double d = LatLonUtils.getHaversineDistance(gp1, gp2);	
//	    
//	    return d;
//	}
  public static double haversineDist(Company a, Company b){
		
	    LatLonPoint gp1 = new LatLonPoint(a.lat, a.lon);
		LatLonPoint gp2 = new LatLonPoint(b.lat,b.lon );
		
	    double d = LatLonUtils.getHaversineDistance(gp1, gp2);	
				
	    return d;
	}
}