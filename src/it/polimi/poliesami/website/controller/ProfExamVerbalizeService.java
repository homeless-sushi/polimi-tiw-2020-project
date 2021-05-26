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
import it.polimi.poliesami.website.utils.HttpUtils;

public class ProfExamVerbalizeService extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(ProfExamVerbalizeService.class.getName());

	private String profExamRegPage;
	private String profRecordsPage;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletContext servletCtx = config.getServletContext();
		profExamRegPage = servletCtx.getInitParameter("profExamRegPage");
		profRecordsPage = servletCtx.getInitParameter("profRecordsPage");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext servletCtx = getServletContext();
		ExamBean exam = (ExamBean) request.getAttribute("exam");
		int examId = exam.getId();

		Map<String,Object> params = Map.of("examId", examId);

		ExamRegistrationDAO examRegistrationDAO = (ExamRegistrationDAO) servletCtx.getAttribute("examRegistrationDAO");
		if(examRegistrationDAO.verbalizeExamEval(examId)){
			logger.log(Level.FINER, "{0}: Verbalized grades of exam {1}", new Object[]{request.getRemoteHost(), examId});
			HttpUtils.redirectWithParams(request, response, profRecordsPage, params);
		}else{
			logger.log(Level.FINER, "{0}: Couldn''t verbalize grades of exam {1}", new Object[]{request.getRemoteHost(), examId});
			HttpUtils.redirectWithParams(request, response, profExamRegPage, params);
		}
	}
}
