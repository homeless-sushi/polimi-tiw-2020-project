package it.polimi.poliesami.api.service;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.poliesami.db.business.ExamBean;
import it.polimi.poliesami.db.business.ExamRegistrationBean;
import it.polimi.poliesami.db.business.ExamResult;
import it.polimi.poliesami.db.business.ExamStatus;
import it.polimi.poliesami.db.dao.ExamRegistrationDAO;

public class ProfExamRegEditService extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final ServletContext servletCtx = getServletContext();
		final ExamBean exam = (ExamBean) request.getAttribute("exam");

		ExamRegistrationBean examEval = new ExamRegistrationBean();
		examEval.setExamId(exam.getId());
		fail : {
			final String[] studentIdsString = request.getParameterValues("studentIds");
			final int[] studentIds;
			try{
				studentIds = Arrays.stream(studentIdsString).mapToInt(Integer::parseInt).toArray();
			}catch(NumberFormatException e){
				request.setAttribute("jsonError", new Exception("Invalid student id"));
				break fail;
			}
			final ExamResult examResult;
			try {
				examResult = ExamResult.valueOf(request.getParameter("examResult"));
			} catch (NullPointerException | IllegalArgumentException e) {
				request.setAttribute("jsonError", new Exception("Invalid exam result"));
				break fail;
			}

			final ExamStatus examStatus = examResult == ExamResult.VUOTO ? ExamStatus.NINS : ExamStatus.INS;

			final int grade;
			try {
				grade = Integer.parseInt(request.getParameter("grade"));
			} catch (NumberFormatException e) {
				request.setAttribute("jsonError", new Exception("Invalid grade"));
				break fail;
			}

			final boolean laude = Boolean.parseBoolean(request.getParameter("laude"));
			
			examEval.setResult(examResult);
			examEval.setStatus(examStatus);
			examEval.setGrade(grade);
			examEval.setLaude(laude);

			final ExamRegistrationDAO examRegistrationDAO 
				= (ExamRegistrationDAO) servletCtx.getAttribute("examRegistrationDAO");
			final boolean result = examRegistrationDAO.editExamEvals(studentIds, examEval);			

			request.setAttribute("jsonBody", result);
			RequestDispatcher jsonDispatcher = getServletContext().getNamedDispatcher("JsonMapper");
			jsonDispatcher.forward(request, response);
			return;
		}
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		RequestDispatcher jsonDispatcher = getServletContext().getNamedDispatcher("JsonMapper");
		jsonDispatcher.forward(request, response);
	}
}
