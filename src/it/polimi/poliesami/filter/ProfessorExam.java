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

import it.polimi.db.business.ExamBean;
import it.polimi.db.dao.ExamDAO;
import it.polimi.poliesami.business.IdentityBean;
import it.polimi.poliesami.utils.AppAuthenticator;
import it.polimi.poliesami.utils.HttpUtils;

public class ProfessorExam extends HttpFilter {
	private static final Logger logger = Logger.getLogger(ProfessorExam.class.getName());
	private String professorExamsPage;

	@Override
	public void init(FilterConfig config) throws ServletException {
		super.init(config);

		ServletContext servletCtx = config.getServletContext();
		professorExamsPage = servletCtx.getInitParameter("professorExamsPage");
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

			AppAuthenticator clientAuthenticator = (AppAuthenticator) servletCtx.getAttribute("clientAuthenticator");
			IdentityBean identity = clientAuthenticator.getClientIdentity(req);
			
			ExamDAO examDAO = (ExamDAO) servletCtx.getAttribute("examDAO");

			ExamBean exam = examDAO.getProfessorExamById(examId, identity.getCareerId());
			if(exam == null) {
				logger.log(Level.FINER, "{0}: Invalid exam Id {1} for user {2}", new Object[]{req.getRemoteHost(), examId, identity});
				break fail;
			}

			req.setAttribute("exam", exam);

			chain.doFilter(req, res);
			return;
		}

		HttpUtils.redirect(req, res, professorExamsPage);
	}
}
