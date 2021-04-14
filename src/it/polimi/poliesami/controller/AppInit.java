package it.polimi.poliesami.controller;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.*;

import javax.sql.DataSource;

import com.auth0.jwt.algorithms.Algorithm;

import it.polimi.db.dao.UserDAO;
import it.polimi.db.utils.Authenticator;

import it.polimi.poliesami.utils.AppAuthenticator;

@WebListener
public class AppInit implements ServletContextListener {
	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext servletContext = event.getServletContext();
		
		DataSource polimiDB = null;
		try {
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			polimiDB = (DataSource) envCtx.lookup("jdbc/polimiDB");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
		servletContext.setAttribute("polimiDB", polimiDB);

		/* ********** DAOs ********** */
		UserDAO userDAO = new UserDAO(polimiDB);
		servletContext.setAttribute("userDAO", userDAO);
		
		/* ********** Authenticators ********** */
		Authenticator userAuthenticator = new Authenticator();
		servletContext.setAttribute("userAuthenticator", userAuthenticator);
		
		AppAuthenticator clientAuthenticator = new AppAuthenticator(
				Algorithm.HMAC256("\"The Garden Of Earthly Delights\" By Hieronymus Bosch"));
		servletContext.setAttribute("clientAuthenticator", clientAuthenticator);
	}
}
