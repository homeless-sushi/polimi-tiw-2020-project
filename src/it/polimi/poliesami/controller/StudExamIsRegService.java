package it.polimi.poliesami.controller;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.polimi.db.dao.ExamRegistrationDAO;
import it.polimi.poliesami.business.IdentityBean;
import it.polimi.poliesami.utils.HttpUtils;

public class StudExamIsRegService extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(StudExamIsRegService.class.getName());
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
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

		HttpSession session = request.getSession();
		IdentityBean identity = (IdentityBean) session.getAttribute("identity");

		ServletContext servletCtx = getServletContext();
		ExamRegistrationDAO examRegistrationDAO = (ExamRegistrationDAO) servletCtx.getAttribute("examRegistrationDAO");

		Map<String,Object> params = Map.of("examId", examId);
		if(examRegistrationDAO.isStudentRegistered(identity.getCareerId(), examId)) {
			HttpUtils.redirectWithParams(request, response, "/inside/student/examRegistration/registered", params);
		} else {
			HttpUtils.redirectWithParams(request, response, "/inside/student/examRegistration/notRegistered", params);
		}
	}
}

