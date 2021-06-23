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
}
