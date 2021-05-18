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

import it.polimi.poliesami.business.IdentityBean;
import it.polimi.poliesami.utils.AppAuthenticator;
import it.polimi.poliesami.utils.HttpUtils;

public class LoginInside extends HttpFilter {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(LoginInside.class.getName());
	private String loginPage;

	@Override
	public void init(FilterConfig config) throws ServletException {
		super.init(config);
		ServletContext servletCtx = config.getServletContext();
		loginPage = servletCtx.getInitParameter("loginPage");
	}

	@Override
	public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
		throws IOException, ServletException {
		
		ServletContext servletCtx = getServletContext();
		
		AppAuthenticator clientAuthenticator = (AppAuthenticator) servletCtx.getAttribute("clientAuthenticator");
		IdentityBean identity = clientAuthenticator.getClientIdentity(req);
		
		if(identity == null) {
			logger.log(Level.FINER, "{0}: Not logged in", req.getRemoteHost());
			HttpUtils.redirect(req, res, loginPage);
			return;
		}

		req.setAttribute("identity", identity);

		chain.doFilter(req, res);
	}
}
