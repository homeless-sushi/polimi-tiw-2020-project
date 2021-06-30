package it.polimi.poliesami.api.resources;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.poliesami.db.business.ExamBean;
import it.polimi.poliesami.db.business.ExamRegistrationBean;
import it.polimi.poliesami.db.dao.ExamRegistrationDAO;

public class ProfExamRegistration extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final ServletContext servletCtx = getServletContext();
		final ExamBean exam = (ExamBean) request.getAttribute("exam");

		fail : {
			final int studentId;
			try{
				studentId = Integer.parseInt(request.getParameter("studentId"));
			}catch(NumberFormatException e){
				request.setAttribute("jsonError", new Exception("Invalid student id"));
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				break fail;
			}

			final ExamRegistrationDAO examRegistrationDAO = (ExamRegistrationDAO) servletCtx.getAttribute("examRegistrationDAO");
			final ExamRegistrationBean examRegistration = examRegistrationDAO.getProfessorExamRegistration(studentId, exam.getId());
			if(examRegistration == null) {
				request.setAttribute("jsonError", new Exception("Student exam registration does not exist"));
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				break fail;
			}
		
			request.setAttribute("jsonBody", examRegistration);
		}
		
		final RequestDispatcher jsonDispatcher = getServletContext().getNamedDispatcher("JsonMapper");
		jsonDispatcher.forward(request, response);
	}
}
