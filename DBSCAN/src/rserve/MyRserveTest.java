package rserve;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.rosuda.REngine.Rserve.RConnection;

public class MyRserveTest {

	private static RConnection c = null;
	
public static void main(String[] args) throws Exception {
		
		drawBar(new String[]{"a","b","c"},new double[]{5,6,7},"x","y","C:/Users/Alket/luna/Rserve/images/test1.pdf",null);
//	Map<String, Map<Double, String>>m = new HashMap<String, Map<Double, String>>();
//	load(m, "C:/Users/Alket/Documents/boxplotdata/H");
//	print(m, "C:/Users/Alket/Documents/boxplotdataH.csv");
	//System.out.println(m);
	
		//drawBoxplot("C:/Users/Alket/luna/DBSCAN/images/boxplot.pdf","boxplotdataH.csv", null);

}
public static void print(Map<String, Map<Double, String>>l, String file) throws Exception{
	PrintWriter out = new PrintWriter(new FileWriter(new File(file)));
	out.println("citta,city,entropy");
	for(String s : l.keySet()){
		for(Double st: l.get(s).keySet()){
		   out.println(s+","+l.get(s).get(st)+","+st);
		}
	}
	out.close();
}

public static void drawBar(String[] x, double [] y, String xlab, String ylab, String file, String opts) {
	try {
        	
        c = new RConnection();// make a new local connection on default port (6311)
        
        c.assign("x", x);
        c.assign("y", y);
        
        String end = opts==null || opts.length()==0 ? ";" : " + "+opts+";";
        String code = 
        		   "library(ggplot2);"
        	     + "z <- data.frame(x,y);"
        	     + "pdf('"+file+"');"
        	     + "ggplot(z,aes(x=x,y=y)) + geom_bar(stat='identity') + theme_bw() +xlab('"+xlab+"') + ylab('"+ylab+"')"+end
        	  //   + "ggsave('"+file+"');"
        	     + "dev.off();";
        
        System.out.println(code);
        c.eval(code);
        c.close();
        //Desktop.getDesktop().open(new File(file));
    } catch (Exception e) {
    	//c.close();
    	if(e.getMessage().startsWith("Cannot connect")) {
         	System.err.println("You must launch the following code in R");
         	System.err.println("library(Rserve)");
         	System.err.println("Rserve()");
        }
        else e.printStackTrace();
    }      
}


public static void drawBoxplot( String fileout, String filein, String opts) {
	try {
        	
        c = new RConnection();// make a new local connection on default port (6311)
        
    //   c.assign("m", m);
//        c.assign("y", y);
          c.createFile(filein);
         System.out.println( c.createFile(filein).toString());
        String end = opts==null || opts.length()==0 ? ";" : " + "+opts+";";
        String code = 
        		   "library(ggplot2);\n"
        		// + "pdf('MyBoxplot.pdf');\n"
//        	     + "files_full <- list.files('"+filein+"', full.names=TRUE);\n"
          	     + "dat <- read.csv(\""+filein+"\", sep = \",\", TRUE);\n"
//                 + " for (i in 1:7) {;\n "
//          	     + " dat <- rbind(dat, read.csv(files_full[i]));\n"
//          	     + " };\n"
        	     + "boxplot(dat$entropy~dat$city,data=dat, main='\"Entropy Data\"' ,\n" 
                 + "xlab=\"cities \", ylab=\"Entropy val.dist.\")\n"
                 + "ggsave(\""+fileout+"\");\n"
        	     + "dev.off();\n";
        
         System.out.println(code);
         
        c.eval(code);
        c.close();
        Desktop.getDesktop().open(new File(fileout));
    } catch (Exception e) {
    	c.close();
    	if(e.getMessage().startsWith("Cannot connect")) {
         	System.err.println("You must launch the following code in R");
         	System.err.println("library(Rserve)");
         	System.err.println("Rserve()");
        }
        else e.printStackTrace();
    }      
}

public static void load(Map<String, Map< Double, String>>m , String file) throws Exception{
	
	File f = new File(file);
	File [] folder = f.listFiles();
	
	for(int i = 0; i < folder.length; i++){
		BufferedReader br = new BufferedReader(new FileReader(folder[i]));
		String line; 
		String city = folder[i].getName(); 
		String cityname = city.substring(0, folder[i].getName().toString().indexOf("."));
		System.out.println(city);
		Map<Double, String>mi =m.get(city); 
		if(mi == null ){
			mi = new HashMap<Double, String>();
		}
		br.readLine();
		
		while((line = br.readLine())!= null){
			String [] r = line.split(",");
			double ent = Double.parseDouble(r[2]);
			String cityi = r[1];
			mi.put(ent,  cityi);
			
		}br.close();
		m.put(cityname, mi);
	}
}
}
