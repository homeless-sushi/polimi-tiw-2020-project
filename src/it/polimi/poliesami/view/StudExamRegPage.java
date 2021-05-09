package it.polimi.poliesami.view;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
		final String examIdString = request.getParameter("examId");
		final int examId = Integer.parseInt(examIdString);

		final ServletContext servletCtx = getServletContext();
		
		final HttpSession session = request.getSession();
		final IdentityBean identity = (IdentityBean) session.getAttribute("identity");
		final ExamRegistrationDAO examRegistrationDAO = (ExamRegistrationDAO) servletCtx.getAttribute("examRegistrationDAO");
		final boolean isRegistered = examRegistrationDAO.isStudentRegistered(identity.getCareerId(), examId);
		
		final WebContext ctx = new WebContext(request, response, servletCtx, request.getLocale());

		final ExamDAO examDAO = (ExamDAO) servletCtx.getAttribute("examDAO");
		final ExamBean exam = examDAO.getExam(examId);
		ctx.setVariable("exam", exam);
		final CourseDAO courseDAO = (CourseDAO) servletCtx.getAttribute("courseDAO");
		final CourseBean course = courseDAO.getCourseFromExam(examId);
		ctx.setVariable("course", course);

		ctx.setVariable("isRegistered", isRegistered);
		if(isRegistered){
		ctx.setVariable("DEREGISTER", StudExamRegService.ACTION.DEREGISTER.toString());
		ctx.setVariable("NINS", ExamStatus.NINS.toString());
		ctx.setVariable("INS", ExamStatus.INS.toString());
		ctx.setVariable("VERB", ExamStatus.VERB.toString());
		ctx.setVariable("PASS", ExamResult.PASS.toString());
		final ExamRegistrationBean examRegistration = examRegistrationDAO.getStudentExamRegistration(identity.getCareerId(), examId);
		ctx.setVariable("examRegistration", examRegistration);
		}else{
		ctx.setVariable("REGISTER", StudExamRegService.ACTION.REGISTER.toString());
		ctx.setVariable("examId", examId);
		}

		TemplateEngine templateEngine = (TemplateEngine) servletCtx.getAttribute("templateEngine");
		templateEngine.process(templatePath, ctx, response.getWriter());
	}
}
