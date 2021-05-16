package it.polimi.poliesami.view;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import it.polimi.db.business.ExamBean;
import it.polimi.db.business.ExamRegistrationBean;
import it.polimi.db.dao.ExamDAO;
import it.polimi.db.dao.ExamRegistrationDAO;

public class ProfExamRegPage extends HttpServlet {
	private String templatePath;

	public enum Column {
		studentId("career.id") {
			@Override
			public String getField(ExamRegistrationBean registration) {
				return Integer.toString(registration.getCareer().getId());
			}
		},
		surname("user.surname") {
			@Override
			public String getField(ExamRegistrationBean registration) {
				return registration.getCareer().getUser().getSurname();
			}
		},
		name("user.name") {
			@Override
			public String getField(ExamRegistrationBean registration) {
				return registration.getCareer().getUser().getName();
			}
		},
		email("user.email") {
			@Override
			public String getField(ExamRegistrationBean registration) {
				return registration.getCareer().getUser().getEmail();
			}
		},
		major("career.major") {
			@Override
			public String getField(ExamRegistrationBean registration) {
				return registration.getCareer().getMajor();
			}
		},
		status("registration.status") {
			@Override
			public String getField(ExamRegistrationBean registration) {
				return registration.getStatus().toString();
			}
		},
		grade("registration.repr") {
			@Override
			public String getField(ExamRegistrationBean registration) {
				return registration.getResultRepresentation();
			}
		};
		
		private String sqlName;
		
		private Column(String sqlName) {
			this.sqlName = sqlName;
		}

		public String getSqlName() {
			return sqlName;
		}

		public abstract String getField(ExamRegistrationBean registration);

	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		templatePath = getInitParameter("templatePath");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext servletCtx = getServletContext();
		String examIdString = request.getParameter("examId");
		String orderBy = request.getParameter("orderBy");
		boolean desc = request.getParameter("desc") != null;

		int examId = Integer.parseInt(examIdString);

		ExamDAO examDAO = (ExamDAO) servletCtx.getAttribute("examDAO");
		ExamBean exam = examDAO.getExamById(examId);

		ExamRegistrationDAO examRegistrationDAO = (ExamRegistrationDAO) servletCtx.getAttribute("examRegistrationDAO");
		Column orderByCol;
		try {
			orderByCol = Column.valueOf(orderBy);
		} catch(NullPointerException | IllegalArgumentException e) {
			orderByCol = Column.studentId;
		}
		List<ExamRegistrationBean> registrations = examRegistrationDAO.getExamRegistrationsByExamId(examId, orderByCol.getSqlName(), desc);

		WebContext ctx = new WebContext(request, response, servletCtx, request.getLocale());
		ctx.setVariable("registrations", registrations);
		ctx.setVariable("exam", exam);
		ctx.setVariable("columns", Column.values());
		ctx.setVariable("desc", desc);
		
		TemplateEngine templateEngine = (TemplateEngine) servletCtx.getAttribute("templateEngine");
		templateEngine.process(templatePath, ctx, response.getWriter());
	}
}
