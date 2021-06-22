package it.polimi.poliesami.api.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.poliesami.db.business.CareerBean;
import it.polimi.poliesami.db.business.ExamBean;
import it.polimi.poliesami.db.dao.ExamDAO;

public class StudentExam extends HttpFilter {

	@Override
	protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
		throws IOException, ServletException{

		final ServletContext servletCtx = getServletContext();
		final String examIdString = req.getParameter("examId");
		
		fail : {
			int examId;
			try {
				examId = Integer.parseInt(examIdString);
			} catch (NumberFormatException e) {
				req.setAttribute("jsonError", new Exception("Param examId must be a number"));
				break fail;
			}

			final CareerBean career = (CareerBean) req.getAttribute("career");			
			final ExamDAO examDAO = (ExamDAO) servletCtx.getAttribute("examDAO");			
			ExamBean exam = examDAO.getStudentExamById(examId, career.getId());
			if(exam == null) {
				req.setAttribute("jsonError", new Exception("Exam not found"));
				break fail;
			}

			req.setAttribute("exam", exam);
			chain.doFilter(req, res);
			return;
		}

		res.setStatus(HttpServletResponse.SC_NOT_FOUND);
		RequestDispatcher jsonDispatcher = getServletContext().getNamedDispatcher("JsonMapper");
		jsonDispatcher.forward(req, res);
	}
}
