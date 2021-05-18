package it.polimi.db.business;

import java.io.Serializable;

public class UserBean implements Serializable {
	public static final int PCODE_LEN = 8;

	private int personCode;
	private String email;
	private String name;
	private String surname;
	
	public UserBean() {}
	public UserBean(String email, String name, String surname) {
		this.email = email;
		this.name = name;
		this.surname = surname;
	}
	public UserBean(int personCode, String email, String name, String surname) {
		this.personCode = personCode;
		this.email = email;
		this.name = name;
		this.surname = surname;
	}
	
	public int getPersonCode() { return this.personCode; }
	public void setPersonCode(int personCode) { this.personCode = personCode; }
	public String getEmail () { return this.email; }
	public void setEmail (String email) { this.email = email; }
	public String getName () { return this.name; }
	public void setName (String name ) { this.name = name; }
	public String getSurname () { return this.surname; }
	public void setSurname (String surname) { this.surname = surname; }

	public String getPersonCodeString(){
		return String.format("%08d", this.personCode);
	}
	
	public void setPersonCodeString(String personCode){
		if(personCode.length() != UserBean.PCODE_LEN){
			throw new IllegalArgumentException();
		}
		this.personCode = Integer.parseInt(personCode);
	}
}
