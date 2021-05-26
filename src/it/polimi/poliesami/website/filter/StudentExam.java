package it.polimi.poliesami.website.filter;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.poliesami.db.business.ExamBean;
import it.polimi.poliesami.db.dao.ExamDAO;
import it.polimi.poliesami.website.business.IdentityBean;
import it.polimi.poliesami.website.utils.HttpUtils;

public class StudentExam extends HttpFilter {
	private static final Logger logger = Logger.getLogger(StudentExam.class.getName());
	private String studentExamsPage;

	@Override
	public void init(FilterConfig config) throws ServletException {
		super.init(config);

		ServletContext servletCtx = config.getServletContext();
		studentExamsPage = servletCtx.getInitParameter("studentExamsPage");
	}

	@Override
	protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
		throws IOException, ServletException{

		ServletContext servletCtx = getServletContext();
		String examIdString = req.getParameter("examId");
		
		fail : {
			int examId;
			try {
				examId = Integer.parseInt(examIdString);
			} catch (NumberFormatException e) {
				logger.log(Level.FINER, "{0}: Invalid exam Id {1}", new Object[]{req.getRemoteHost(), examIdString});
				break fail;
			}

			IdentityBean identity = (IdentityBean) req.getAttribute("identity");
			
			ExamDAO examDAO = (ExamDAO) servletCtx.getAttribute("examDAO");
			
			ExamBean exam = examDAO.getStudentExamById(examId, identity.getCareer().getId());
			if(exam == null) {
				logger.log(Level.FINER, "{0}: Invalid exam Id {1} for user {2}", new Object[]{req.getRemoteHost(), examId, identity});
				break fail;
			}

			req.setAttribute("exam", exam);

			chain.doFilter(req, res);
			return;
		}

		HttpUtils.redirect(req, res, studentExamsPage);
	}
}
