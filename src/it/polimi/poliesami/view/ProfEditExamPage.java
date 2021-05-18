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
import it.polimi.db.business.ExamResult;
import it.polimi.db.dao.ExamRegistrationDAO;

public class ProfEditExamPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private String templatePath;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		templatePath = getInitParameter("templatePath");	
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		final ServletContext servletCtx = getServletContext();
		final ExamBean exam = (ExamBean) req.getAttribute("exam");
		final int examId = exam.getId();

		final String studentIdString = req.getParameter("studentId");
		final int studentId = Integer.parseInt(studentIdString);

		final ExamRegistrationDAO examRegistrationDAO = (ExamRegistrationDAO) servletCtx.getAttribute("examRegistrationDAO");
		final ExamRegistrationBean examRegistration = examRegistrationDAO.getProfessorExamRegistration(studentId, examId);

		final WebContext ctx = new WebContext(req, res, servletCtx, req.getLocale());
		ctx.setVariable("examRegistration", examRegistration);
		ctx.setVariable("results", ExamResult.values());

		final TemplateEngine templateEngine = (TemplateEngine) servletCtx.getAttribute("templateEngine");
		templateEngine.process(templatePath, ctx, res.getWriter());
	}
}
