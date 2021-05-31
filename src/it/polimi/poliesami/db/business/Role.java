package it.polimi.poliesami.db.business;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {

	PROFESSOR ("professor"),
	STUDENT ("student");

	private String role;

	private Role(String role){
		this.role = role;
	}

	@Override
	@JsonValue
	public String toString(){ return this.role; }

	public static Role fromString(String role){
		switch(role.toLowerCase()) {
			case "professor":
				return Role.PROFESSOR;
			case "student":
				return Role.STUDENT;
			default:
				return null;
		}
	}

}
