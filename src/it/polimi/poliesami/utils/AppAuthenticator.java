package it.polimi.poliesami.utils;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import it.polimi.db.business.CareerBean;
import it.polimi.db.business.Role;
import it.polimi.db.business.UserBean;
import it.polimi.db.dao.CareerDAO;
import it.polimi.db.dao.UserDAO;
import it.polimi.db.utils.Authenticator;
import it.polimi.poliesami.business.IdentityBean;

public class AppAuthenticator {
	private static final String IDBEAN_ATTRNAME = "identity";
	private static final String AUTHTOKEN_COOKIENAME = "authToken";
	private static final Logger logger = Logger.getLogger(AppAuthenticator.class.getName());

	private UserDAO userDAO;
	private CareerDAO careerDAO;
	private Authenticator userAuthenticator;
	private Algorithm signingAlg;
	private JWTVerifier verifier;

	public AppAuthenticator(UserDAO userDAO, CareerDAO careerDAO, Authenticator authenticator, Algorithm signingAlg) {
		this.userDAO = userDAO;
		this.careerDAO = careerDAO;
		this.userAuthenticator = authenticator;
		this.signingAlg = signingAlg;
		this.verifier = JWT.require(this.signingAlg)
			.acceptLeeway(5)
			.build();
	}

	public boolean setClientIdentity(HttpServletRequest request, HttpServletResponse response, int personCode, byte[] password, boolean allDayLogin) {
		try {
			IdentityBean identity = createIdentity(personCode);
			identity.setAllDay(allDayLogin);
			authenticate(identity, password);

			if(identity.isAllDay())
				setJWTCookie(request, response, identity);
			request.getSession().setAttribute(IDBEAN_ATTRNAME, identity);

			logger.log(Level.FINE, "{0}: Set identity {1}", new Object[]{request.getRemoteHost(), identity});
			return true;
		} catch (AuthenticationException e) {
			logger.log(Level.FINE, "{0}: {1}", new Object[]{request.getRemoteHost(), e.getMessage()});
		}
		return false;
	}

	public boolean setClientCareer(HttpServletRequest request, HttpServletResponse response, int careerId, Role role) {
		try {
			IdentityBean identity = getClientIdentity(request);
			setCareer(identity, careerId, role);

			if(identity.isAllDay())
				setJWTCookie(request, response, identity);

			logger.log(Level.FINE, "{0}: Set identity {1}", new Object[]{request.getRemoteHost(), identity});
			return true;
		} catch (AuthenticationException e) {
			logger.log(Level.FINE, "{0}: {1}", new Object[]{request.getRemoteHost(), e.getMessage()});
		}
		return false;
	}

	public IdentityBean getClientIdentity(HttpServletRequest request) {
		IdentityBean identity;
		
		// Get current session identity
		identity = getSessionIdentity(request);
		if(identity != null)
			return identity;
		
		// Get and restore jwt session identity
		identity = getJWTIdentity(request);
		if(identity != null)
			return identity;
		
		// Not authenticated
		return null;
	}

	public void deleteClientIdentity(HttpServletRequest request, HttpServletResponse response) {
		// Session logout
		HttpSession session = request.getSession(false);
		if(session != null) {
			session.removeAttribute(IDBEAN_ATTRNAME);
			logger.log(Level.FINE, "{0}: Delete session identity", request.getRemoteHost());
		}

		// jwt logout
		String jwtEncoded = new CookieMap(request.getCookies()).get(AUTHTOKEN_COOKIENAME);
		if(jwtEncoded != null) {
			Cookie jwtCookie = new Cookie(AUTHTOKEN_COOKIENAME, "");
			jwtCookie.setPath(request.getContextPath());
			jwtCookie.setMaxAge(0);
			response.addCookie(jwtCookie);
			logger.log(Level.FINE, "{0}: Delete jwt identity", request.getRemoteHost());
		}
	}

	private IdentityBean createIdentity(int personCode) throws AuthenticationException {
		UserBean user = userDAO.getUserByPersonCode(personCode);
		if(user == null)
			throw new AuthenticationException("No such user " + personCode);
		
		IdentityBean identity = new IdentityBean();
		identity.setUser(user);
		return identity;
	}

	private void authenticate(IdentityBean identity, byte[] password) throws AuthenticationException {
		int personCode = identity.getPersonCode();
		byte[] hashedPsw = userDAO.getUserHashedPsw(personCode);
		
		if(!userAuthenticator.verify(password, hashedPsw))
			throw new AuthenticationException("Wrong password for user " + personCode);
	}

	private void setCareer(IdentityBean identity, int careerId, Role role) throws AuthenticationException {
		int personCode = identity.getPersonCode();
		CareerBean career = careerDAO.getUserCareer(personCode, careerId, role);
		if(career == null)
			throw new AuthenticationException("Invalid career (" + careerId + ", " + role + ") for user " + personCode);
		
		identity.setCareer(career);
	}

	private void setJWTCookie(HttpServletRequest request, HttpServletResponse response, IdentityBean identity) {
		Instant tomorrow = Instant.now()
			.plus(1, ChronoUnit.DAYS)
			.truncatedTo(ChronoUnit.DAYS);
		
		JWTCreator.Builder builder = JWT.create()
			.withIssuer("it.polimi.poliesami")
			.withSubject(identity.getPersonCodeString())
			.withAudience("it.polimi.poliesami")
			.withExpiresAt(Date.from(tomorrow))
			.withIssuedAt(new Date());
		CareerBean career = identity.getCareer();
		if(career != null){
			builder.withClaim("careerId", career.getId());
			builder.withClaim("role", career.getRole().toString());
		}
		String jwtEncoded = builder.sign(this.signingAlg);			

		// Manually build Cookie header
		response.addHeader("Set-Cookie",
			AUTHTOKEN_COOKIENAME + "=" + jwtEncoded + "; " +
			"Expires=" + ZonedDateTime.ofInstant(tomorrow, ZoneOffset.UTC).format(DateTimeFormatter.RFC_1123_DATE_TIME) + "; " +
			"Path=" + request.getContextPath()
		);
		logger.log(Level.FINE, "{0}: Set jwt identity {1}", new Object[]{request.getRemoteHost(), identity});
	}

	private IdentityBean getSessionIdentity(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if(session != null) {
			IdentityBean identity = (IdentityBean) session.getAttribute(IDBEAN_ATTRNAME);
			if(identity != null) {
				logger.log(Level.FINER, "{0}: Get session identity {1}", new Object[]{request.getRemoteHost(), identity});
				return identity;
			}
		}
		return null;
	}

	private IdentityBean getJWTIdentity(HttpServletRequest request) {
		String jwtEncoded = new CookieMap(request.getCookies()).get(AUTHTOKEN_COOKIENAME);
		if(jwtEncoded != null) {
			try {
				DecodedJWT jwtDecoded = verifier.verify(jwtEncoded);
				String personCode = jwtDecoded.getSubject();
				Claim careerId = jwtDecoded.getClaim("careerId");
				Claim role = jwtDecoded.getClaim("role");
				
				IdentityBean identity = createIdentity(UserBean.personCodeToInt(personCode));
				identity.setAllDay(true);

				if(!role.isNull())
					setCareer(identity, careerId.asInt(), Role.fromString(role.asString()));

				request.getSession().setAttribute(IDBEAN_ATTRNAME, identity);
				logger.log(Level.FINER, "{0}: Get jwt identity {1}", new Object[]{request.getRemoteHost(), identity});
				return identity;
			} catch (JWTVerificationException | AuthenticationException invalidToken) {
				logger.log(Level.FINER, "{0}: Invalid jwt {1}", new Object[]{request.getRemoteHost(), invalidToken.getMessage()});
			}
		}
		return null;
	}
}

class AuthenticationException extends Exception {
	public AuthenticationException () {
		super();
	}

	public AuthenticationException (String message) {
		super(message);
	}
}
