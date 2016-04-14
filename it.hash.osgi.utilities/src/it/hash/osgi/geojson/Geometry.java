package it.hash.osgi.geojson;

public abstract class Geometry {
	protected String type;
	
	public Geometry(){
		type=this.getClass().getSimpleName();
	}

	public String getType() {
		return type;
	}

	public void setType(String type){
		this.type=type;
	}

	@Override
	public abstract int hashCode() ;

	@Override
	public abstract boolean equals(Object obj) ;
}
