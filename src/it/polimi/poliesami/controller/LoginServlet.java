package it.polimi.poliesami.controller;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.db.business.UserBean;
import it.polimi.db.dao.UserDAO;
import it.polimi.db.utils.Authenticator;

import it.polimi.poliesami.utils.AppAuthenticator;
import it.polimi.poliesami.utils.HttpUtils;

public class LoginServlet extends HttpServlet {
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
		HttpUtils.redirect(request, response, loginPage);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String personCode = request.getParameter("person_code");
		String plainPsw = request.getParameter("password");
		boolean allDayLogin = request.getParameter("all_day") != null;
		
		ServletContext servletCtx = getServletContext();
		
		UserDAO userDAO = (UserDAO) servletCtx.getAttribute("userDAO");
		UserBean user = userDAO.getUserByPersonCode(personCode);

		if(user == null) {
			System.out.println("No such user exists");
			// TODO session error
			HttpUtils.redirect(request, response, loginPage);
			return;
		}
		
		Authenticator userAuthenticator = (Authenticator) servletCtx.getAttribute("userAuthenticator");
		boolean success = userAuthenticator.verify(plainPsw.getBytes(), user.getHashedPassword());
		
		if(!success) {
			System.out.println("User and password don't match");
			// TODO session error
			HttpUtils.redirect(request, response, loginPage);
			return;
		}
		
		AppAuthenticator clientAuthenticator = (AppAuthenticator) servletCtx.getAttribute("clientAuthenticator");
		clientAuthenticator.setClientIdentity(request, response, personCode, allDayLogin);
		// TODO redirect to careers
		
		HttpUtils.redirect(request, response, "/inside/");
	}
}
