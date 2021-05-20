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

import it.polimi.db.business.UserBean;
import it.polimi.poliesami.utils.AppAuthenticator;
import it.polimi.poliesami.utils.HttpUtils;
import it.polimi.poliesami.view.LoginPage;

public class LoginService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(LoginService.class.getName());
	private String loginPage;
	private String careersPage;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletContext servletCtx = config.getServletContext();
		loginPage = servletCtx.getInitParameter("loginPage");
		careersPage = servletCtx.getInitParameter("careersPage");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpUtils.redirect(request, response, loginPage);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext servletCtx = getServletContext();
		String personCodeString = request.getParameter("person_code");
		String plainPsw = request.getParameter("password");
		boolean allDayLogin = Boolean.parseBoolean(request.getParameter("all_day"));

		fail: {
			if(personCodeString == null || plainPsw == null)
				break fail;

			int personCode;
			try {
				personCode = UserBean.personCodeToInt(personCodeString);
			} catch (IllegalArgumentException e) {
				logger.log(Level.FINER, "{0}: {1}", new Object[]{request.getRemoteHost(), e});
				break fail;
			}

			AppAuthenticator clientAuthenticator = (AppAuthenticator) servletCtx.getAttribute("clientAuthenticator");
			boolean valid = clientAuthenticator.setClientIdentity(request, response, personCode, plainPsw.getBytes(), allDayLogin);
			if(!valid)
				break fail;

			HttpUtils.redirect(request, response, careersPage);
			return;
		}

		request.getSession().setAttribute(LoginPage.ERROR_MSG, LoginPage.ERROR_TYPE.WRONG_CREDENTIALS);
		HttpUtils.redirect(request, response, loginPage);
	}
}
