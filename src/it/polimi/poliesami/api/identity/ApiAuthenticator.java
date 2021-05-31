package it.polimi.poliesami.api.identity;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import it.polimi.poliesami.db.dao.UserDAO;
import it.polimi.poliesami.db.dao.CareerDAO;
import it.polimi.poliesami.db.utils.Authenticator;
import it.polimi.poliesami.db.business.CareerBean;
import it.polimi.poliesami.db.business.Role;
import it.polimi.poliesami.db.business.UserBean;

public class ApiAuthenticator {
	private UserDAO userDAO;
	private CareerDAO careerDAO;
	private Authenticator userAuthenticator;
	private Algorithm signingAlg;
	private JWTVerifier verifier;

	public ApiAuthenticator(UserDAO userDAO, CareerDAO careerDAO, Authenticator authenticator, Algorithm signingAlg) {
		this.userDAO = userDAO;
		this.careerDAO = careerDAO;
		this.userAuthenticator = authenticator;
		this.signingAlg = signingAlg;
		this.verifier = JWT.require(this.signingAlg)
			.acceptLeeway(5)
			.build();
	}

	public JsonIdentity login(String personCode, String password) throws AuthenticationException {
		UserBean user = getUser(personCode);
		checkPassword(user, password.getBytes());

		JsonIdentity identity = getIdentity(user);
		generateJWT(identity);
		return identity;
	}

	public JsonIdentity loginJWT(String jwt) throws AuthenticationException {
		DecodedJWT decoded = decodeJWT(jwt);
		String personCode = decoded.getSubject();

		UserBean user = getUser(personCode);
		JsonIdentity identity = getIdentity(user);
		identity.setJwt(jwt);
		return identity;
	}

	public Identity readJWT(String jwt) throws AuthenticationException {
		DecodedJWT decoded = decodeJWT(jwt);
		String personCode = decoded.getSubject();

		Claim idsClaim = decoded.getClaim("ids");
		Claim rolesClaim = decoded.getClaim("roles");

		Integer[] ids = idsClaim.asArray(Integer.class);
		String[] roles = rolesClaim.asArray(String.class);
		return new Identity(personCode, ids, roles);
	}

	private UserBean getUser(String personCode) throws AuthenticationException {
		int pcode;
		try {
			pcode = UserBean.personCodeToInt(personCode);
		} catch (IllegalArgumentException e) {
			throw new AuthenticationException("Invalid person code", e);
		}
		UserBean user = userDAO.getUserByPersonCode(pcode);
		if(user == null)
			throw new AuthenticationException("No such user " + personCode);
		return user;
	}

	private void checkPassword(UserBean user, byte[] password) throws AuthenticationException {
		int pcode = user.getPersonCode();
		byte[] hashedPsw = userDAO.getUserHashedPsw(pcode);
		if(!userAuthenticator.verify(password, hashedPsw))
			throw new AuthenticationException("Wrong password for user " + user.getPersonCodeString());
	}

	private JsonIdentity getIdentity(UserBean user) {
		List<CareerBean> careers = careerDAO.getUserCareers(user.getPersonCode());

		JsonIdentity identity = new JsonIdentity();
		identity.setUser(user);
		identity.setCareers(careers);
		return identity;
	}

	private DecodedJWT decodeJWT(String jwt) throws AuthenticationException {
		try {
			return verifier.verify(jwt);
		} catch(JWTVerificationException e) {
			throw new AuthenticationException("Invalid jwt", e);
		}
	}

	private void generateJWT(JsonIdentity identity) {
		String personCode = identity.getUser().getPersonCodeString();
		List<CareerBean> careers = identity.getCareers();
		Integer[] ids = careers.stream().map(CareerBean::getId).toArray(Integer[]::new);
		String[] roles = careers.stream().map(CareerBean::getRole).map(Role::toString).toArray(String[]::new);

		String jwt = JWT.create()
			.withIssuer("it.polimi.poliesami")
			.withAudience("it.polimi.poliesami")
			.withExpiresAt(jwtExpiresAt())
			.withIssuedAt(new Date())
			.withSubject(personCode)
			.withArrayClaim("ids", ids)
			.withArrayClaim("roles", roles)
			.sign(signingAlg);

		identity.setJwt(jwt);
	}

	private Date jwtExpiresAt() {
		Instant expires = Instant.now()
			.plus(1, ChronoUnit.DAYS)
			.truncatedTo(ChronoUnit.DAYS);
		return Date.from(expires);
	}
}
