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
import it.polimi.poliesami.utils.Direction;

public class ProfExamRegPage extends HttpServlet {
	private String templatePath;

	public enum Column {
		studentId {
			@Override
			public String getField(ExamRegistrationBean registration) {
				return Integer.toString(registration.getCareer().getId());
			}

			@Override
			public String getOrderBy(Direction dir) {
				return "career.id " + dir;
			}
		},
		surname {
			@Override
			public String getField(ExamRegistrationBean registration) {
				return registration.getCareer().getUser().getSurname();
			}

			@Override
			public String getOrderBy(Direction dir) {
				return "user.surname " + dir;
			}
		},
		name {
			@Override
			public String getField(ExamRegistrationBean registration) {
				return registration.getCareer().getUser().getName();
			}

			@Override
			public String getOrderBy(Direction dir) {
				return "user.name " + dir;
			}
		},
		email {
			@Override
			public String getField(ExamRegistrationBean registration) {
				return registration.getCareer().getUser().getEmail();
			}

			@Override
			public String getOrderBy(Direction dir) {
				return "user.email " + dir;
			}
		},
		major {
			@Override
			public String getField(ExamRegistrationBean registration) {
				return registration.getCareer().getMajor();
			}

			@Override
			public String getOrderBy(Direction dir) {
				return "career.major " + dir;
			}
		},
		status {
			@Override
			public String getField(ExamRegistrationBean registration) {
				return registration.getStatus().toString();
			}

			@Override
			public String getOrderBy(Direction dir) {
				return "registration.status " + dir;
			}
		},
		grade {
			@Override
			public String getField(ExamRegistrationBean registration) {
				return registration.getResultRepresentation();
			}

			@Override
			public String getOrderBy(Direction dir) {
				return String.join(", ",
					"registration.result " + dir,
					"registration.grade " + dir,
					"registration.laude " + dir);
			}
		};

		public abstract String getOrderBy(Direction dir);

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
		boolean desc = Boolean.parseBoolean(request.getParameter("desc"));
		Direction dir = desc ? Direction.DESC : Direction.ASC;

		int examId = Integer.parseInt(examIdString);

		ExamDAO examDAO = (ExamDAO) servletCtx.getAttribute("examDAO");
		ExamBean exam = examDAO.getExamById(examId);

		String orderBySql = null;
		if(orderBy != null) try {
			orderBySql = Column.valueOf(orderBy).getOrderBy(dir);
		} catch(IllegalArgumentException e) {
			orderBySql = null;
		}
		
		ExamRegistrationDAO examRegistrationDAO = (ExamRegistrationDAO) servletCtx.getAttribute("examRegistrationDAO");
		List<ExamRegistrationBean> registrations = examRegistrationDAO.getExamRegistrationsByExamId(examId, orderBySql);

		WebContext ctx = new WebContext(request, response, servletCtx, request.getLocale());
		ctx.setVariable("registrations", registrations);
		ctx.setVariable("exam", exam);
		ctx.setVariable("columns", Column.values());
		ctx.setVariable("desc", desc);
		
		TemplateEngine templateEngine = (TemplateEngine) servletCtx.getAttribute("templateEngine");
		templateEngine.process(templatePath, ctx, response.getWriter());
	}
}
