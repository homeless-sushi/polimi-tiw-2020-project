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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import it.polimi.db.business.Role;
import it.polimi.poliesami.business.IdentityBean;

public class AppAuthenticator {
	private static final String IDBEAN_ATTRNAME = "identity";
	private static final String AUTHTOKEN_COOKIENAME = "authToken";
	private static final Logger logger = Logger.getLogger(AppAuthenticator.class.getName());

	private Algorithm signingAlg;
	private JWTVerifier verifier; 
	
	public AppAuthenticator(Algorithm signingAlg) {
		this.signingAlg = signingAlg;
		this.verifier = JWT.require(this.signingAlg)
			.acceptLeeway(5)
			.build();
	}
	
	public void setClientIdentity(HttpServletRequest request, HttpServletResponse response, IdentityBean identity){ 
		if(identity.isAllDay()) {
			setJWTCookie(request, response, identity);
		}
		request.getSession().setAttribute(IDBEAN_ATTRNAME, identity);
		logger.log(Level.FINE, "{0}: Set identity {1}", new Object[]{request.getRemoteHost(), identity});
	}
	
	public IdentityBean getClientIdentity(HttpServletRequest request) {
		// Get current session identity
		{
			HttpSession session = request.getSession(false);
			if(session != null) {
				IdentityBean identity = (IdentityBean) session.getAttribute(IDBEAN_ATTRNAME);
				if(identity != null) {
					logger.log(Level.FINER, "{0}: Get session identity {1}", new Object[]{request.getRemoteHost(), identity});
					return identity;
				}
			}
		}
		
		// Get and restore jwt session identity
		{
			String jwtEncoded = new CookieMap(request.getCookies()).get(AUTHTOKEN_COOKIENAME);
			if(jwtEncoded != null) {
				try {
					DecodedJWT jwtDecoded = verifier.verify(jwtEncoded);
					String personCode = jwtDecoded.getSubject();
					Claim allDay = jwtDecoded.getClaim("allDay");
					Claim careerId = jwtDecoded.getClaim("careerId");
					Claim role = jwtDecoded.getClaim("role");
					
					IdentityBean identity = new IdentityBean();
					identity.setPersonCode(personCode);
					identity.setAllDay(allDay.asBoolean());
					if(!role.isNull()){
						identity.setCareerId(careerId.asInt());
						identity.setRole(Role.fromString(role.asString()));
					}

					request.getSession().setAttribute(IDBEAN_ATTRNAME, identity);
					logger.log(Level.FINER, "{0}: Get jwt identity {1}", new Object[]{request.getRemoteHost(), personCode});
					return identity;
				} catch (JWTVerificationException invalidToken) {
						logger.log(Level.FINER, "{0}: Invalid jwt", request.getRemoteHost());
				}
			}
		}
		
		// Not authenticated
		return null;
	}
	
	private void setJWTCookie(HttpServletRequest request, HttpServletResponse response, IdentityBean identity) {
		Instant tomorrow = Instant.now()
			.plus(1, ChronoUnit.DAYS)
			.truncatedTo(ChronoUnit.DAYS);
		
		JWTCreator.Builder builder = JWT.create()
			.withIssuer("it.polimi.poliesami")
			.withSubject(identity.getPersonCode())
			.withAudience("it.polimi.poliesami")
			.withExpiresAt(Date.from(tomorrow))
			.withIssuedAt(new Date())
			.withClaim("allDay", identity.isAllDay());
		if(identity.getRole() != null){
			builder.withClaim("careerId", identity.getCareerId());
			builder.withClaim("role", identity.getRole().toString());
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
}
