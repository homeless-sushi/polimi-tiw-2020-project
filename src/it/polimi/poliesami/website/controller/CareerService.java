package it.polimi.poliesami.website.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.poliesami.db.business.Role;
import it.polimi.poliesami.website.utils.AppAuthenticator;
import it.polimi.poliesami.website.utils.HttpUtils;

public class CareerService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(CareerService.class.getName());
	private String careersPage;
	private String professorExamsPage;
	private String studentExamsPage;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletContext servletCtx = config.getServletContext();
		careersPage = servletCtx.getInitParameter("careersPage");
		professorExamsPage = servletCtx.getInitParameter("professorExamsPage");
		studentExamsPage = servletCtx.getInitParameter("studentExamsPage");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext servletCtx = getServletContext();
		String careerIdString = request.getParameter("careerId");
		String roleString = request.getParameter("role");

		fail: {
			int careerId;
			try {
				careerId = Integer.parseInt(careerIdString);
			} catch (NumberFormatException e) {
				break fail;
			}

			Role careerRole = Role.fromString(roleString);
			if(careerRole == null) {
				break fail;
			}

			AppAuthenticator clientAuthenticator = (AppAuthenticator) servletCtx.getAttribute("clientAuthenticator");
			boolean valid = clientAuthenticator.setClientCareer(request, response, careerId, careerRole);

			if(!valid)
				break fail;

			switch (careerRole) {
				case PROFESSOR:
					HttpUtils.redirect(request, response, professorExamsPage);
					return;
				case STUDENT:
					HttpUtils.redirect(request, response, studentExamsPage);
					return;
			
				default:
			}
		}

		logger.log(Level.FINER, "{0}: Invalid career {1} {2}", new Object[]{request.getRemoteHost(), careerIdString, roleString});
		HttpUtils.redirect(request, response, careersPage);
	}
}
