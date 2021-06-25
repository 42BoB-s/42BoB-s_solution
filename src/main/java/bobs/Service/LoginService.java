package bobs.Service;

import bobs.Dto.SessionDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public interface LoginService {
	String getOAuthToken(String code);
	String getUserID(String token);
	HttpSession getSession(HttpServletRequest req, String user_id, int location_id);
	void    destorySession(HttpSession httpSession);
}
