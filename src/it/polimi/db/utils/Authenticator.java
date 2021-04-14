package it.polimi.db.utils;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Hasher;
import at.favre.lib.crypto.bcrypt.BCrypt.Verifyer;

public class Authenticator {
	private Hasher hasher;
	private Verifyer verifier;
	
	public Authenticator() {
		this.hasher = BCrypt.withDefaults();
		this.verifier = BCrypt.verifyer();
	}
	
	public byte[] hash(int cost, byte[] plainText) {
		return this.hasher.hash(cost, plainText);
	}
	
	public boolean verify(byte[] plainText, byte[] hashed) {
		BCrypt.Result result = this.verifier.verify(plainText, hashed);
		return result.verified;
	}
}
