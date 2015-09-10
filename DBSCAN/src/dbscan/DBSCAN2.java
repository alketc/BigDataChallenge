package dbscan;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.clustering.Cluster;

import org.gps.utils.LatLonPoint;
import org.gps.utils.LatLonUtils;

import oopbigdatachallenge.Company;



public class DBSCAN2 {
	
	public static void main(String[] args) throws Exception {
		
		// parameter of clustering 
		int min_dist_of_clust =  100000; 
		int min_nr_comp_per_clust = 5;
		
		// points
		List<Points> points = new ArrayList<Points>();
		// add points in 5,5
		
		for(int i=0; i<10;i++)
			points.add(new Points(5+Math.random(),5+Math.random()));
		
		// add points in 15,15
		for(int i=0; i<10;i++)
			points.add(new Points(15+Math.random(),15+Math.random()));
		System.out.println(points.size());
		
		// declare an object dbscan that will precluster the points 
		DBSCANClusterer<Points> dbscan = new DBSCANClusterer<Points>(min_dist_of_clust, min_nr_comp_per_clust,new DistanceMeasure(){
			
			public double compute(double[] latlon1, double[] latlon2) {
				LatLonPoint p1 = new LatLonPoint(latlon1[0],latlon1[1]);
				LatLonPoint p2 = new LatLonPoint(latlon2[0],latlon2[1]);
				System.out.println("dist = "+LatLonUtils.getHaversineDistance(p1, p2));
				return LatLonUtils.getHaversineDistance(p1, p2);
			}
		});
		// apply the method cluster to the points
	    List<Cluster<Points>> cluster = dbscan.cluster(points);
	    
	    // Lets make a data structure that will associate an id to a cluster of points
	    Map<Integer, List<Points>> m = new TreeMap<Integer, List<Points>>();
		
		int counter = 0; 
		
		for(Cluster<Points> c: cluster){

			List<Points> list_ci = new ArrayList<Points>();

			for(int i = 0; i < c.getPoints().size(); i++){
				
				list_ci.add(c.getPoints().get(i));
			}
			
			m.put(counter, list_ci);
			counter++;
			
		}
        // print out the points of the two clusters 
		for(Integer i : m.keySet()){
			for(int j = 0; j < m.get(i).size(); j++){
				System.out.println("Cluster id = "+i+" - - -|> Points j ="+j+" , "+m.get(i).get(j).getLat()+", "+m.get(i).get(j).getLon());
			
			}
		}

	}	
}