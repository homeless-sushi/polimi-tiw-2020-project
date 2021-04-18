package it.polimi.poliesami.utils;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

public class AppAuthenticator {
	private static final String PERSONCODE_ATTRNAME = "personCode";
	private static final String AUTHTOKEN_COOKIENAME = "authToken";

	private Algorithm signingAlg;
	private JWTVerifier verifier; 
	
	public AppAuthenticator(Algorithm signingAlg) {
		this.signingAlg = signingAlg;
		this.verifier = JWT.require(this.signingAlg)
			.acceptLeeway(5)
			.build();
	}
	
	public void setClientIdentity(HttpServletRequest request, HttpServletResponse response, 
			String personCode, boolean rememberMe){ 

		if(rememberMe) {
			setJWTCookie(request, response, personCode);
		}
		request.getSession().setAttribute(PERSONCODE_ATTRNAME, personCode);
	}
	
	public String getClientIdentity(HttpServletRequest request) {
		// Get current session identity
		{
			HttpSession session = request.getSession(false);
			if(session != null) {
				String personCode = (String) session.getAttribute(PERSONCODE_ATTRNAME);
				if(personCode != null) {
					return personCode;
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
					request.getSession().setAttribute(PERSONCODE_ATTRNAME, personCode);
					return personCode;
				} catch (JWTVerificationException invalidToken) {
					// TODO log invalid jwt
				}
			}
		}
		
		// Not authenticated
		return null;
	}
	
	private void setJWTCookie(HttpServletRequest request, HttpServletResponse response, String personCode) {
		Instant tomorrow = Instant.now()
			.plus(1, ChronoUnit.DAYS)
			.truncatedTo(ChronoUnit.DAYS);
		
		JWTCreator.Builder builder = JWT.create();
		String jwtEncoded = builder
			.withIssuer("it.polimi.poliesami")
			.withSubject(personCode)
			.withAudience("it.polimi.poliesami")
			.withExpiresAt(Date.from(tomorrow))
			.withIssuedAt(new Date())
			.sign(this.signingAlg);

		// Manually build Cookie header
		response.addHeader("Set-Cookie",
			AUTHTOKEN_COOKIENAME + "=" + jwtEncoded + "; " +
			"Expires=" + ZonedDateTime.ofInstant(tomorrow, ZoneOffset.UTC).format(DateTimeFormatter.RFC_1123_DATE_TIME) + "; " +
			"Path=" + request.getContextPath()
		);
	}
}
