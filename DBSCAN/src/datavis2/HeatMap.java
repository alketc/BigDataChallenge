package datavis2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import oopbigdatachallenge.Company;

public class HeatMap {
	
	public static void genHeatmap(List<Company>l, String file) throws Exception{
		
		PrintWriter out = new PrintWriter(new FileWriter(new File(file)));
		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<head>");
		out.println("<meta charset=\"utf-8\">");
		out.println("<title>Heatmaps</title>");
		out.println("<style>");
		out.println("html, body, #map-canvas {");
		out.println("height: 100%;");
		out.println("margin: 0;");
		out.println("padding: 0;");
		out.println("}");

		out.println("</style>");
		out.println("<script src=\"https://maps.googleapis.com/maps/api/js?v=3.exp&signed_in=true&libraries=visualization\"></script>");
		out.println("<script>");
		out.println("var map, pointarray, heatmap;");

		out.println("var taxiData = [");
		int counter = 0; 
		for(int i = 0; i < l.size(); i++){
			counter++;
			if(counter == l.size()){
			out.println("new google.maps.LatLng("+l.get(i).getLon()+", "+l.get(i).getLat()+")");
			}
			else{
				out.println("new google.maps.LatLng("+l.get(i).getLon()+", "+l.get(i).getLat()+"),");
			}
		}
		out.println("];");

		out.println("function initialize() {");
		out.println("var mapOptions = {");
		out.println("zoom: 10,");
		out.println("center: new google.maps.LatLng("+l.get(l.size()/2).getLon()+","+l.get(l.size()/2).getLat()+"),");
		out.println("mapTypeId: google.maps.MapTypeId.SATELLITE");
		out.println("};");

		out.println("map = new google.maps.Map(document.getElementById('map-canvas'),");
		out.println("mapOptions);");

		out.println("var pointArray = new google.maps.MVCArray(taxiData);");

		out.println("heatmap = new google.maps.visualization.HeatmapLayer({");
		out.println("data: pointArray");
		out.println("});");

		out.println("heatmap.setMap(map);");
		out.println("}");

		out.println("function toggleHeatmap() {");
		out.println("heatmap.setMap(heatmap.getMap() ? null : map);");
		out.println("}");

		out.println("function changeGradient() {");
		out.println("var gradient = [");
		out.println("'rgba(0, 255, 255, 0)',");
		out.println("'rgba(0, 255, 255, 1)',");
		out.println("'rgba(0, 191, 255, 1)',");
		out.println("'rgba(0, 127, 255, 1)',");
		out.println("'rgba(0, 63, 255, 1)'");
		    
		out.println("]");
		out.println("heatmap.set('gradient', heatmap.get('gradient') ? null : gradient);");
		out.println("}");

		out.println("function changeRadius() {");
		out.println("heatmap.set('radius', heatmap.get('radius') ? null : 20);");
		out.println("}");

		out.println("function changeOpacity() {");
		out.println("heatmap.set('opacity', heatmap.get('opacity') ? null : 0.2);");
		out.println("}");

		out.println("google.maps.event.addDomListener(window, 'load', initialize);");
		out.println("</script>");
		out.println("</head>");

		out.println("<body>");
		out.println("<div id=\"panel\">");
		out.println("<button onclick=\"toggleHeatmap()\">Toggle Heatmap</button>");
		out.println("<button onclick=\"changeGradient()\">Change gradient</button>");
		out.println("<button onclick=\"changeRadius()\">Change radius</button>");
		out.println("<button onclick=\"changeOpacity()\">Change opacity</button>");
		out.println("</div>");
		out.println("<div id=\"map-canvas\"></div>");
		out.println("</body>");
		out.println("</html>");
		out.close();
	}
public static void genHeatmap2(List<double[]>l, String file) throws Exception{
		
		PrintWriter out = new PrintWriter(new FileWriter(new File(file)));
		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<head>");
		out.println("<meta charset=\"utf-8\">");
		out.println("<title>Heatmaps</title>");
		out.println("<style>");
		out.println("html, body, #map-canvas {");
		out.println("height: 100%;");
		out.println("margin: 0;");
		out.println("padding: 0;");
		out.println("}");

		out.println("</style>");
		out.println("<script src=\"https://maps.googleapis.com/maps/api/js?v=3.exp&signed_in=true&libraries=visualization\"></script>");
		out.println("<script>");
		out.println("var map, pointarray, heatmap;");

		out.println("var taxiData = [");
		int counter = 0; 
		for(int i = 0; i < l.size(); i++){
			counter++;
			if(counter == l.size()){
			out.println("new google.maps.LatLng("+l.get(i)[0]+", "+l.get(i)[1]+")");
			}
			else{
				out.println("new google.maps.LatLng("+l.get(i)[0]+", "+l.get(i)[1]+"),");
			}
		}
		out.println("];");

		out.println("function initialize() {");
		out.println("var mapOptions = {");
		out.println("zoom: 10,");
		out.println("center: new google.maps.LatLng("+l.get(l.size()/2)[0]+","+l.get(l.size()/2)[1]+"),");
		out.println("mapTypeId: google.maps.MapTypeId.SATELLITE");
		out.println("};");

		out.println("map = new google.maps.Map(document.getElementById('map-canvas'),");
		out.println("mapOptions);");

		out.println("var pointArray = new google.maps.MVCArray(taxiData);");

		out.println("heatmap = new google.maps.visualization.HeatmapLayer({");
		out.println("data: pointArray");
		out.println("});");

		out.println("heatmap.setMap(map);");
		out.println("}");

		out.println("function toggleHeatmap() {");
		out.println("heatmap.setMap(heatmap.getMap() ? null : map);");
		out.println("}");

		out.println("function changeGradient() {");
		out.println("var gradient = [");
		out.println("'rgba(0, 255, 255, 0)',");
		out.println("'rgba(0, 255, 255, 1)',");
		out.println("'rgba(0, 191, 255, 1)',");
		out.println("'rgba(0, 127, 255, 1)',");
		out.println("'rgba(0, 63, 255, 1)'");
		    
		out.println("]");
		out.println("heatmap.set('gradient', heatmap.get('gradient') ? null : gradient);");
		out.println("}");

		out.println("function changeRadius() {");
		out.println("heatmap.set('radius', heatmap.get('radius') ? null : 20);");
		out.println("}");

		out.println("function changeOpacity() {");
		out.println("heatmap.set('opacity', heatmap.get('opacity') ? null : 0.2);");
		out.println("}");

		out.println("google.maps.event.addDomListener(window, 'load', initialize);");
		out.println("</script>");
		out.println("</head>");

		out.println("<body>");
		out.println("<div id=\"panel\">");
		out.println("<button onclick=\"toggleHeatmap()\">Toggle Heatmap</button>");
		out.println("<button onclick=\"changeGradient()\">Change gradient</button>");
		out.println("<button onclick=\"changeRadius()\">Change radius</button>");
		out.println("<button onclick=\"changeOpacity()\">Change opacity</button>");
		out.println("</div>");
		out.println("<div id=\"map-canvas\"></div>");
		out.println("</body>");
		out.println("</html>");
		out.close();
	}
	
	public static void main(String [] args)throws Exception{
		
		String file = "";
		File fi = new File(file);
		List<double[]>l = new ArrayList<double[]>();
		File[] f = fi.listFiles();
		
		for(int i = 0; i < f.length; i++){
			String line; 
			BufferedReader br = new BufferedReader(new FileReader(f[i]));
			while((line = br.readLine())!= null){
				String [] r = line.split("{\"type\":\"Point\",\"coordinates\":[");
				String coord = r[1].substring(0, 15);
				String [] r2 = coord.split(",");
				l.add(new double[]{Double.parseDouble(r2[0]), Double.parseDouble(r2[1])});
			}br.close();
			
		}
		genHeatmap2(l, "heatmapTwitter.html");
	}
}
