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

import it.polimi.poliesami.db.business.ExamBean;
import it.polimi.poliesami.db.business.ExamRecordBean;
import it.polimi.poliesami.db.dao.ExamRecordDAO;
import it.polimi.poliesami.db.dao.ExamRegistrationDAO;

public class ProfRecordsPage extends HttpServlet {
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

		ExamRecordDAO examRecordDAO = (ExamRecordDAO) servletCtx.getAttribute("examRecordDAO");
		ExamRegistrationDAO examRegistrationDAO = (ExamRegistrationDAO) servletCtx.getAttribute("examRegistrationDAO");

		List<ExamRecordBean> records = examRecordDAO.getExamRecords(exam.getId());
		examRegistrationDAO.fetchRecordRegistrations(records);

		final WebContext ctx = new WebContext(req, res, servletCtx, req.getLocale());
		ctx.setVariable("records", records);

		final TemplateEngine templateEngine = (TemplateEngine) servletCtx.getAttribute("templateEngine");
		templateEngine.process(templatePath, ctx, res.getWriter());
	}
}
