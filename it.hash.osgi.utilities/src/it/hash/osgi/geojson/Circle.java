package it.hash.osgi.geojson;

public class Circle extends Geometry implements Comparable<Circle>{
	
	protected Point center;
	protected double radius;

	public Circle(double longitude, double latitude, double radius){
		super();
		center= new Point(longitude,latitude);
		this.radius=radius;
	}

	public Point getCenter() {
		return center;
	}

	public void setCenter(Point center) {
		this.center = center;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((center == null) ? 0 : center.hashCode());
		long temp;
		temp = Double.doubleToLongBits(radius);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		Circle other = (Circle) obj;
		if (center == null) {
			if (other.center != null)
				return false;
		} else if (!center.equals(other.center) && Double.doubleToLongBits(radius) != Double.doubleToLongBits(other.radius))
			return false;
	
		return true;
	}

	@Override
	public int compareTo(Circle circle) {
	    int ret= this.center.compareTo(circle.getCenter());
	    if ((ret==0) && (this.radius==circle.radius))
	    	return 0;
	    
		return 1;
	}
}
