package it.hash.osgi.content.service;

import net.vz.mongodb.jackson.ObjectId;

public class Content {
	@ObjectId
	private String _id;
	private String _previousId;
	private String name;
	private String lang;
	private String type;
	private byte[] content;
	
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public byte[] getContent() {
		return content;
	}
	public void setContent(byte[] content) {
		this.content = content;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String get_previousId() {
		return _previousId;
	}
	public void set_previousId(String _previousId) {
		this._previousId = _previousId;
	}
}
