package it.polimi.poliesami.controller;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.db.business.ExamRegistrationBean;
import it.polimi.db.business.ExamResult;
import it.polimi.db.business.ExamStatus;
import it.polimi.db.dao.ExamRegistrationDAO;
import it.polimi.poliesami.utils.HttpUtils;

public class ProfExamRegEditService extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(ProfExamRegEditService.class.getName());

	private String profEditExamPage;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletContext servletCtx = config.getServletContext();
		profEditExamPage = servletCtx.getInitParameter("profEditExamPage");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		final ServletContext servletCtx = getServletContext();
		final ExamRegistrationBean examRegistration = (ExamRegistrationBean) req.getAttribute("examRegistration");

		fail : {
			final ExamResult examResult;
			try {
				examResult = ExamResult.valueOf(req.getParameter("examResult"));
			} catch (NullPointerException | IllegalArgumentException e) {
				break fail;
			}

			final ExamStatus examStatus = examResult == ExamResult.VUOTO ? ExamStatus.NINS : ExamStatus.INS;

			final int grade;
			try {
				grade = Integer.parseInt(req.getParameter("grade"));
			} catch (NumberFormatException e) {
				break fail;
			}

			final boolean laude = Boolean.parseBoolean(req.getParameter("laude"));
			
			examRegistration.setResult(examResult);
			examRegistration.setStatus(examStatus);
			examRegistration.setGrade(grade);
			examRegistration.setLaude(laude);

			ExamRegistrationDAO examRegistrationDAO = (ExamRegistrationDAO) servletCtx.getAttribute("examRegistrationDAO");
			if(!examRegistrationDAO.editExamEval(examRegistration))
				break fail;
			
			logger.log(Level.FINER, "{0}: Edit evaluation of student {1} exam {2}", new Object[]{req.getRemoteHost(), examRegistration.getStudentId(), examRegistration.getExamId()});
		}

		Map<String,Object> params = Map.of("examId", examRegistration.getExamId(), "studentId", examRegistration.getStudentId());
		HttpUtils.redirectWithParams(req, res, profEditExamPage, params);
	}
}

