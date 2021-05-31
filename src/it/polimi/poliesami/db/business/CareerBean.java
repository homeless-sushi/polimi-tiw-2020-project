package it.polimi.poliesami.db.business;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class CareerBean implements Serializable {
	@JsonIgnore
	private int personCode;
	private int id;
	private Role role;
	@JsonInclude(Include.NON_NULL)
	private String major;
	@JsonInclude(Include.NON_NULL)
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

	@JsonIgnore
	public String getPersonCodeString(){
		return UserBean.personCodeToString(personCode);
	}
	
	public void setPersonCodeString(String personCode){
		this.personCode = UserBean.personCodeToInt(personCode);
	}
}
