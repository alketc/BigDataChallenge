package datavis2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class DataSetAnalysis {
	
	public static void main (String [] args) throws Exception{
		
		String file = "data/CompaniesIntegratedDataset2.csv";
		String line;
		int counter=0;
		BufferedReader br = new BufferedReader(new FileReader(new File(file)));
		while((line = br.readLine())!= null){
			String [] r = line.split("\t");
			String k = r[3];
			if(k.equals("S")){
				counter++;
			}
			
		}br.close();
		System.out.println(counter);
	}

}
