package it.polimi.poliesami.website.view;

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

import it.polimi.poliesami.db.business.CareerBean;
import it.polimi.poliesami.db.dao.CareerDAO;
import it.polimi.poliesami.website.business.IdentityBean;

public class CareersPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private String templatePath;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		templatePath = getInitParameter("templatePath");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		ServletContext servletCtx = getServletContext();
		
		IdentityBean identity = (IdentityBean) request.getAttribute("identity");
		
		CareerDAO careerDAO = (CareerDAO) servletCtx.getAttribute("careerDAO");		
		List<CareerBean> careers = careerDAO.getUserCareers(identity.getPersonCode());
		
		WebContext ctx = new WebContext(request, response, servletCtx, request.getLocale());
		ctx.setVariable("careers", careers);
		
		TemplateEngine templateEngine = (TemplateEngine) servletCtx.getAttribute("templateEngine");
		templateEngine.process(templatePath, ctx, response.getWriter());
	}

}
