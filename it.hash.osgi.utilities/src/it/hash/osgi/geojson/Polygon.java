package it.hash.osgi.geojson;

import java.util.ArrayList;
import java.util.List;

public class Polygon extends LineString {
	
	public Polygon(){
		super();
		
	}
	@Override
	public List<Coordinates> addCoordinates(Double longitude,Double latitude) {
		if (this.coordinates ==null )
			  this.coordinates=new ArrayList<Coordinates>();
		Coordinates c = new Coordinates();
		c.setLongitude(longitude);
		c.setLatitude(latitude);
		if (!this.coordinates.contains(c))
			this.coordinates.add(c);
		
		return this.coordinates;
	
	}

public int size(){
	return this.coordinates.size();
}
}
