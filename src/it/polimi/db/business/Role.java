package it.polimi.db.business;

public enum Role {

	PROFESSOR ("professor"), 
	STUDENT ("student");

	private String role;

	private Role(String role){ 
		this.role = role; 
	}

	@Override
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
