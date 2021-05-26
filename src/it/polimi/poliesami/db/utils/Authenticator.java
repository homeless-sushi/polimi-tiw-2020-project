package it.polimi.poliesami.db.utils;

import at.favre.lib.crypto.bcrypt.BCrypt.Hasher;
import at.favre.lib.crypto.bcrypt.BCrypt.Result;
import at.favre.lib.crypto.bcrypt.BCrypt.Verifyer;

public class Authenticator {
	private Hasher hasher;
	private Verifyer verifier;
	
	public Authenticator(Hasher hasher, Verifyer verifier) {
		this.hasher = hasher;
		this.verifier = verifier;
	}
	
	public byte[] hash(int cost, byte[] plainText) {
		return this.hasher.hash(cost, plainText);
	}
	
	public boolean verify(byte[] plainText, byte[] hashed) {
		Result result = this.verifier.verify(plainText, hashed);
		return result.verified;
	}
}
