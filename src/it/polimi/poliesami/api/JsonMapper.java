package it.polimi.poliesami.api;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonMapper extends HttpServlet {
	private ObjectMapper jsonObjectMapper;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		jsonObjectMapper = new ObjectMapper()
			.addMixIn(Throwable.class, ThrowableMixIn.class)
			.findAndRegisterModules()
			.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		process(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		process(request, response);
	}

	private void process(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/json");


		boolean pretty = Boolean.parseBoolean(request.getParameter("pretty"));
		ObjectWriter jsonObjectWriter = pretty ?
			jsonObjectMapper.writerWithDefaultPrettyPrinter() :
			jsonObjectMapper.writer();
		JsonResponse json = new JsonResponse();
		json.data = request.getAttribute("jsonBody");
		json.error = request.getAttribute("jsonError");
		jsonObjectWriter.writeValue(response.getWriter(), json);
	}
}

@JsonAutoDetect(getterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE)
@JsonPropertyOrder({"message", "cause"})
interface ThrowableMixIn {
	@JsonGetter
	String getMessage();
	@JsonInclude(Include.NON_NULL)
	@JsonGetter
	String getCause();
}

@JsonInclude(Include.NON_NULL)
class JsonResponse {
	public Object error;
	public Object data;
}
