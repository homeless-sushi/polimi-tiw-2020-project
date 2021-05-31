package it.polimi.poliesami.api.identity;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginFilter extends HttpFilter {
	@Override
	protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		try {
			Identity identity = authenticate(request.getHeader("Authorization"));
			request.setAttribute("identity", identity);
			chain.doFilter(request, response);
			return;
		} catch (AuthenticationException e) {
			response.setHeader("WWW-Authenticate", "Bearer");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			request.setAttribute("jsonError", new AuthenticationException("Not authorized", e));
		} catch (IllegalArgumentException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			request.setAttribute("jsonError", e);
		}
		RequestDispatcher jsonDispatcher = getServletContext().getNamedDispatcher("JsonMapper");
		jsonDispatcher.forward(request, response);
	}

	private Identity authenticate(String authorization) throws AuthenticationException {
		if(authorization == null)
			throw new AuthenticationException("No authorization header");

		String[] auth = authorization.split(" ", 2);
		if(auth.length < 2)
			throw new IllegalArgumentException("Malformed authorization header");

		if (!auth[0].equalsIgnoreCase("bearer"))
				throw new IllegalArgumentException("Invalid authorization type");

		ApiAuthenticator apiAuthenticator = (ApiAuthenticator) getServletContext().getAttribute("apiAuthenticator");
		return apiAuthenticator.readJWT(auth[1]);
	}
}
