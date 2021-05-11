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

import it.polimi.db.dao.ExamRegistrationDAO;
import it.polimi.poliesami.utils.HttpUtils;

public class ProfExamPublishService extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(ProfExamPublishService.class.getName());

	private String profExamRegPage;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletContext servletCtx = config.getServletContext();
		profExamRegPage = servletCtx.getInitParameter("profExamRegPage");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext servletCtx = getServletContext();
		String examIdString = request.getParameter("examId");
		int examId = Integer.parseInt(examIdString);

		ExamRegistrationDAO examRegistrationDAO = (ExamRegistrationDAO) servletCtx.getAttribute("examRegistrationDAO");
		if(examRegistrationDAO.publishExamEval(examId)){
			logger.log(Level.FINER, "{0}: Published grades of exam {1}", new Object[]{request.getRemoteHost(), examIdString});
		}else{
			logger.log(Level.FINER, "{0}: Couldn''t publish grades of exam {1}", new Object[]{request.getRemoteHost(), examIdString});
		}

		Map<String,Object> params = Map.of("examId",examId);
		HttpUtils.redirectWithParams(request, response, profExamRegPage, params);
	}
}

