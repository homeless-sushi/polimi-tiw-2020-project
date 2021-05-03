package it.polimi.poliesami.filter;

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

import it.polimi.db.dao.ExamDAO;
import it.polimi.poliesami.business.IdentityBean;
import it.polimi.poliesami.utils.AppAuthenticator;
import it.polimi.poliesami.utils.HttpUtils;

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

		String examIdString = req.getParameter("examId");
		
		fail : {
			int examId;
			try {
				examId = Integer.parseInt(examIdString);
			} catch (NumberFormatException e) {
				logger.log(Level.FINER, "{0}: Bad request", req.getRemoteHost());
				break fail;
			}

			ServletContext servletCtx = getServletContext();

			AppAuthenticator clientAuthenticator = (AppAuthenticator) servletCtx.getAttribute("clientAuthenticator");
			IdentityBean identity = clientAuthenticator.getClientIdentity(req);
			ExamDAO examDAO = (ExamDAO) servletCtx.getAttribute("examDAO");
			
			if(!examDAO.isExamCourseAttendee(identity.getCareerId(), examId)) {
				break fail;
			}

			chain.doFilter(req, res);
			return;
		}

		HttpUtils.redirect(req, res, studentExamsPage);
	}
}