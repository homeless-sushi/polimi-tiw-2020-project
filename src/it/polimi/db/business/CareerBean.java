package it.polimi.db.business;

import java.io.Serializable;

public class CareerBean implements Serializable {
	private String personCode;
	private int id;
	private Role role;
	private String major;
	
	public CareerBean() {}
	public CareerBean(String personCode, int id, Role role, String major) {
		this.personCode = personCode;
		this.id = id;
		this.role = role;
		this.major = major;
	}
	public CareerBean(String personCode, int id, Role role) {
		this.personCode = personCode;
		this.id = id;
		this.role = role;
	}
	
	public String getPersonCode() { return this.personCode; }
	public void setPersonCode(String personCode) { this.personCode = personCode; }
	public int getId() { return this.id; }
	public void setId(int id) { this.id = id; }
	public Role getRole() { return this.role; }
	public void setRole(Role role) { this.role = role; }
	public void setRole(String role) { this.role = Role.fromString(role); }
	public String getMajor() { return this.major; }
	public void setMajor(String major) { this.major = major; }
	
	
}
