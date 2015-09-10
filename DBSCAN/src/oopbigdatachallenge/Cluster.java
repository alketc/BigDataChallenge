package oopbigdatachallenge;

import java.util.ArrayList;

public class Cluster {

	int id = 0;
	
	ArrayList<Company> cluster = null;
	
	public Cluster(int i){
		this.id = i;
	    cluster = new ArrayList<Company>();
	}
		
	
	public String toString(){
		ArrayList<Company> point = new ArrayList<Company>();

		for(int i = 0; i < cluster.size(); i++) {
			System.out.println(cluster.get(i) );
		}
		return ""; 
	}
	
	
	public static Object[] cluster(int id, ArrayList<Company>arr, double dist){

		Cluster c1 = new Cluster(id);

		ArrayList<Company> rem_points =  new ArrayList<Company>();

        c1.cluster.add(arr.get(0));

		for(int i = 1; i < arr.size(); i++){
			if (CompanyMethods.haversineDist(arr.get(0), arr.get(i)) < dist) {
				c1.cluster.add(arr.get(i));

			}
			else rem_points.add(arr.get(i));
		}
		return new Object[]{c1,rem_points};
	}
	
	public static Object[] cluster2(int id, ArrayList<Company>arr, double dist){

		Cluster c1 = new Cluster(id);

		ArrayList<Company> rem_points =  new ArrayList<Company>();
		ArrayList<Company> rem_points2 =  new ArrayList<Company>();
       
		c1.cluster.add(arr.get(0));
        
        int sec_0 = arr.get(0).getAteco();
       // System.out.println("Il settore che considero è {*****************************: "+sec_0);
		
        for(int i = 1; i < arr.size(); i++){
        	
			if (CompanyMethods.haversineDist(arr.get(0), arr.get(i)) < dist) {
				
				
				
				int sec_i =  arr.get(i).getAteco();
				if((sec_0 < 10) && (sec_i < 10)){
						c1.cluster.add(arr.get(i));
				//		System.out.println("inserisco nell cluster azienda di sett "+sec_i);
				}
				else if ((sec_0 >=10 && sec_0 <33) && (sec_i >=10 && sec_i < 33)){
					c1.cluster.add(arr.get(i));
				//	System.out.println("inserisco nell cluster azienda di sett "+sec_i);
				}
				else if((sec_0 >=33 && sec_0 <=99) && (sec_i >=33 && sec_i <= 99)) {
					c1.cluster.add(arr.get(i));
				//	System.out.println("inserisco nell cluster azienda di sett "+sec_i);
				}
			else{
				//System.out.println(sec_0+" "+sec_i);	 
				rem_points.add(arr.get(i));
					//System.out.println("azienda di ateco code diverso  = "+arr.get(i).ateco);
				}
			}
			else rem_points.add(arr.get(i));
		}
       System.out.println("}*****************************************"+rem_points2.size());
		return new Object[]{c1,rem_points};
	}
}
