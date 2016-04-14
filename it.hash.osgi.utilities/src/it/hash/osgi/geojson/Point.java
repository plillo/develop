package it.hash.osgi.geojson;


public class Point extends Geometry implements Comparable<Point>{
	protected Coordinates coordinates;
	
	public Point(Double latitude, Double longitude){
		super();
		this.coordinates= new Coordinates();
		this.coordinates.setLatitude(latitude);
		this.coordinates.setLongitude(longitude);
	}
	
	public Point(Coordinates coordinates){
		super();
		this.coordinates = coordinates;
	}
	
	public Point(){
		super();
		this.coordinates = null;
	}
	
	public Coordinates getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(Double latitude, Double longitude){
		this.coordinates.setLatitude(latitude);
		this.coordinates.setLongitude(longitude);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.coordinates == null) ? 0 : this.coordinates.hashCode());
		result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point other = (Point) obj;
		if(other.coordinates==null)
			if (coordinates != null)
			  return false;
	
	    if (!coordinates.equals(other.coordinates))
			return false;
		return true;
	}

	public String toGeoJson() {
		String s= "{ type : \"Point\","+ "coordinates: ["+ getCoordinates().getLatitude()+","+getCoordinates().getLongitude()+"]}";
		return s;	
	}

	@Override
	public int compareTo(Point point) {
 
		return this.getCoordinates().compareTo(point.getCoordinates());
	}
	
	
}
