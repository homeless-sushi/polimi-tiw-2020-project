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
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import at.favre.lib.crypto.bcrypt.BCrypt;

import com.auth0.jwt.algorithms.Algorithm;

import it.polimi.poliesami.api.identity.ApiAuthenticator;
import it.polimi.poliesami.db.dao.CareerDAO;
import it.polimi.poliesami.db.dao.CourseDAO;
import it.polimi.poliesami.db.dao.ExamDAO;
import it.polimi.poliesami.db.dao.ExamRecordDAO;
import it.polimi.poliesami.db.dao.ExamRegistrationDAO;
import it.polimi.poliesami.db.dao.UserDAO;
import it.polimi.poliesami.db.utils.Authenticator;
import it.polimi.poliesami.website.utils.AppAuthenticator;

@WebListener
public class AppInit implements ServletContextListener {
	private static final Logger logger = Logger.getLogger(AppInit.class.getName());

	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext servletContext = event.getServletContext();

		servletContext.setRequestCharacterEncoding("UTF-8");
		servletContext.setResponseCharacterEncoding("UTF-8");
		
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

		CourseDAO courseDAO = new CourseDAO(polimiDB);
		servletContext.setAttribute("courseDAO", courseDAO);

		ExamDAO examDAO = new ExamDAO(polimiDB);
		servletContext.setAttribute("examDAO", examDAO);

		ExamRegistrationDAO examRegistrationDAO = new ExamRegistrationDAO(polimiDB);
 		servletContext.setAttribute("examRegistrationDAO", examRegistrationDAO);
		
		ExamRecordDAO examRecordDAO = new ExamRecordDAO(polimiDB);
		servletContext.setAttribute("examRecordDAO", examRecordDAO);
		
		/* ********** Authenticators ********** */
		Authenticator userAuthenticator = new Authenticator(BCrypt.withDefaults(), BCrypt.verifyer());
		Algorithm jwtAlgorithm = Algorithm.HMAC256("\"The Garden Of Earthly Delights\" By Hieronymus Bosch");

		AppAuthenticator clientAuthenticator = new AppAuthenticator(
			userDAO,
			careerDAO,
			userAuthenticator,
			jwtAlgorithm);
		servletContext.setAttribute("clientAuthenticator", clientAuthenticator);

		ApiAuthenticator apiAuthenticator = new ApiAuthenticator(
			userDAO,
			careerDAO,
			userAuthenticator,
			jwtAlgorithm);
		servletContext.setAttribute("apiAuthenticator", apiAuthenticator);

		/* ********** Thymeleaf Template Engine ********** */
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);

		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setCharacterEncoding("UTF-8");
		templateResolver.setPrefix("/WEB-INF/templates/");
		templateResolver.setSuffix(".html");

		TemplateEngine templateEngine = new TemplateEngine();
		templateEngine.addDialect(new Java8TimeDialect());
		templateEngine.setTemplateResolver(templateResolver);
		servletContext.setAttribute("templateEngine", templateEngine);

		ServletContextTemplateResolver jsTemplateResolver = new ServletContextTemplateResolver(servletContext);

		jsTemplateResolver.setTemplateMode(TemplateMode.JAVASCRIPT);
		jsTemplateResolver.setCharacterEncoding("UTF-8");
		jsTemplateResolver.setPrefix("/WEB-INF/api/");
		jsTemplateResolver.setSuffix(".js");

		TemplateEngine jsTemplateEngine = new TemplateEngine();
		jsTemplateEngine.setTemplateResolver(jsTemplateResolver);
		servletContext.setAttribute("jsTemplateEngine", jsTemplateEngine);
	}
}
