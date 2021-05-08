package it.polimi.poliesami.view;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import it.polimi.db.business.CareerBean;
import it.polimi.db.dao.CareerDAO;
import it.polimi.poliesami.business.IdentityBean;
import it.polimi.poliesami.utils.AppAuthenticator;

public class CareersPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private String templatePath;
	private String careerService;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		templatePath = getInitParameter("templatePath");

		ServletContext servletCtx = config.getServletContext();
		careerService = servletCtx.getInitParameter("careerService");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		ServletContext servletCtx = getServletContext();
		
		AppAuthenticator clientAutheticator = (AppAuthenticator) servletCtx.getAttribute("clientAuthenticator");
		IdentityBean identity = clientAutheticator.getClientIdentity(request);
		
		CareerDAO careerDAO = (CareerDAO) servletCtx.getAttribute("careerDAO");		
		List<CareerBean> careers = careerDAO.getUserCareers(identity.getPersonCode());
		
		WebContext ctx = new WebContext(request, response, servletCtx, request.getLocale());
		ctx.setVariable("careers", careers);
		ctx.setVariable("careerService", careerService);
		
		TemplateEngine templateEngine = (TemplateEngine) servletCtx.getAttribute("templateEngine");
		templateEngine.process(templatePath, ctx, response.getWriter());
	}

}
