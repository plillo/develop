package it.hash.osgi.geojson;

import java.util.ArrayList;
import java.util.List;

public  class LineString extends Geometry {
	
	protected List<Coordinates> coordinates;
	
	public LineString(){
		super();
		
	}
	public List<Coordinates> addCoordinates(Double longitude,Double latitude) {
		if (this.coordinates ==null )
			  this.coordinates=new ArrayList<Coordinates>();
		Coordinates c = new Coordinates();
		c.setLongitude(longitude);
		c.setLatitude(latitude);
		if (this.coordinates.size()<2 && (!this.coordinates.contains(c)))
			this.coordinates.add(c);
		
		return this.coordinates;
	
	}

public List<Coordinates> removeCoordinates(Double longitude,Double latitude)
{
	Coordinates c = new Coordinates();
	c.setLongitude(longitude);
	c.setLatitude(latitude);
	
	if (this.coordinates.contains(c))
		this.coordinates.remove(c);

	return this.coordinates;
}
	
public List<Coordinates> getCoordinates() {
	return this.coordinates;
}
public  String toGeoJson(){
	
	String s= "{ type : \""+this.getType()+"\","+ "coordinates: [";
   for(int i=0;i<this.coordinates.size()-1;i++){
         s+="["+this.coordinates.get(i).getLongitude()
			+","+coordinates.get(i).getLatitude()+"],";
			
}
   
   s+="["+this.coordinates.get(this.coordinates.size()-1).getLongitude()
		+","+coordinates.get(this.coordinates.size()-1).getLatitude()+"]]}";
	return s;	

}

	@Override 
	public String toString(){
		String s=null;
		int i=0;
		if (this.coordinates.size()>1){
	
		for( i=0;i< this.coordinates.size()-1;i++)
		   s+="["+this.coordinates.get(i).getLatitude()+","+this.coordinates.get(i).getLongitude()+"],";
	}
		s+="["+this.coordinates.get(i).getLatitude()+","+this.coordinates.get(i).getLongitude()+"]";
		
		
		return s;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((coordinates == null) ? 0 : coordinates.hashCode());
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
		LineString other = (LineString) obj;
		if (coordinates == null) 
			if (other.coordinates != null)
				return false;
		
		 if (!coordinates.equals(other.coordinates))
			return false;
		return true;
	}

}
