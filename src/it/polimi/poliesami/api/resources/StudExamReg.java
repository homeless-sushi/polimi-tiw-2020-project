package it.polimi.poliesami.api.resources;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.poliesami.db.business.CareerBean;
import it.polimi.poliesami.db.business.ExamBean;
import it.polimi.poliesami.db.business.ExamRegistrationBean;
import it.polimi.poliesami.db.dao.ExamRegistrationDAO;

import it.polimi.poliesami.api.NullableData;

public class StudExamReg extends HttpServlet {

	public enum ACTION {
		REGISTER("register") {
			@Override
			public boolean execute(ExamRegistrationDAO dao, int careerId, int examId) {
				return dao.registerToExam(careerId, examId);
			}
		},
		DEREGISTER("deregister") {
			@Override
			public boolean execute(ExamRegistrationDAO dao, int careerId, int examId) {
				return dao.deregisterFromExam(careerId, examId);
			}
		},
		REJECT("reject") {
			@Override
			public boolean execute(ExamRegistrationDAO dao, int careerId, int examId) {
				return dao.rejectStudExam(careerId, examId);
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
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final ServletContext servletCtx = getServletContext();
		final CareerBean career = (CareerBean) request.getAttribute("career");
		final ExamBean exam = (ExamBean) request.getAttribute("exam");

		final ExamRegistrationDAO examRegistrationDAO = (ExamRegistrationDAO) servletCtx.getAttribute("examRegistrationDAO");
		final ExamRegistrationBean examRegistration = examRegistrationDAO.getStudentExamRegistration(career.getId(), exam.getId());
		
		request.setAttribute("jsonBody", new NullableData(examRegistration));		
		RequestDispatcher jsonDispatcher = getServletContext().getNamedDispatcher("JsonMapper");
		jsonDispatcher.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		ServletContext servletCtx = getServletContext();
		final CareerBean career = (CareerBean) request.getAttribute("career");
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
		final boolean result = action.execute(examRegistrationDAO, career.getId(), exam.getId());
		
		request.setAttribute("jsonBody", result);
		RequestDispatcher jsonDispatcher = getServletContext().getNamedDispatcher("JsonMapper");
		jsonDispatcher.forward(request, response);
	}
}
