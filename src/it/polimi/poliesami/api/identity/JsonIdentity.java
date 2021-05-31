package it.polimi.poliesami.api.identity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;

import it.polimi.poliesami.db.business.CareerBean;
import it.polimi.poliesami.db.business.UserBean;

public class JsonIdentity {
	private String jwt;
	private UserBean user;
	private List<CareerBean> careers;

	@JsonGetter("jwt")
	public String getJwt() { return jwt; }
	public void setJwt(String jwt) { this.jwt = jwt; }

	@JsonGetter("user")
	public UserBean getUser() { return user; }
	public void setUser(UserBean user) { this.user = user; }

	@JsonGetter("careers")
	public List<CareerBean> getCareers() { return careers; }
	public void setCareers(List<CareerBean> careers) { this.careers = careers; }
}
