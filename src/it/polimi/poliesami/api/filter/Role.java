package it.polimi.poliesami.api.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.poliesami.api.identity.Identity;
import it.polimi.poliesami.db.business.CareerBean;

public class Role extends HttpFilter {

	private it.polimi.poliesami.db.business.Role acceptRole;

	@Override
	public void init(FilterConfig config) throws ServletException {
		super.init(config);

		acceptRole = it.polimi.poliesami.db.business.Role.fromString(config.getInitParameter("acceptRole"));
	}

	@Override
	protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		fail : {
			Identity identity = (Identity) request.getAttribute("identity");

			String careerIdString = request.getParameter("careerId");
			int careerId;
			try {	
				careerId = Integer.parseInt(careerIdString);
			}catch(NumberFormatException e){
				request.setAttribute("jsonError", new Exception("Param careerId must be a number"));
				break fail;
			}

			if(!identity.hasCareer(careerId, acceptRole)){ 
				request.setAttribute("jsonError", new Exception("Career not found"));
				break fail; 
			}

			CareerBean career = new CareerBean();
			career.setId(careerId);
			career.setRole(acceptRole);
			request.setAttribute("career", career);

			chain.doFilter(request, response);
			return;
		}

		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		RequestDispatcher jsonDispatcher = getServletContext().getNamedDispatcher("JsonMapper");
		jsonDispatcher.forward(request, response);
	}
}
