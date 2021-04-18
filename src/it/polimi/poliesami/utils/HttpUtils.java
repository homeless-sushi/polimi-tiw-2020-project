package it.polimi.poliesami.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class HttpUtils {
	private HttpUtils() {}

	public static void redirect(HttpServletRequest request, HttpServletResponse response, String location) {
		String path = request.getContextPath() + location;
		response.setHeader("Location", response.encodeRedirectURL(path));
		response.setStatus(HttpServletResponse.SC_SEE_OTHER);
	}
}
