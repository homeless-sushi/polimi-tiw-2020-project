package it.polimi.poliesami.business;

import java.io.Serializable;

import it.polimi.db.business.CareerBean;
import it.polimi.db.business.UserBean;

public class IdentityBean implements Serializable {
	private UserBean user;
	private CareerBean career;
	private boolean allDay;

	public void setUser(UserBean user) {
		if(user == null)
			throw new NullPointerException("Invalid null user in identity");
		this.user = user;
	}
	public UserBean getUser() { return user; }
	public void setCareer(CareerBean career) { this.career = career; }
	public CareerBean getCareer() { return career; }
	public void setAllDay(boolean allDay) { this.allDay = allDay; }
	public boolean isAllDay() { return allDay; }
	public int getPersonCode() { return user.getPersonCode(); }
	public String getPersonCodeString() { return user.getPersonCodeString(); }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("(person code: ");
		builder.append(getPersonCodeString());
		if(career != null) {
			builder.append(", career id: " + career.getId() + ", role: " + career.getRole());
		}
		builder.append(")");
		return builder.toString();
	}
}
