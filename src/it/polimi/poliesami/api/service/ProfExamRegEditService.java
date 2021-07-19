package it.polimi.poliesami.api.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

		final List<ExamRegistrationBean> examEvaluations = new ArrayList<>();
		fail : {
			final String[] evaluationStrings = request.getParameterValues("evaluations");

			if(evaluationStrings == null){
				break fail;
			}

			for(final String evaluationString : evaluationStrings){

				final String[] evaluationStringSplit = evaluationString.split(",", 4);
				if(evaluationStringSplit.length != 4){
					break fail;
				}

				final int studentId;
				try{
					studentId = Integer.parseInt(evaluationStringSplit[0]);
				}catch(NumberFormatException e){
					request.setAttribute("jsonError", new Exception("Invalid student id"));
					break fail;
				}
				final ExamResult examResult;
				try {
					examResult = ExamResult.valueOf(evaluationStringSplit[1]);
				} catch (NullPointerException | IllegalArgumentException e) {
					request.setAttribute("jsonError", new Exception("Invalid exam result"));
					break fail;
				}

				final ExamStatus examStatus = examResult == ExamResult.VUOTO ? ExamStatus.NINS : ExamStatus.INS;

				final int grade;
				try {
					grade = Integer.parseInt(evaluationStringSplit[2]);
				} catch (NumberFormatException e) {
					request.setAttribute("jsonError", new Exception("Invalid grade"));
					break fail;
				}

				final boolean laude = Boolean.parseBoolean(evaluationStringSplit[3]);

				final ExamRegistrationBean examEval = new ExamRegistrationBean();
				examEval.setExamId(exam.getId());
				examEval.setStudentId(studentId);			
				examEval.setResult(examResult);
				examEval.setStatus(examStatus);
				examEval.setGrade(grade);
				examEval.setLaude(laude);

				examEvaluations.add(examEval);
			}


			final ExamRegistrationDAO examRegistrationDAO 
				= (ExamRegistrationDAO) servletCtx.getAttribute("examRegistrationDAO");
			final boolean result = examRegistrationDAO.editExamEvals(examEvaluations);			

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
