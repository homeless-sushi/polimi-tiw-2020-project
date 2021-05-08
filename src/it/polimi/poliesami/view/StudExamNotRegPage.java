package it.polimi.poliesami.view;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import it.polimi.poliesami.controller.StudExamRegService;


public class StudExamNotRegPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private String templatePath;
	private String studExamRegistrationService;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		templatePath = getInitParameter("templatePath");

		ServletContext servletCtx = config.getServletContext();
		studExamRegistrationService = servletCtx.getInitParameter("studExamRegService");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String examIdString = request.getParameter("examId");

		ServletContext servletCtx = getServletContext();
		WebContext ctx = new WebContext(request, response, servletCtx, request.getLocale());

		ctx.setVariable("studExamRegService", studExamRegistrationService);
		ctx.setVariable("action", StudExamRegService.ACTION.REGISTER.toString());
		ctx.setVariable("examId", examIdString);
		
		TemplateEngine templateEngine = (TemplateEngine) servletCtx.getAttribute("templateEngine");
		templateEngine.process(templatePath, ctx, response.getWriter());
	}
}
