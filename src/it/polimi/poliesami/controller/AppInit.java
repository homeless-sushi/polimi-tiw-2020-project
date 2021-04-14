package it.polimi.poliesami.controller;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.*;

import javax.sql.DataSource;

@WebListener
public class AppInit implements ServletContextListener {
	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext servletContext = event.getServletContext();
		
		try {
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			DataSource polimiDB = (DataSource) envCtx.lookup("jdbc/polimiDB");
			servletContext.setAttribute("polimiDB", polimiDB);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
}
