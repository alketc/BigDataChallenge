package oopbigdatachallenge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main2CompanyData {
	
	public static void main(String [] args) throws Exception{
		
		String data = "data/CompaniesIntegratedDataset2.csv";
		String citta = "Palermo";
		Map<String, List<Company>>m = new HashMap<String, List<Company>>();
	
		CompanyMethods.loadCityData(data, m, citta);
		//System.out.println(m.get("Venezia").size());
		CompanyMethods.printCityKML(m, citta, citta+"Companies.kml");
		
//		ArrayList<Company>l = new ArrayList<Company>();
//		CompanyMethods.loadComData(data, l);
//		CompanyMethods.printKML(l, "CompaniesKML.kml");
	}

}
