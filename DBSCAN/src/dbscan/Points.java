package dbscan;

import org.apache.commons.math3.ml.clustering.Clusterable;

public class Points implements Clusterable {
	
	double lat; 
	double lon; 
	
	public Points(double la , double lo){
		
		this.lat = la; 
		this.lon = lo;
		
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public double[] getPoint() {
		return new double[]{lat,lon};
	}
}
