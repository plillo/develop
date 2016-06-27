package it.hash.osgi.business.promotion;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import it.hash.osgi.utils.StringUtils;
import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.ObjectId;

public class Promotion implements Comparable<Promotion> {

	@ObjectId
	@Id
	private String _id;
	private String uuid;
	private String type;
	private Long fromDate;
	private Long toDate;
	private Boolean activate;
	private String pictureUuid;

	// Business
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

	public Long getFromDate() {
		return fromDate;
	}

	public void setFromDate(Long fromDate) {
		this.fromDate = fromDate;
	}

	public Long getToDate() {
		return toDate;
	}

	public void setToDate(Long endTime) {
		this.toDate = endTime;
	}

	public Boolean getActivate() {
		return activate;
	}

	public void setActivate(Boolean activate) {
		this.activate = activate;
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
		String attribute = null;
		Set<String> entry = map.keySet();
		for (Object elem : entry) {
			attribute = (String) elem;
			if (attribute.equals("_id")) {
				Object o = map.get(attribute).toString();
				this.set_id(o.toString());
			} else {

				switch (attribute) {
				case "uuid":
					this.setUuid((String) map.get(attribute));
					break;
				case "type":
					this.setType((String) map.get(attribute));
					break;

				case "fromDate":
					Object fromDate = map.get(attribute);
					if (fromDate instanceof Double)
						this.setFromDate(((Double) map.get(attribute)).longValue());
					else
						this.setFromDate(((Long) map.get(attribute)));

					break;

				case "toDate":

					Object toDate = map.get(attribute);
					if (toDate instanceof Double)
						this.setToDate(((Double) map.get(attribute)).longValue());
					else
						this.setToDate(((Long) map.get(attribute)));

					break;
				case "activate":
					this.setActivate((Boolean) map.get(attribute));
					break;
				case "pictureUuid":
					this.setPictureUuid((String) map.get(attribute));
					break;
				case "businessUuid":
					this.setBusinessUuid((String) map.get(attribute));
					break;
				case "businessName":
					this.setBusinessName((String) map.get(attribute));
					break;

				case "businessPIva":
					this.setBusinessPIva((String) map.get(attribute));
					break;

				case "businessFiscalCode":
					this.setBusinessFiscalCode((String) map.get(attribute));
					break;

				case "businessAddress":
					this.setBusinessAddress((String) map.get(attribute));
					break;

				case "businessCity":
					this.setBusinessCity((String) map.get(attribute));
					break;

				case "businessCap":
					this.setBusinessCap((String) map.get(attribute));
					break;

				case "businessNation":
					this.setBusinessUuid((String) map.get(attribute));
					break;

				}
			}
		}
	}

	@Override
	public String toString() {
		return "Promotion [_id=" + _id + ", uuid=" + uuid + ", type=" + type + ", fromDate=" + fromDate + ", toDate="
				+ toDate + ", activate=" + activate + ", pictureUuid=" + pictureUuid + ", businessUuid=" + businessUuid
				+ ", businessName=" + businessName + ", businessPIva=" + businessPIva + ", businessFiscalCode="
				+ businessFiscalCode + ", businessAddress=" + businessAddress + ", businessCity=" + businessCity
				+ ", businessCap=" + businessCap + ", businessNation=" + businessNation + "]";
	}

	public Map<String, Object> toMap() {
		// TODO continuare :-(
		Map<String, Object> map = new HashMap<String, Object>();

		if (!StringUtils.isEON(this.get_id()))
			map.put("_id", this.get_id());
		if (!StringUtils.isEON(this.getUuid()))
			map.put("uuid", this.getUuid());
		if (!StringUtils.isEON(this.getType()))
			map.put("type", this.getType());
		if (this.getFromDate() != null)
			map.put("fromDate", this.getFromDate());
		if (this.toDate != null)
			map.put("toDate", this.getToDate());
		if (this.activate != null)
			map.put("activate", this.activate);
		if (!StringUtils.isEON(this.getPictureUuid()))
			map.put("uuidPictureUuid", this.getPictureUuid());

		// Business
		if (!StringUtils.isEON(this.getBusinessUuid()))
			map.put("businessUuid", this.getBusinessUuid());
		if (!StringUtils.isEON(this.getBusinessName()))
			map.put("businessName", this.getBusinessName());
		if (!StringUtils.isEON(this.getBusinessPIva()))
			map.put("businessPIva", this.getBusinessPIva());
		if (!StringUtils.isEON(this.getBusinessFiscalCode()))
			map.put("businessFiscalCode", this.getBusinessFiscalCode());
		if (!StringUtils.isEON(this.getBusinessAddress()))
			map.put("businessAddress", this.getBusinessAddress());
		if (!StringUtils.isEON(this.getBusinessCity()))
			map.put("businessCity", this.getBusinessCity());
		if (!StringUtils.isEON(this.getBusinessCap()))
			map.put("businessCap", this.getBusinessCap());
		if (!StringUtils.isEON(this.getBusinessNation()))
			map.put("businessNation", this.getBusinessNation());

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
