package it.polimi.db.business;

import java.io.Serializable;

public class CareerBean implements Serializable {
	private String personCode;
	private String id;
	private String role;
	private String major;
	
	public CareerBean() {}
	public CareerBean(String personCode, String id, String role, String major) {
		this.personCode = personCode;
		this.id = id;
		this.role = role;
		this.major = major;
	}
	public CareerBean(String personCode, String id, String role) {
		this.personCode = personCode;
		this.id = id;
		this.role = role;
	}
	
	public String getPersonCode() { return this.personCode; }
	public void setPersonCode(String personCode) { this.personCode = personCode; }
	public String getId() { return this.id;	}
	public void setId(String id) { this.id = id; }
	public String getRole() { return this.role; }
	public void setRole(String role) { this.role = role; }
	public String getMajor() { return this.major; }
	public void setMajor(String major) { this.major = major; }
	
	
}
