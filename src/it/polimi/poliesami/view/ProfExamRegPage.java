package it.polimi.poliesami.view;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.javatuples.Triplet;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import it.polimi.db.business.CareerBean;
import it.polimi.db.business.CourseBean;
import it.polimi.db.business.ExamBean;
import it.polimi.db.business.ExamRegistrationBean;
import it.polimi.db.business.ExamStatus;
import it.polimi.db.business.UserBean;
import it.polimi.db.dao.CourseDAO;
import it.polimi.db.dao.ExamDAO;
import it.polimi.db.dao.ExamRegistrationDAO;

public class ProfExamRegPage extends HttpServlet {
	private String templatePath;

	private Map<String,String> orderBy;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		templatePath = getInitParameter("templatePath");
		
		orderBy = Map.of(
			"studentId", "user_career.id",
			"surname", "user.surname",
			"name", "user.name",
			"email", "user.email",
			"major", "user_career.major",
			"status", "exam_registration.status",
			"grade", "exam_registration.repr"
		);
	}


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext servletCtx = getServletContext();
		String examIdString = request.getParameter("examId");

		WebContext ctx = new WebContext(request, response, servletCtx, request.getLocale());

		int examId = Integer.parseInt(examIdString);
		ctx.setVariable("examId", examId);

		String orderByString = request.getParameter("orderBy");
		if(orderByString == null){ orderByString = "studentId";	}
		ctx.setVariable("orderByString", orderByString);

		String descendingString = request.getParameter("descending");
		boolean descending = Boolean.parseBoolean(descendingString);
		ctx.setVariable("descending", descending);

		ExamDAO examDAO = (ExamDAO) servletCtx.getAttribute("examDAO");
		ExamBean exam = examDAO.getExamById(examId);
		ctx.setVariable("exam", exam);

		CourseDAO courseDAO = (CourseDAO) servletCtx.getAttribute("courseDAO");
		CourseBean course = courseDAO.getCourseFromExam(examId);
		ctx.setVariable("course", course);

		ExamRegistrationDAO examRegistrationDAO = (ExamRegistrationDAO) servletCtx.getAttribute("examRegistrationDAO");
		List<Triplet<UserBean, CareerBean, ExamRegistrationBean>> userCareerExamRegistrations = examRegistrationDAO.getStudentCareerExamRegistrations(examId, orderBy.get(orderByString), descending);
		ctx.setVariable("studCareerExamRegistrations", userCareerExamRegistrations);

		ctx.setVariable("PUB", ExamStatus.PUB);
		ctx.setVariable("RIF", ExamStatus.RIF);
		ctx.setVariable("VERB", ExamStatus.VERB);
		
		TemplateEngine templateEngine = (TemplateEngine) servletCtx.getAttribute("templateEngine");
		templateEngine.process(templatePath, ctx, response.getWriter());
	}
}
