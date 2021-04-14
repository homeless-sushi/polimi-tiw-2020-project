package it.polimi.db.business;

import java.io.Serializable;

public class UserBean implements Serializable {
	private String personCode;
	private String email;
	private byte[] hashedPassword;
	private String name;
	private String surname;
	
	public UserBean() {}
	public UserBean(String email, byte[] hashedPassword, String name, String surname) {
		this.email = email;
		this.hashedPassword = hashedPassword;
		this.name = name;
		this.surname = surname;
	}
	public UserBean(String personCode, String email, byte[] hashedPassword, String name, String surname) {
		this.personCode = personCode;
		this.email = email;
		this.hashedPassword = hashedPassword;
		this.name = name;
		this.surname = surname;
	}
	
	public String getPersonCode() { return this.personCode; }
	public void setPersonCode(String personCode) { this.personCode = personCode; }
	public String getEmail () { return this.email; }
	public void setEmail (String email) { this.email = email; }
	public byte[] getHashedPassword () { return this.hashedPassword; }
	public void setHashedPassword (byte[] hashedPassword) { this.hashedPassword = hashedPassword; }
	public String getName () { return this.name; }
	public void setName (String name ) { this.name = name; }
	public String getSurname () { return this.surname; }
	public void setSurname (String surname) { this.surname = surname; }
}
