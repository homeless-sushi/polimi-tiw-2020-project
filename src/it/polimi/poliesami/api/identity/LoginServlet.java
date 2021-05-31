package it.polimi.poliesami.api.identity;

import java.io.IOException;
import java.util.Base64;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			JsonIdentity identity = authenticate(request.getHeader("Authorization"));
			request.setAttribute("jsonBody", identity);
		} catch (AuthenticationException e) {
			response.setHeader("WWW-Authenticate", "Basic");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			request.setAttribute("jsonError", new AuthenticationException("Failed login", e));
		} catch (IllegalArgumentException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			request.setAttribute("jsonError", e);
		}
		RequestDispatcher jsonDispatcher = getServletContext().getNamedDispatcher("JsonMapper");
		jsonDispatcher.forward(request, response);
	}

	private JsonIdentity authenticate(String authorization) throws AuthenticationException {
		if(authorization == null)
			throw new AuthenticationException("No authorization header");

		String[] auth = authorization.split(" ", 2);
		if(auth.length < 2)
			throw new IllegalArgumentException("Malformed authorization header");

		switch (auth[0].toLowerCase()) {
			case "basic":
				return basicAuthorization(auth[1]);
			case "bearer":
				return bearerAuthorization(auth[1]);
			default:
				throw new IllegalArgumentException("Invalid authorization type");
		}
	}

	private JsonIdentity basicAuthorization(String encoded) throws AuthenticationException {
		byte[] decoded = Base64.getDecoder().decode(encoded);

		String[] credentials = new String(decoded).split(":", 2);
		if(credentials.length < 2)
			throw new IllegalArgumentException("Malformed basic credentials");

		ApiAuthenticator apiAuthenticator = (ApiAuthenticator) getServletContext().getAttribute("apiAuthenticator");
		return apiAuthenticator.login(credentials[0], credentials[1]);
	}

	private JsonIdentity bearerAuthorization(String jwt) throws AuthenticationException {
		ApiAuthenticator apiAuthenticator = (ApiAuthenticator) getServletContext().getAttribute("apiAuthenticator");
		return apiAuthenticator.loginJWT(jwt);
	}
}
