package it.hash.osgi.business.promotion;

import java.util.HashMap;
import java.util.Map;

import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.ObjectId;

public class Promotion implements Comparable<Promotion> {

	@ObjectId
	@Id
	private String _id;
	private String uuid;
	private String type;
	private Long startTime;
	private Long endTime;
	private String pictureUuid;
	// 
	private String businessUuid;
	private String businessName;
	private String businessPIva;
	private String businessFiscalCode;
	private String businessAddress;
	private String businessCity;
	private String businessCap;
	private String businessNation;

	public String getBusinessUuid() {
		return businessUuid;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public void setBusinessUuid(String businessUuid) {
		this.businessUuid = businessUuid;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getBusinessPIva() {
		return businessPIva;
	}

	public void setBusinessPIva(String businessPIva) {
		this.businessPIva = businessPIva;
	}

	public String getBusinessFiscalCode() {
		return businessFiscalCode;
	}

	public void setBusinessFiscalCode(String businessFiscalCode) {
		this.businessFiscalCode = businessFiscalCode;
	}

	public String getBusinessAddress() {
		return businessAddress;
	}

	public void setBusinessAddress(String businessAddress) {
		this.businessAddress = businessAddress;
	}

	public String getBusinessCity() {
		return businessCity;
	}

	public void setBusinessCity(String businessCity) {
		this.businessCity = businessCity;
	}

	public String getBusinessCap() {
		return businessCap;
	}

	public void setBusinessCap(String businessCap) {
		this.businessCap = businessCap;
	}

	public String getBusinessNation() {
		return businessNation;
	}

	public void setBusinessNation(String businessNation) {
		this.businessNation = businessNation;
	}

	public String getPictureUuid() {
		return pictureUuid;
	}

	public void setPictureUuid(String pictureUuid) {
		this.pictureUuid = pictureUuid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	@Override
	public String toString() {
		return "Promotion [uuid=" + uuid + ", type=" + type + ", businessName=" + businessName + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
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
		Promotion other = (Promotion) obj;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}

	public void setByMap(Map<String, Object> map) {
		this.setUuid((String) map.get("uuid"));
		this.setType((String) map.get("type"));
		this.setBusinessName((String) map.get("businessName"));
	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uuid", this.getUuid());
		map.put("type", this.getType());
		map.put("businessName", this.getBusinessName());
		return map;
	}

	@Override
	public int compareTo(Promotion p) {
		// TODO Auto-generated method stub
		if (this.getUuid().equals(p.getUuid()))
			return 0;
		return 1;
	}
}
