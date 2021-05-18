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

import it.polimi.db.business.ExamBean;
import it.polimi.db.business.ExamRegistrationBean;
import it.polimi.db.dao.ExamRegistrationDAO;
import it.polimi.poliesami.business.IdentityBean;
import it.polimi.poliesami.controller.StudExamRegService;

public class StudExamRegPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private String templatePath;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		templatePath = getInitParameter("templatePath");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final ServletContext servletCtx = getServletContext();
		final ExamBean exam = (ExamBean) request.getAttribute("exam");
		
		IdentityBean identity = (IdentityBean) request.getAttribute("identity");

		final ExamRegistrationDAO examRegistrationDAO = (ExamRegistrationDAO) servletCtx.getAttribute("examRegistrationDAO");
		final ExamRegistrationBean examRegistration = examRegistrationDAO.getStudentExamRegistration(identity.getCareerId(), exam.getId());
		
		final WebContext ctx = new WebContext(request, response, servletCtx, request.getLocale());
		ctx.setVariable("examRegistration", examRegistration);
		ctx.setVariable("register", StudExamRegService.ACTION.REGISTER.toString());
		ctx.setVariable("deregister", StudExamRegService.ACTION.DEREGISTER.toString());

		TemplateEngine templateEngine = (TemplateEngine) servletCtx.getAttribute("templateEngine");
		templateEngine.process(templatePath, ctx, response.getWriter());
	}
}
