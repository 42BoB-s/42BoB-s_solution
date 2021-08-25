package bobs.service;

import bobs.dto.SessionDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public interface LoginService {
	// 42OAuth 토큰을 받아오는 서비스
	String getOAuthToken(String code);
	// 42OAuth 토큰을 이용하여 사용자 계정정보 가져오는 서비스
	// 현재 SessionDto의 객체를 new 연산자를 이용하여 생성중, 추후 수정 필요
	SessionDto getUserInfo(String token);
	// 세션 얻어오는 서비스
	HttpSession getSession(HttpServletRequest req, SessionDto sessionDto);
	// 세션 파기하는 서비스
	void    destorySession(HttpSession httpSession);
}
