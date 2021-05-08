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

import it.polimi.db.business.CourseBean;
import it.polimi.db.business.ExamBean;
import it.polimi.db.business.ExamRegistrationBean;
import it.polimi.db.business.ExamResult;
import it.polimi.db.business.ExamStatus;
import it.polimi.db.dao.CourseDAO;
import it.polimi.db.dao.ExamDAO;
import it.polimi.db.dao.ExamRegistrationDAO;
import it.polimi.poliesami.business.IdentityBean;
import it.polimi.poliesami.controller.StudExamRegService;
import it.polimi.poliesami.utils.AppAuthenticator;

public class StudExamRegPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private String templatePath;
	private String studExamRegService;
	private String studExamRejectService;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		templatePath = getInitParameter("templatePath");

		ServletContext servletCtx = config.getServletContext();
		studExamRegService = servletCtx.getInitParameter("studExamRegService");
		studExamRejectService = servletCtx.getInitParameter("studExamRejectService");
	}


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String examIdString = request.getParameter("examId");
			
		int examId;
		try {
			examId = Integer.parseInt(examIdString);
		} catch (NumberFormatException e) {
			// TODO handle invalid exam Id
			examId = 0;
		}

		ServletContext servletCtx = getServletContext();
		
		AppAuthenticator clientAutheticator = (AppAuthenticator) servletCtx.getAttribute("clientAuthenticator");
		IdentityBean identity = clientAutheticator.getClientIdentity(request);

		WebContext ctx = new WebContext(request, response, servletCtx, request.getLocale());
		ctx.setVariable("studExamRegService", studExamRegService);
		ctx.setVariable("studExamRejectService", studExamRejectService);
		ctx.setVariable("action", StudExamRegService.ACTION.DEREGISTER.toString());
		ctx.setVariable("NINS", ExamStatus.NINS.toString());
		ctx.setVariable("INS", ExamStatus.INS.toString());
		ctx.setVariable("VERB", ExamStatus.VERB.toString());
		ctx.setVariable("PASS", ExamResult.PASS.toString());

		ExamDAO examDAO = (ExamDAO) servletCtx.getAttribute("examDAO");
		ExamBean exam = examDAO.getExam(examId);
		ctx.setVariable("exam", exam);

		CourseDAO courseDAO = (CourseDAO) servletCtx.getAttribute("courseDAO");
		CourseBean course = courseDAO.getCourseFromExam(examId);
		ctx.setVariable("course", course);

		ExamRegistrationDAO examRegistrationDAO = (ExamRegistrationDAO) servletCtx.getAttribute("examRegistrationDAO");
		ExamRegistrationBean examRegistration = examRegistrationDAO.getStudentExamRegistration(identity.getCareerId(), examId);
		ctx.setVariable("examRegistration", examRegistration);
		
		TemplateEngine templateEngine = (TemplateEngine) servletCtx.getAttribute("templateEngine");
		templateEngine.process(templatePath, ctx, response.getWriter());
	}
}
