package it.hash.osgi.geojson;

import java.util.HashMap;
import java.util.Map;

public class Coordinates implements Comparable<Coordinates>{
		protected double lat;
		protected double lng ;
		
		public double getLatitude() {
			return lat;
		}
		
		public void setLatitude(double latitude) {
			this.lat = latitude;
		}
		
		public double getLongitude() {
			return lng;
		}
		
		public void setLongitude(double longitude) {
			this.lng = longitude;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			long temp;
			temp = Double.doubleToLongBits(lat);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			temp = Double.doubleToLongBits(lng);
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
			Coordinates other = (Coordinates) obj;
			if (Double.doubleToLongBits(lat) != Double.doubleToLongBits(other.lat)
					&& (Double.doubleToLongBits(lng) != Double.doubleToLongBits(other.lng)))
				return false;
			return true;
		}
		
		@Override
		public int compareTo(Coordinates coordinate) {
			if ((this.lat==coordinate.getLatitude()) && (this.lng==coordinate.getLongitude()))
					return 0;
			
			return 1;
		}

		public Object toMap() {
			Map <String, Object> map = new HashMap<String, Object>();
			map.put("lat", this.getLatitude());
			map.put("lng", this.getLongitude());
			
			return map;
		}
		
		public Double[] toArray(){
			return new Double[]{ this.lat, this.lng};
		}
		
		@Override
		public String toString(){
			return "["+this.getLatitude()+","+getLongitude()+"]";
		}

}
