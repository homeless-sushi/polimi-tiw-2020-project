package it.polimi.poliesami.website.filter;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.FilterConfig;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.poliesami.website.business.IdentityBean;
import it.polimi.poliesami.website.utils.AppAuthenticator;
import it.polimi.poliesami.website.utils.HttpUtils;


public class LoginOutside extends HttpFilter {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(LoginOutside.class.getName());
	private String careersPage;

	@Override
	public void init(FilterConfig config) throws ServletException {
		super.init(config);
		ServletContext servletCtx = config.getServletContext();
		careersPage = servletCtx.getInitParameter("careersPage");
	}

	@Override
	protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
		throws IOException, ServletException{
		
		ServletContext servletCtx = getServletContext();
		
		AppAuthenticator clientAuthenticator = (AppAuthenticator) servletCtx.getAttribute("clientAuthenticator");
		IdentityBean identity = clientAuthenticator.getClientIdentity(req);
		
		if(identity != null) {
			logger.log(Level.FINER, "{0}: Already logged in", req.getRemoteHost());
			HttpUtils.redirect(req, res, careersPage);
			return;
		}

		chain.doFilter(req, res);
	}
}
