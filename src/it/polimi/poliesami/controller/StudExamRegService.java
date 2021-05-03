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
import javax.servlet.http.HttpSession;

import it.polimi.db.dao.ExamRegistrationDAO;
import it.polimi.poliesami.business.IdentityBean;
import it.polimi.poliesami.utils.HttpUtils;

public class StudExamRegService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(StudExamRegService.class.getName());
	private String examRegistrationPage;
	private String studentExamsPage;
	
	public enum ACTION {
		REGISTER("register"),
		DEREGISTER("deregister");

		private String string; 

		private ACTION(String action){ this.string = action; }

		@Override
		public String toString(){ return this.string; }

		public static ACTION fromString(String action) {
			for(ACTION actionType : ACTION.values()) {
				if(actionType.string.equalsIgnoreCase(action)) {
					return actionType;
				}
			}
			return null;
		}
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletContext servletCtx = config.getServletContext();
		examRegistrationPage = servletCtx.getInitParameter("examRegistrationPage");
		studentExamsPage = servletCtx.getInitParameter("studentExamsPage");
		
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String actionString = request.getParameter("action");
		String examIdString = request.getParameter("examId");
		
		fail : {
			int examId;
			try {
				examId = Integer.parseInt(examIdString);
			} catch (NumberFormatException e) {
				break fail;
			}
			
			ACTION action = ACTION.fromString(actionString);
			if(action == null){
				break fail;
			}
			
			HttpSession session = request.getSession();
			IdentityBean identity = (IdentityBean) session.getAttribute("identity");
			int careerId = identity.getCareerId();
			
			ServletContext servletCtx = getServletContext();
			ExamRegistrationDAO examRegistrationDAO = (ExamRegistrationDAO) servletCtx.getAttribute("examRegistrationDAO");

			switch (action) {
				case REGISTER:
					if(!examRegistrationDAO.registerToExam(careerId, examId)){
						break fail;
					}
					logger.log(Level.FINER, "{0}: Student {1} registered to {2}", new Object[]{request.getRemoteHost(), careerId, examId});
					break;
				case DEREGISTER:
					if(!examRegistrationDAO.deregisterFromExam(careerId, examId)){
						break fail;
					}
					logger.log(Level.FINER, "{0}: Student {1} deregistered from {2}", new Object[]{request.getRemoteHost(), careerId, examId});
					break;
				default:
				break fail;
			}
			Map<String,Object> params = Map.of("examId", examId);
			HttpUtils.redirectWithParams(request, response, examRegistrationPage, params);
		}

		logger.log(Level.FINER, "{0}: Couldn''t {1} to/from exam {2}", new Object[]{request.getRemoteHost(), actionString, examIdString});
		HttpUtils.redirect(request, response, studentExamsPage);
	}
}
