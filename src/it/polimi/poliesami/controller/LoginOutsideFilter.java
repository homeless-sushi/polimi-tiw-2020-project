package it.polimi.poliesami.controller;

import java.io.IOException;
import javax.servlet.ServletException;

import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.poliesami.utils.AppAuthenticator;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;

public class LoginOutsideFilter extends HttpFilter {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
		throws IOException, ServletException{
		
		System.out.println("LoginOutsideFilter");
		ServletContext servletCtx = getServletContext();
		
		AppAuthenticator clientAuthenticator = (AppAuthenticator) servletCtx.getAttribute("clientAuthenticator");
		String identity = clientAuthenticator.getClientIdentity(req);
		
		if(identity != null) {
			System.out.println("Sei già loggato, besugo");
			// TODO redirect to careers
			return;
		}

		chain.doFilter(req, res);
	}
}
