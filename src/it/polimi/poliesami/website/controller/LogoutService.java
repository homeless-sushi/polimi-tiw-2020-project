package it.polimi.poliesami.website.controller;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.poliesami.website.utils.AppAuthenticator;
import it.polimi.poliesami.website.utils.HttpUtils;

public class LogoutService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String loginPage;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletContext servletCtx = config.getServletContext();
		loginPage = servletCtx.getInitParameter("loginPage");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext servletCtx = getServletContext();
		
		AppAuthenticator clientAuthenticator = (AppAuthenticator) servletCtx.getAttribute("clientAuthenticator");
		clientAuthenticator.deleteClientIdentity(request, response);

		HttpUtils.redirect(request, response, loginPage);
	}
}
