package it.polimi.poliesami.utils;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.Cookie;
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
	private static final String AUTHTOKEN_ATTRNAME = "authToken";

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
		
		String personCode;
		HttpSession session = request.getSession();

		personCode = (String) session.getAttribute(PERSONCODE_ATTRNAME);
		if(personCode != null) {
			return personCode;
		}
		
		CookieMap cookies = new CookieMap(request.getCookies());
		String jwtEncoded = cookies.get(AUTHTOKEN_ATTRNAME);
		if(jwtEncoded == null) {
			return null;
		}
		
		DecodedJWT jwtDecoded = null;
		
		try {
			this.verifier.verify(jwtEncoded);
			jwtDecoded = JWT.decode(jwtEncoded);
		} catch (JWTVerificationException invalidToken) {
			return null;
		}
		
		personCode = jwtDecoded.getSubject();
		session.setAttribute(PERSONCODE_ATTRNAME, personCode);
		return personCode;
	}
	
	private void setJWTCookie(HttpServletRequest request, HttpServletResponse response, String personCode) {
				
		Calendar tomorrow = this.getTomorrow();
		
		JWTCreator.Builder builder = JWT.create();
		String jwtEncoded = builder
			.withIssuer("it.polimi.poliesami")
			.withSubject(personCode)
			.withAudience("it.polimi.poliesami")
			.withExpiresAt(tomorrow.getTime())
			.withIssuedAt(new Date())
			.sign(this.signingAlg);
		
		Cookie jwtCookie = new Cookie(AUTHTOKEN_ATTRNAME, jwtEncoded);
		jwtCookie.setMaxAge((int) ((tomorrow.getTimeInMillis() - System.currentTimeMillis()) / 1000));
		jwtCookie.setPath(request.getContextPath());
		
		response.addCookie(jwtCookie);
	}
	
	private Calendar getTomorrow() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		
		return c;
	}
}
