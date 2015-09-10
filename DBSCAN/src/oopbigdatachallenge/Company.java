package oopbigdatachallenge;

import org.apache.commons.math3.ml.clustering.Clusterable;

public class Company implements Comparable<Company>, Clusterable {
	
	String id;
	String name; 
	double lat; 
	double lon; 
	int ateco; 
	String kind;
	String citta; 
	String dim; 
	String age; 
	
	
	public Company(String id, String n, double la, double lo, String k, String citta, int a, String d, String ag){
		this.id = id;
		this.name = n;
		this.lat = la;
		this.lon = lo; 
		this.ateco = a; 
		this.kind = k; 
		this.citta = citta;
		this.dim = d; 
		this.age = ag; 
	}

	

	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String toString(){
		return id+"\t"+name+"\t"+lat+"\t"+lon+"\t"+kind+"\t"+citta+"\t"+ateco+"\t"+dim+"\t"+age;
	}

	public String getCitta() {
		return citta;
	}

	public void setCitta(String citta) {
		this.citta = citta;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public int getAteco() {
		return ateco;
	}

	public void setAteco(int ateco) {
		this.ateco = ateco;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getDim() {
		return dim;
	}

	public void setDim(String dim) {
		this.dim = dim;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	@Override
	public int compareTo(Company c) {
		// TODO Auto-generated method stub
		return citta.compareTo(c.citta);
	}


	public double[] getPoint() {
		return new double[]{lat,lon};
	}
	
}
