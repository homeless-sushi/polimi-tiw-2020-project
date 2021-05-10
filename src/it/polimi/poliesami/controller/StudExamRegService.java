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
import it.polimi.poliesami.business.IdentityBean;
import it.polimi.poliesami.utils.AppAuthenticator;
import it.polimi.poliesami.utils.HttpUtils;

public class StudExamRegService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(StudExamRegService.class.getName());
	private String studExamRegPage;
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
		studExamRegPage = servletCtx.getInitParameter("studExamRegPage");
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

			ServletContext servletCtx = getServletContext();
			
			AppAuthenticator clientAutheticator = (AppAuthenticator) servletCtx.getAttribute("clientAuthenticator");
			IdentityBean identity = clientAutheticator.getClientIdentity(request);
			int careerId = identity.getCareerId();
			
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
			HttpUtils.redirectWithParams(request, response, studExamRegPage, params);
			return;
		}
		logger.log(Level.FINER, "{0}: Couldn''t {1} to/from exam {2}", new Object[]{request.getRemoteHost(), actionString, examIdString});
		HttpUtils.redirect(request, response, studentExamsPage);
	}
}
