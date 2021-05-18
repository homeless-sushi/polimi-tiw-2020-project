package it.polimi.poliesami.business;

import java.io.Serializable;

import it.polimi.db.business.Role;
import it.polimi.db.business.UserBean;

public class IdentityBean implements Serializable {
	private int personCode;
	private boolean allDay;
	private int careerId;
	private Role role;

	public IdentityBean(){}
	public IdentityBean(int personCode, boolean allDay){
		this.personCode = personCode;
		this.allDay = allDay;
	}
	public IdentityBean(int personCode, boolean allDay, int careerId, Role role){
		this.personCode = personCode;
		this.allDay = allDay;
		this.careerId = careerId;
		this.role = role;
	}

	public void setPersonCode(int personCode) { this.personCode = personCode; }
	public int getPersonCode() { return this.personCode; }
	public void setAllDay(boolean allDay) { this.allDay = allDay; }
	public boolean isAllDay() { return this.allDay; }
	public void setCareerId(int careerId) { this.careerId = careerId; }
	public int getCareerId() { return this.careerId; }
	public void setRole(Role role) { this.role = role; }
	public Role getRole() { return this.role; }

	public String getPersonCodeString(){
		return String.format("%08d", this.personCode);
	}
	
	public void setPersonCodeString(String personCode){
		invalid : {
			if(personCode.length() != UserBean.PCODE_LEN){
				break invalid;
			}
			try{
				this.personCode = Integer.parseInt(personCode);
			}catch(NumberFormatException e){
				break invalid;
			}
		}
		throw new IllegalArgumentException();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(this.getClass().getSimpleName() + "(");
		builder.append(personCode);
		if(role != null) {
				builder.append(", " + careerId + ", " + role);
		}
		builder.append(")");
		return builder.toString();
	}
}
