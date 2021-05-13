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

import it.polimi.db.business.ExamRegistrationBean;
import it.polimi.db.business.ExamResult;
import it.polimi.db.business.ExamStatus;
import it.polimi.db.dao.ExamRegistrationDAO;

public class ProfEditExamPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private String templatePath;

	private static final ExamStatus[] statusOptions = new ExamStatus[]{ExamStatus.NINS, ExamStatus.INS};
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		templatePath = getInitParameter("templatePath");	
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		final ServletContext servletCtx = getServletContext();
		final String examIdString = req.getParameter("examId");
		final int examId = Integer.parseInt(examIdString);

		final String studentIdString = req.getParameter("studentId");
		final int studentId = Integer.parseInt(studentIdString);

		final ExamRegistrationDAO examRegistrationDAO = (ExamRegistrationDAO) servletCtx.getAttribute("examRegistrationDAO");
		final ExamRegistrationBean examRegistration = examRegistrationDAO.getProfessorExamRegistration(studentId, examId);

		final WebContext ctx = new WebContext(req, res, servletCtx, req.getLocale());
		ctx.setVariable("examRegistration", examRegistration);
		ctx.setVariable("statuses", statusOptions);
		ctx.setVariable("results", ExamResult.values());

		final TemplateEngine templateEngine = (TemplateEngine) servletCtx.getAttribute("templateEngine");
		templateEngine.process(templatePath, ctx, res.getWriter());
	}
}
