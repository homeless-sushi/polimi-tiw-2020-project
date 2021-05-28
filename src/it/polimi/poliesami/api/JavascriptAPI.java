package it.polimi.poliesami.api;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

public class JavascriptAPI extends HttpServlet {
	private String templatePath;
	private String apiURI;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		templatePath = getInitParameter("templatePath");

		ServletContext servletCtx = getServletContext();
		apiURI = servletCtx.getInitParameter("apiURI");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext servletCtx = getServletContext();

		response.setContentType("text/javascript");

		WebContext ctx = new WebContext(request, response, servletCtx, request.getLocale());
		ctx.setVariable("baseURL", "http://" + request.getHeader("Host") + "/" + request.getContextPath() + apiURI);

		TemplateEngine templateEngine = (TemplateEngine) servletCtx.getAttribute("jsTemplateEngine");
		templateEngine.process(templatePath, ctx, response.getWriter());
	}
}
