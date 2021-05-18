package it.polimi.poliesami.filter;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.db.business.ExamBean;
import it.polimi.db.business.ExamRegistrationBean;
import it.polimi.db.dao.ExamRegistrationDAO;
import it.polimi.poliesami.utils.HttpUtils;

public class ExamEdit extends HttpFilter {
	private static final Logger logger = Logger.getLogger(ExamEdit.class.getName());
	private String profExamRegPage;

	@Override
	public void init(FilterConfig config) throws ServletException {
		super.init(config);

		ServletContext servletCtx = config.getServletContext();
		profExamRegPage = servletCtx.getInitParameter("profExamRegPage");
	}

	@Override
	protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
		throws IOException, ServletException{

		final ServletContext servletCtx = getServletContext();
		final String studentIdString = req.getParameter("studentId");

		final ExamBean exam = (ExamBean) req.getAttribute("exam");
		final int examId = exam.getId();

		fail : {
			int studentId;
			try {
				studentId = Integer.parseInt(studentIdString);
			} catch (NumberFormatException e) {
				logger.log(Level.FINER, "{0}: Invalid student ID Id {1}", new Object[]{req.getRemoteHost(), studentIdString});
				break fail;
			}

			final ExamRegistrationDAO examRegistrationDAO = (ExamRegistrationDAO) servletCtx.getAttribute("examRegistrationDAO");
			final ExamRegistrationBean examRegistration = examRegistrationDAO.getProfessorExamRegistration(studentId, examId);
			if(examRegistration == null) {
				logger.log(Level.FINER, "{0}: Invalid exam Id {1} for student {2}", new Object[]{req.getRemoteHost(), examId, studentIdString});
				break fail;
			}

			req.setAttribute("examRegistration", examRegistration);

			chain.doFilter(req, res);
			return;
		}

		Map<String,Object> params = Map.of("examId", examId);
		HttpUtils.redirectWithParams(req, res, profExamRegPage, params);
	}
}
