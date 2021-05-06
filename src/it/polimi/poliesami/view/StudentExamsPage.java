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
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import it.polimi.db.business.CourseBean;
import it.polimi.db.business.ExamBean;
import it.polimi.db.dao.CourseDAO;
import it.polimi.db.dao.ExamDAO;
import it.polimi.poliesami.business.CourseExamsBean;
import it.polimi.poliesami.business.IdentityBean;

public class StudentExamsPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private String templatePath;
	private String studExamRegPage;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		templatePath = getInitParameter("templatePath");

		ServletContext servletCtx = config.getServletContext();
		studExamRegPage = servletCtx.getInitParameter("studExamRegPage");		
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		ServletContext servletCtx = getServletContext();
		
		HttpSession session = request.getSession();
		IdentityBean identity = (IdentityBean) session.getAttribute("identity");
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
		ctx.setVariable("studExamRegPage", studExamRegPage);
		ctx.setVariable("courseExamsList", courseExamsList);
		
		TemplateEngine templateEngine = (TemplateEngine) servletCtx.getAttribute("templateEngine");
		templateEngine.process(templatePath, ctx, response.getWriter());
	}
}
