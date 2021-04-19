package it.polimi.poliesami.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.poliesami.utils.AppAuthenticator;
import it.polimi.poliesami.utils.HttpUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;

public class LoginOutsideFilter extends HttpFilter {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(LoginOutsideFilter.class.getName());

	@Override
	protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
		throws IOException, ServletException{
		
		ServletContext servletCtx = getServletContext();
		
		AppAuthenticator clientAuthenticator = (AppAuthenticator) servletCtx.getAttribute("clientAuthenticator");
		String identity = clientAuthenticator.getClientIdentity(req);
		
		if(identity != null) {
			logger.log(Level.FINER, "{0}: Already logged in", req.getRemoteHost());
			// TODO redirect to careers
			HttpUtils.redirect(req, res, "/inside/");
			return;
		}

		chain.doFilter(req, res);
	}
}
