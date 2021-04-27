package it.polimi.poliesami;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.*;

import javax.sql.DataSource;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import com.auth0.jwt.algorithms.Algorithm;

import it.polimi.db.dao.CareerDAO;
import it.polimi.db.dao.UserDAO;
import it.polimi.db.utils.Authenticator;

import it.polimi.poliesami.utils.AppAuthenticator;

@WebListener
public class AppInit implements ServletContextListener {
	private static final Logger logger = Logger.getLogger(AppInit.class.getName());

	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext servletContext = event.getServletContext();
		
		DataSource polimiDB = null;
		try {
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			polimiDB = (DataSource) envCtx.lookup("jdbc/polimiDB");
		} catch (NamingException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		
		servletContext.setAttribute("polimiDB", polimiDB);

		/* ********** DAOs ********** */
		UserDAO userDAO = new UserDAO(polimiDB);
		servletContext.setAttribute("userDAO", userDAO);
		
		CareerDAO careerDAO = new CareerDAO(polimiDB);
		servletContext.setAttribute("careerDAO", careerDAO);
		
		/* ********** Authenticators ********** */
		Authenticator userAuthenticator = new Authenticator();
		servletContext.setAttribute("userAuthenticator", userAuthenticator);
		
		AppAuthenticator clientAuthenticator = new AppAuthenticator(
				Algorithm.HMAC256("\"The Garden Of Earthly Delights\" By Hieronymus Bosch"));
		servletContext.setAttribute("clientAuthenticator", clientAuthenticator);
		
		/* ********** Thymeleaf Template Engine ********** */
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);

		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setPrefix("/WEB-INF/templates/");
		templateResolver.setSuffix(".html");

		TemplateEngine templateEngine = new TemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);
		servletContext.setAttribute("templateEngine", templateEngine);
	}
}
