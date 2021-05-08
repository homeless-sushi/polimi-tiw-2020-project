package it.polimi.poliesami.view;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

public class LoginPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String ERROR_MSG = "LoginPageServlet__ERROR_MSG";

	private String templatePath;

	public enum ERROR_TYPES {
		WRONG_CREDENTIALS ("login.ERROR_WRONG_CREDENTIALS"),
		MUST_LOG_IN ("login.ERROR_MUST_LOG_IN");
		
		private final String errorType;
		
		ERROR_TYPES(String errorType) { this.errorType = errorType; }
		
		public String getErrorType() { return this.errorType; }
	}
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		templatePath = getInitParameter("templatePath");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext servletCtx = getServletContext();
		HttpSession session = request.getSession();
		
		WebContext ctx = new WebContext(request, response, servletCtx, request.getLocale());
		ctx.setVariable("errorMsg", LoginPage.ERROR_MSG);
		
		TemplateEngine templateEngine = (TemplateEngine) servletCtx.getAttribute("templateEngine");
		templateEngine.process(templatePath, ctx, response.getWriter());
		
		session.removeAttribute(LoginPage.ERROR_MSG);
	}
}
