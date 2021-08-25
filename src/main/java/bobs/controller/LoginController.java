package bobs.controller;

import bobs.dao.classes.JdbcActivityLogDao;
import bobs.dto.ActivityLogDto;
import bobs.dto.SessionDto;
import bobs.propInjector.PropInjector;
import bobs.service.LoginService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;



@Controller
@RequiredArgsConstructor
public class LoginController {

	private final LoginService loginService;
	private final PropInjector propInject;
	private final JdbcActivityLogDao activityLogDao;

	@GetMapping("/42OAuth")
	public void OAtuh(HttpServletRequest req, HttpServletResponse rep, @RequestParam(value="code") String code) {
		String token = loginService.getOAuthToken(code);
		SessionDto sessionDto = loginService.getUserInfo(token);
		//임시
		sessionDto.setLocation_id(1);
		HttpSession httpSession = loginService.getSession(req, sessionDto);
		try {
			activityLogDao.create(new ActivityLogDto().getLoginLog(sessionDto));
			rep.sendRedirect(propInject.getBaseUrl() + "main");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/logout")
	public String logout(HttpSession httpSession)
	{
		if (httpSession != null)
			loginService.destorySession(httpSession);
		return "login";
	}
}
