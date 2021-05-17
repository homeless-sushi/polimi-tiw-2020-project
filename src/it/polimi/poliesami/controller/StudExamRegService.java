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

import it.polimi.db.business.ExamBean;
import it.polimi.db.dao.ExamRegistrationDAO;
import it.polimi.poliesami.business.IdentityBean;
import it.polimi.poliesami.utils.AppAuthenticator;
import it.polimi.poliesami.utils.HttpUtils;

public class StudExamRegService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(StudExamRegService.class.getName());
	private String studExamRegPage;
	
	public enum ACTION {
		REGISTER("/register") {
			@Override
			public boolean execute(ExamRegistrationDAO dao, int careerId, int examId) {
				return dao.registerToExam(careerId, examId);
			}
		},
		DEREGISTER("/deregister") {
			@Override
			public boolean execute(ExamRegistrationDAO dao, int careerId, int examId) {
				return dao.deregisterFromExam(careerId, examId);
			}
		};

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

		public abstract boolean execute(ExamRegistrationDAO dao, int careerId, int examId);
	}

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
		
		ACTION action = ACTION.fromString(request.getPathInfo());
		if(action == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, request.getRequestURI());
			return;
		}

		AppAuthenticator clientAutheticator = (AppAuthenticator) servletCtx.getAttribute("clientAuthenticator");
		IdentityBean identity = clientAutheticator.getClientIdentity(request);

		ExamRegistrationDAO examRegistrationDAO = (ExamRegistrationDAO) servletCtx.getAttribute("examRegistrationDAO");

		int careerId = identity.getCareerId();
		boolean res = action.execute(examRegistrationDAO, careerId, examId);
		logger.log(Level.FINER, "{0}: Student {1}, exam {2}, {3}: {4}", new Object[]{request.getRemoteHost(), careerId, examId, action, res ? "SUCCESS" : "FAIL"});

		Map<String,Object> params = Map.of("examId", examId);
		HttpUtils.redirectWithParams(request, response, studExamRegPage, params);
	}
}
