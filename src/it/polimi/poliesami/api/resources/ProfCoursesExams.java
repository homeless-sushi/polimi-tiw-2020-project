package it.polimi.poliesami.api.resources;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.poliesami.db.business.CareerBean;
import it.polimi.poliesami.db.business.CourseBean;
import it.polimi.poliesami.db.dao.CourseDAO;
import it.polimi.poliesami.db.dao.ExamDAO;

public class ProfCoursesExams extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final ServletContext servletCtx = getServletContext();
		final String yearString = request.getParameter("year");
		final CareerBean career = (CareerBean) request.getAttribute("career");
		
		final CourseDAO courseDAO = (CourseDAO) servletCtx.getAttribute("courseDAO");
		final ExamDAO examDAO = (ExamDAO) servletCtx.getAttribute("examDAO");
		
		int year;
		try {
			year = Integer.parseInt(yearString);
		} catch (NumberFormatException e) {
			year = CourseDAO.getAcademicYear();
		}

		final List<CourseBean> courses = courseDAO.getProfessorCourses(career.getId(), year);
		examDAO.fetchCourseExams(courses);
		request.setAttribute("jsonBody", courses);
		
		final RequestDispatcher jsonDispatcher = getServletContext().getNamedDispatcher("JsonMapper");
		jsonDispatcher.forward(request, response);
	}
}
