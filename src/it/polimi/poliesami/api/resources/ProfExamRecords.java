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
import it.polimi.poliesami.db.business.ExamRecordBean;
import it.polimi.poliesami.db.dao.ExamRecordDAO;
import it.polimi.poliesami.db.dao.ExamRegistrationDAO;


public class ProfExamRecords extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final ServletContext servletCtx = getServletContext();
		final ExamBean exam = (ExamBean) request.getAttribute("exam");

		final ExamRecordDAO examRecordDAO = (ExamRecordDAO) servletCtx.getAttribute("examRecordDAO");
		final ExamRegistrationDAO examRegistrationDAO = (ExamRegistrationDAO) servletCtx.getAttribute("examRegistrationDAO");

		final List<ExamRecordBean> records = examRecordDAO.getExamRecords(exam.getId());
		examRegistrationDAO.fetchRecordRegistrations(records);

		request.setAttribute("jsonBody", new NullableData(records));		
		final RequestDispatcher jsonDispatcher = getServletContext().getNamedDispatcher("JsonMapper");
		jsonDispatcher.forward(request, response);
	}
}

