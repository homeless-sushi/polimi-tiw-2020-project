package it.polimi.poliesami.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.polimi.db.business.Role;
import it.polimi.db.dao.CareerDAO;
import it.polimi.poliesami.business.IdentityBean;
import it.polimi.poliesami.utils.AppAuthenticator;
import it.polimi.poliesami.utils.HttpUtils;

public class CareerService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(CareerService.class.getName());
	private String careersPage;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletContext servletCtx = config.getServletContext();
		careersPage = servletCtx.getInitParameter("careersPage");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

			HttpSession session = request.getSession();
			IdentityBean identity = (IdentityBean) session.getAttribute("identity");
			if(identity == null) {
				break fail;
			}

			ServletContext servletCtx = getServletContext();
			CareerDAO careerDAO = (CareerDAO) servletCtx.getAttribute("careerDAO");
			if(!careerDAO.validCareer(identity.getPersonCode(), careerId, careerRole)) {
				break fail;
			}

			identity.setCareerId(careerId);
			identity.setRole(careerRole);
			
			logger.log(Level.FINER, "{0}: Selected career {1} {2}", new Object[]{request.getRemoteHost(), careerId, careerRole});

			AppAuthenticator clientAuthenticator = (AppAuthenticator) servletCtx.getAttribute("clientAuthenticator");
			clientAuthenticator.setClientIdentity(request, response, identity);

			switch (careerRole) {
				case PROFESSOR:
					HttpUtils.redirect(request, response, "/inside/professor/");
					return;
				case STUDENT:
					HttpUtils.redirect(request, response, "/inside/student/");
					return;
			
				default:
			}
		}

		logger.log(Level.FINER, "{0}: Invalid career {1} {2}", new Object[]{request.getRemoteHost(), careerIdString, roleString});
		HttpUtils.redirect(request, response, careersPage);
	}
}