package it.polimi.db.business;

import java.io.Serializable;

public class CareerBean implements Serializable {
	private int personCode;
	private int id;
	private Role role;
	private String major;
	private UserBean user;
	
	public CareerBean() {}
	public CareerBean(int personCode, int id, Role role, String major) {
		this.personCode = personCode;
		this.id = id;
		this.role = role;
		this.major = major;
	}
	public CareerBean(int personCode, int id, Role role) {
		this.personCode = personCode;
		this.id = id;
		this.role = role;
	}
	
	public int getPersonCode() { return this.personCode; }
	public void setPersonCode(int personCode) { this.personCode = personCode; }
	public int getId() { return this.id; }
	public void setId(int id) { this.id = id; }
	public Role getRole() { return this.role; }
	public void setRole(Role role) { this.role = role; }
	public void setRole(String role) { this.role = Role.fromString(role); }
	public String getMajor() { return this.major; }
	public void setMajor(String major) { this.major = major; }
	public UserBean getUser() { return this.user; }
	public void setUser(UserBean user) { this.user = user; }

	public String getPersonCodeString(){
		return UserBean.personCodeToString(personCode);
	}
	
	public void setPersonCodeString(String personCode){
		this.personCode = UserBean.personCodeToInt(personCode);
	}
}
