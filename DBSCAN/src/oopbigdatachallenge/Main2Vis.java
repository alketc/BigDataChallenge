package oopbigdatachallenge;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.ui.RefineryUtilities;

import datavis2.BoxAndWhiskerDemo;

public class Main2Vis {
	static String [] datafiles = {"H", "DBSCAN"};
	
	public static void main (String []args) throws Exception{
		String data = "boxplotdata";
		
		Map<String ,Map<String, Map<String, Double>>>m = new HashMap<String, Map<String,Map<String,Double>>>();
		getData(m, datafiles);
		System.out.println(m.size());
		for(String s : m.keySet()){
			System.out.println(s+"-->  ");
			for(String s2 : m.get(s).keySet()){
				System.out.println(s2+"--> "+m.get(s).get(s2));
			}
		}
		  final BoxAndWhiskerDemo demo = new BoxAndWhiskerDemo("Box-and-Whisker Chart Demo");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
	}
	
	public static void print(List<String>l, String file) throws Exception{
		PrintWriter out = new PrintWriter(new FileWriter(new File(file)));
		for(int i = 0; i < l.size(); i++){
			out.println(l.get(i));
		}
		out.close();
	}

	
	public static void getData (Map<String ,Map<String, Map<String, Double>>>m , String[] folder) throws Exception{
		
		for(int i = 0; i < folder.length; i++){ // per ogni cartella 
        System.out.println("folder i che leggo "+folder[i].toString());
    	File f = new File("boxplotdata/"+folder[i]);
    	File [] files = f.listFiles();
    	String fld = folder[i].toString();
    	System.out.println(fld+", "+fld.length());
    	//String subfld = fld.substring(fld.indexOf("/"));
    	Map<String , Map<String, Double>> mi =  m.get(fld);
    	if(mi == null){
    		mi = new HashMap<String, Map<String, Double>>();
    	}
        for(int j = 0; j < files.length; j++){ // per ogni file nella cartella i esima
        	BufferedReader br = new BufferedReader(new FileReader(files[i]));
        	System.out.println("file i che leggo "+files[i].toString());
        	String line;
        	br.readLine();
        	Map<String , Double>mij = mi.get(files[j].toString());
        	if(mij == null){
        		mij = new HashMap<String, Double>();
        	}
        	while((line = br.readLine())!= null){
        		System.out.println("lego "+line);
        		String [] r = line.split(",");
        		
        		double val = Double.parseDouble(r[1]);
        		mij.put(r[0], val); // cluster id ,val entropia cluster 
        	}br.close();
   
        	mi.put(files[j].getName().substring(0, files[j].getName().indexOf(".")), mij);
        	
        }
       
		m.put(folder[i].toString(), mi);
		}
	}
}
