package it.polimi.poliesami.controller;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.db.business.UserBean;
import it.polimi.db.dao.UserDAO;
import it.polimi.db.utils.Authenticator;

import it.polimi.poliesami.utils.AppAuthenticator;

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO redirect to login
		return;
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
			// TODO redirect to failed login
			return;
		}
		
		Authenticator userAuthenticator = (Authenticator) servletCtx.getAttribute("userAuthenticator");
		boolean success = userAuthenticator.verify(plainPsw.getBytes(), user.getHashedPassword());
		
		if(!success) {
			System.out.println("User and password don't match");
			return;
			// TODO redirect to failed login
		}
		
		AppAuthenticator clientAuthenticator = (AppAuthenticator) servletCtx.getAttribute("clientAuthenticator");
		clientAuthenticator.setClientIdentity(request, response, personCode, allDayLogin);
		// TODO redirect to careers
		
	}
}
