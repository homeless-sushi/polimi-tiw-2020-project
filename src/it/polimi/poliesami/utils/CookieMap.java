package it.polimi.poliesami.utils;

import java.util.Map;
import java.util.Hashtable;

import javax.servlet.http.Cookie;

public class CookieMap {
	private Map<String, String> cookieMap;
	
	public CookieMap(Cookie[] cookies) {
		cookieMap = new Hashtable<>();
		
		if(cookies == null) {
			return;
		}
		
		for(Cookie cookie : cookies) {
			cookieMap.put(cookie.getName(), cookie.getValue());
		}
	}
	
	public String get(String name) {
		return cookieMap.get(name);
	}
}
