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

import it.polimi.poliesami.db.business.CareerBean;
import it.polimi.poliesami.website.business.IdentityBean;
import it.polimi.poliesami.website.utils.HttpUtils;

public class Role extends HttpFilter {
	private static final Logger logger = Logger.getLogger(Role.class.getName());
	private it.polimi.poliesami.db.business.Role acceptRole;
	private String careersPage;

	@Override
	public void init(FilterConfig config) throws ServletException {
		super.init(config);

		acceptRole = it.polimi.poliesami.db.business.Role.fromString(config.getInitParameter("acceptRole"));

		ServletContext servletCtx = config.getServletContext();
		careersPage = servletCtx.getInitParameter("careersPage");
	}

	@Override
	protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
		throws IOException, ServletException{
		
		IdentityBean identity = (IdentityBean) req.getAttribute("identity");
		CareerBean career = identity.getCareer();

		if(career == null) {
			logger.log(Level.FINER, "{0}: No career selected", req.getRemoteHost());
			HttpUtils.redirect(req, res, careersPage);
			return;
		}
		if(career.getRole() != acceptRole) {
			logger.log(Level.FINER, "{0}: A {1} cannot access this page", new Object[]{req.getRemoteHost(), career.getRole()});
			HttpUtils.redirect(req, res, careersPage);
			return;
		}

		chain.doFilter(req, res);
	}
}
