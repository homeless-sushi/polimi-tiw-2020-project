package it.polimi.poliesami.controller;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.poliesami.utils.AppAuthenticator;
import it.polimi.poliesami.utils.HttpUtils;

public class LoginInsideFilter extends HttpFilter {
	private static final long serialVersionUID = 1L;
	private String loginPage;

	@Override
	public void init(FilterConfig config) throws ServletException {
		super.init(config);
		ServletContext servletCtx = config.getServletContext();
		loginPage = servletCtx.getInitParameter("loginPage");
	}

	@Override
	public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
		throws IOException, ServletException {
		
		System.out.println("LoginInsideFilter");
		ServletContext servletCtx = getServletContext();
		
		AppAuthenticator clientAuthenticator = (AppAuthenticator) servletCtx.getAttribute("clientAuthenticator");
		String identity = clientAuthenticator.getClientIdentity(req);
		
		if(identity == null) {
			System.out.println("Torna al login, besugo!");
			HttpUtils.redirect(req, res, loginPage);
			return;
		}

		chain.doFilter(req, res);
	}
}
