package rserve;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.rosuda.REngine.Rserve.RConnection;

public class Rserve {
	
	public static void main (String [] args) throws Exception{
		
		String file = "C:\\Users\\Alket\\Documents\\boxplotdataH.csv";
		
		Map<String, List<double[]>>m = new HashMap<String, List<double[]>>();
		Map<String, List<Double>>m2 = new HashMap<String, List<Double>>();
		
		getData(file, m2);
		List<String>names = new ArrayList<String>();
		int counter = 0;
		for(String s : m2.keySet()){
			names.add(s);
					}
		List<double[]>li = new ArrayList<double[]>();
		
		for(String s : m2.keySet()){
			
			double [] di = new double[ m2.get(s).size()];
			
			for(int i = 0; i < m2.get(s).size(); i++){
				di[i]=  m2.get(s).get(i);
			}
			li.add(di);
			m.put(s, li);
		
		}

		for(int i = 0; i  < li.size(); i++){
			for(int j = 0; j < li.get(i).length; j++){
			  System.out.println(i+"--> "+li.get(i)[j]);
			}
		}
		drawBoxplot(li, names, "cities", "entropy values", "C:/Users/Alket/luna/DBSCAN/images/boxplot.pdf", "");
	}
	public static void getData(String file, Map<String, List<Double>>m2)throws Exception{
		BufferedReader br = new BufferedReader(new FileReader(new File(file)));
		br.readLine();
		String line; 
		while((line = br.readLine())!= null){
			String [] r = line.split(",");
			String city = r[0];
			double val = Double.parseDouble(r[2]);
			List<Double>li = m2.get(city);
			if(li == null){
				li = new ArrayList<Double>();
				m2.put(city, li);
				
			}
			else li.add(val);
			
			
		}br.close();
		
	}
	private static RConnection c = null;
	public static void drawBoxplot(List<double []> y, List<String> names, String xlab, String ylab, String file, String opts) {
		try {
			file = file.replaceAll("_", "-");
            c = new RConnection();// make a new local connection on default port (6311)
            
            for(int i=0; i<y.size();i++)
            	c.assign("y"+i, y.get(i));
            
                      
            String end = opts==null || opts.length()==0 ? ";" : " + "+opts+";";
            String code = 
            		   "library(ggplot2);"
            		 + "library(reshape2);";
            
            for(int i=0;i<y.size();i++) {
            		code += "yy"+i+" <- data.frame(y"+i+");";
            		code += "names(yy"+i+") <- c('"+names.get(i)+"');";
            		code += "yy"+i+" <- melt(yy"+i+",id.vars=c());";
            }
            
            StringBuffer sby= new StringBuffer();
            for(int i=0; i<y.size();i++)
            	sby.append(",yy"+i);
            
            	     code += "z <- rbind("+sby.substring(1)+");"   
            	     	  + "ggplot(z,aes(x=factor(variable),y=value)) + geom_boxplot() + theme_bw(base_size = "+10+") +xlab('"+xlab+"') + ylab('"+ylab+"')"+end
            	          + "ggsave('"+file+"',width=10);"
            	          + "dev.off();";
            
            System.out.println(code.replaceAll(";", ";\n"));
            c.eval(code);
            c.close();
             Desktop.getDesktop().open(new File(file));
        } catch (Exception e) {
        	
        	if(e.getMessage().startsWith("Cannot connect")) {
             	System.err.println("You must launch the following code in R");
             	System.err.println("library(Rserve)");
             	System.err.println("Rserve()");
            }
            else e.printStackTrace();
        	c.close();
        }      
	}

}
