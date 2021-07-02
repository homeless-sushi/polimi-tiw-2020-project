package it.polimi.poliesami.api.resources;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.poliesami.db.business.ExamBean;
import it.polimi.poliesami.db.business.ExamRegistrationBean;
import it.polimi.poliesami.db.dao.ExamRegistrationDAO;

public class ProfExamRegistrations extends HttpServlet {
	public enum ACTION {
		PUBLISH("publish") {
			@Override
			public boolean execute(ExamRegistrationDAO dao, int examId) {
				return dao.publishExamEval(examId);
			}
		},
		VERBALIZE("verbalize") {
			@Override
			public boolean execute(ExamRegistrationDAO dao, int examId) {
				return dao.verbalizeExamEval(examId);
			}
		};

		private ACTION(String action){ this.string = action; }
	
		private String string;

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

		public abstract boolean execute(ExamRegistrationDAO dao, int examId);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final ServletContext servletCtx = getServletContext();
		final ExamBean exam = (ExamBean) request.getAttribute("exam");
		
		final ExamRegistrationDAO examRegistrationDAO = (ExamRegistrationDAO) servletCtx.getAttribute("examRegistrationDAO");
		final List<ExamRegistrationBean> registrations = examRegistrationDAO.getExamRegistrationsByExamId(exam.getId(), null);
		request.setAttribute("jsonBody", registrations);
		
		request.setAttribute("jsonBody", registrations);
		final RequestDispatcher jsonDispatcher = getServletContext().getNamedDispatcher("JsonMapper");
		jsonDispatcher.forward(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException {
		final ServletContext servletCtx = getServletContext();
		final ExamBean exam = (ExamBean) request.getAttribute("exam");

		final ACTION action = ACTION.fromString(request.getParameter("action"));
		if(action == null) {
			request.setAttribute("jsonError", new Exception("Invalid action"));
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			RequestDispatcher jsonDispatcher = getServletContext().getNamedDispatcher("JsonMapper");
			jsonDispatcher.forward(request, response);
			return;
		}

		final ExamRegistrationDAO examRegistrationDAO = (ExamRegistrationDAO) servletCtx.getAttribute("examRegistrationDAO");
		final boolean result = action.execute(examRegistrationDAO, exam.getId());

		request.setAttribute("jsonBody", result);
		RequestDispatcher jsonDispatcher = getServletContext().getNamedDispatcher("JsonMapper");
		jsonDispatcher.forward(request, response);
	}
}
