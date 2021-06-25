package it.polimi.poliesami.api.resources;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.poliesami.api.NullableData;
import it.polimi.poliesami.db.business.ExamBean;
import it.polimi.poliesami.db.business.ExamRegistrationBean;
import it.polimi.poliesami.db.dao.ExamRegistrationDAO;

public class ProfExamRegistrations extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final ServletContext servletCtx = getServletContext();
		final ExamBean exam = (ExamBean) request.getAttribute("exam");
		
		final ExamRegistrationDAO examRegistrationDAO = (ExamRegistrationDAO) servletCtx.getAttribute("examRegistrationDAO");
		final List<ExamRegistrationBean> registrations = examRegistrationDAO.getExamRegistrationsByExamId(exam.getId(), null);
		request.setAttribute("jsonBody", registrations);
		
		request.setAttribute("jsonBody", new NullableData(registrations));
		final RequestDispatcher jsonDispatcher = getServletContext().getNamedDispatcher("JsonMapper");
		jsonDispatcher.forward(request, response);
	}
}
