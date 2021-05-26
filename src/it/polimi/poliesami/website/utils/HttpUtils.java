package it.polimi.poliesami.website.utils;

import java.util.Map;
import java.util.StringJoiner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class HttpUtils {
	private HttpUtils() {}

	public static void redirect(HttpServletRequest request, HttpServletResponse response, String location) {
		String path = request.getContextPath() + location;
		response.setHeader("Location", response.encodeRedirectURL(path));
		response.setStatus(HttpServletResponse.SC_SEE_OTHER);
	}

	public static void redirectWithParams(HttpServletRequest request, HttpServletResponse response, String location, Map<String, Object> params) {
		StringJoiner locationWithParams = new StringJoiner("&", location + "?", "");
		for(Map.Entry<String,Object> param : params.entrySet()) {
			locationWithParams.add(param.getKey() + "=" + param.getValue().toString());
		}

		redirect(request, response, locationWithParams.toString());
	}
}
