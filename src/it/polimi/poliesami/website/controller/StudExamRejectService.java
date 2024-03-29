package it.polimi.poliesami.website.controller;

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

import it.polimi.poliesami.db.business.ExamBean;
import it.polimi.poliesami.db.dao.ExamRegistrationDAO;
import it.polimi.poliesami.website.business.IdentityBean;
import it.polimi.poliesami.website.utils.HttpUtils;

public class StudExamRejectService extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(StudExamRejectService.class.getName());

	private String studExamRegPage;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletContext servletCtx = config.getServletContext();
		studExamRegPage = servletCtx.getInitParameter("studExamRegPage");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext servletCtx = getServletContext();
		ExamBean exam = (ExamBean) request.getAttribute("exam");
		int examId = exam.getId();
		
		IdentityBean identity = (IdentityBean) request.getAttribute("identity");

		ExamRegistrationDAO examRegistrationDAO = (ExamRegistrationDAO) servletCtx.getAttribute("examRegistrationDAO");

		if(!examRegistrationDAO.rejectStudExam(identity.getCareer().getId(), examId)) {
			logger.log(Level.FINER, "{0}: Couldn''t reject evaluation of exam {1}", new Object[]{request.getRemoteHost(), examId});
		} else {
			logger.log(Level.FINER, "{0}: Rejected evaluation of exam {1}", new Object[]{request.getRemoteHost(), examId});
		}

		Map<String,Object> params = Map.of("examId", examId);
		HttpUtils.redirectWithParams(request, response, studExamRegPage, params);
	}
}

