package it.polimi.poliesami.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import it.polimi.db.business.CourseBean;
import it.polimi.db.business.ExamBean;
import it.polimi.db.dao.CourseDAO;
import it.polimi.db.dao.ExamDAO;
import it.polimi.poliesami.business.CourseExamsBean;
import it.polimi.poliesami.business.IdentityBean;
import it.polimi.poliesami.utils.AppAuthenticator;

public class StudentExamsPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private String templatePath;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		templatePath = getInitParameter("templatePath");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		ServletContext servletCtx = getServletContext();
		
		AppAuthenticator clientAutheticator = (AppAuthenticator) servletCtx.getAttribute("clientAuthenticator");
		IdentityBean identity = clientAutheticator.getClientIdentity(request);
		
		CourseDAO courseDAO = (CourseDAO) servletCtx.getAttribute("courseDAO");
		String yearString = request.getParameter("year");
		int year;
		try {
			year = Integer.parseInt(yearString);
		} catch (NumberFormatException e) {
			year = courseDAO.getAcademicYear();
		}
		List<CourseBean> courses = courseDAO.getStudentCourses(identity.getCareerId(), year);
		List<CourseExamsBean> courseExamsList = new ArrayList<>();
		ExamDAO examDAO = (ExamDAO)	servletCtx.getAttribute("examDAO");
		for(CourseBean course : courses){
			List<ExamBean> exams = examDAO.getCourseExams(course.getId(), year);
			CourseExamsBean courseExams = new CourseExamsBean(course, exams);
			courseExamsList.add(courseExams);
		}

		WebContext ctx = new WebContext(request, response, servletCtx, request.getLocale());
		ctx.setVariable("year", year);
		ctx.setVariable("courseExamsList", courseExamsList);
		
		TemplateEngine templateEngine = (TemplateEngine) servletCtx.getAttribute("templateEngine");
		templateEngine.process(templatePath, ctx, response.getWriter());
	}
}
