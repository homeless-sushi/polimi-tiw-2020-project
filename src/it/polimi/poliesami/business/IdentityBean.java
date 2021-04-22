package it.polimi.poliesami.business;

import java.io.Serializable;

import it.polimi.db.business.Role;

public class IdentityBean implements Serializable {
	private String personCode;
	private boolean allDay;
	private int careerId;
	private Role role;

	public IdentityBean(){}
	public IdentityBean(String personCode, boolean allDay){
		this.personCode = personCode;
		this.allDay = allDay;
	}
	public IdentityBean(String personCode, boolean allDay, int careerId, Role role){
		this.personCode = personCode;
		this.allDay = allDay;
		this.careerId = careerId;
		this.role = role;
	}

	public void setPersonCode(String personCode) { this.personCode = personCode; }
	public String getPersonCode() { return this.personCode; }
	public void setAllDay(boolean allDay) { this.allDay = allDay; }
	public boolean isAllDay() { return this.allDay; }
	public void setCareerId(int careerId) { this.careerId = careerId; }
	public int getCareerId() { return this.careerId; }
	public void setRole(Role role) { this.role = role; }
	public Role getRole() { return this.role; }
}
