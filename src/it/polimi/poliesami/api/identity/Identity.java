package it.polimi.poliesami.api.identity;

import it.polimi.poliesami.db.business.Role;

public class Identity {
	private String personCode;
	private Integer[] ids;
	private String[] roles;

	public Identity(String personCode, Integer[] ids, String[] roles) {
		if(ids.length != roles.length)
			throw new IllegalArgumentException("Different length arrays ids[] and roles[]");
		this.personCode = personCode;
		this.ids = ids;
		this.roles = roles;
	}

	public String getPersonCode() {
		return personCode;
	}

	public boolean hasCareer(int id, Role role) {
		String roleName = role.toString();
		for (int i = 0; i < ids.length; i++) {
			if((ids[i] == id) && (roles[i].equals(roleName)))
				return true;
		}
		return false;
	}
}
