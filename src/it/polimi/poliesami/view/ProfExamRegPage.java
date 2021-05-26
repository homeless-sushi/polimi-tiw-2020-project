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
import it.polimi.db.dao.ExamRegistrationDAO;
import it.polimi.db.utils.Direction;

public class ProfExamRegPage extends HttpServlet {
	private String templatePath;

	public enum Column {
		studentId {
			@Override
			public String getField(String beanName) {
				return "${" + beanName + ".career.id}";
			}

			@Override
			public String getOrderBy(Direction dir) {
				return "career.id " + dir;
			}
		},
		surname {
			@Override
			public String getField(String beanName) {
				return "${" + beanName + ".career.user.surname}";
			}

			@Override
			public String getOrderBy(Direction dir) {
				return "user.surname " + dir;
			}
		},
		name {
			@Override
			public String getField(String beanName) {
				return "${" + beanName + ".career.user.name}";
			}

			@Override
			public String getOrderBy(Direction dir) {
				return "user.name " + dir;
			}
		},
		email {
			@Override
			public String getField(String beanName) {
				return "${" + beanName + ".career.user.email}";
			}

			@Override
			public String getOrderBy(Direction dir) {
				return "user.email " + dir;
			}
		},
		major {
			@Override
			public String getField(String beanName) {
				return "${" + beanName + ".career.major}";
			}

			@Override
			public String getOrderBy(Direction dir) {
				return "career.major " + dir;
			}
		},
		status {
			@Override
			public String getField(String beanName) {
				return "#{|evaluationInfo.${" + beanName + ".status}|}";
			}

			@Override
			public String getOrderBy(Direction dir) {
				return "registration.status " + dir;
			}
		},
		grade {
			@Override
			public String getField(String beanName) {
				return "${#messages.msgOrNull('evaluationInfo.' + " + beanName + ".resultRepresentation)} ?: ${" + beanName + ".resultRepresentation}";
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

		public abstract String getField(String beanName);
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		templatePath = getInitParameter("templatePath");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext servletCtx = getServletContext();
		String orderBy = request.getParameter("orderBy");
		boolean desc = Boolean.parseBoolean(request.getParameter("desc"));
		ExamBean exam = (ExamBean) request.getAttribute("exam");

		Direction dir = desc ? Direction.DESC : Direction.ASC;

		String orderBySql = null;
		if(orderBy != null) try {
			orderBySql = Column.valueOf(orderBy).getOrderBy(dir);
		} catch(IllegalArgumentException e) {
			orderBySql = null;
		}
		
		ExamRegistrationDAO examRegistrationDAO = (ExamRegistrationDAO) servletCtx.getAttribute("examRegistrationDAO");
		List<ExamRegistrationBean> registrations = examRegistrationDAO.getExamRegistrationsByExamId(exam.getId(), orderBySql);

		WebContext ctx = new WebContext(request, response, servletCtx, request.getLocale());
		ctx.setVariable("registrations", registrations);
		ctx.setVariable("columns", Column.values());
		ctx.setVariable("desc", desc);
		
		TemplateEngine templateEngine = (TemplateEngine) servletCtx.getAttribute("templateEngine");
		templateEngine.process(templatePath, ctx, response.getWriter());
	}
}
