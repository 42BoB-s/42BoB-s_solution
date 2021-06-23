package bobs.Controller;

import bobs.Dto.SessionDto;
import bobs.Service.LoginService;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;


@Controller
public class LoginController {

	private final LoginService loginService;

	@Autowired
	public LoginController(LoginService loginService) {
		this.loginService = loginService;
	}

	@GetMapping("/42OAuth")
	public void OAtuh(HttpServletRequest req, HttpServletResponse rep, @RequestParam(value="code") String code)
	{
		String token = loginService.getOAuthToken(code);
		String user_id = loginService.getUserID(token);
		HttpSession httpSession = loginService.getSession(req, user_id, 1);
		try {
			rep.sendRedirect("http://localhost:8080/main");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@GetMapping("/login")
	public String login()
	{
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
