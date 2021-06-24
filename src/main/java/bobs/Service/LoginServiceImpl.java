package bobs.Service;

import bobs.Dto.SessionDto;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.nio.charset.Charset;

@Service
public class LoginServiceImpl implements LoginService {

	@Override
	public String getOAuthToken(String code) {

		System.out.println("Authorization Code======>{}" + code);

		String uid = "629bdab8a98df03b4e7a38c0bbc9a9d5697a0964b0f21413def813faa8125917";
		String secret = "";
		String redirect_uri = "http://localhost:8080/42OAuth";

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> request = new HttpEntity<String>(headers);
		ResponseEntity<String> response = null;

		String accessTokenUrl = "https://api.intra.42.fr/oauth/token"
								+ "?grant_type=authorization_code"
		                        + "&client_id=" + uid
		                        + "&client_secret=" +secret
								+ "&code=" + code
								+ "&redirect_uri=" + redirect_uri;

		response = restTemplate.exchange(accessTokenUrl, HttpMethod.POST, request, String.class);

		//parse를 쓰지 않고, dto로 넘길수도 있다는 모양. (추후 받아올 데이터가 늘어나면 변경예정)
		JSONParser parser = new JSONParser();
		String access_token = null;
		try{
			Object obj = parser.parse(response.getBody());
			JSONObject jsonObj = (JSONObject) obj;
			access_token = (String)jsonObj.get("access_token");
		}catch (Exception e)
		{
			e.printStackTrace();
		}

		//System.out.println("Access Token Response======>{}" + response.getBody());
		//System.out.println("Access Token======>{}" + access_token);

		return access_token;
	}

	@Override
	public String getUserID(String token) {

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+ token);
		headers.add("Content-Type", "application/json; charset=utf-8");
		HttpEntity<String> request = new HttpEntity<String>(headers);
		ResponseEntity<String> response = null;

		String getInfoUrl = "https://api.intra.42.fr/v2/me";

		response = restTemplate.exchange(getInfoUrl, HttpMethod.GET, request, String.class);

		System.out.println("INFO Response======>{}" + response.getBody());

		//json parse 필요
		JSONParser parser = new JSONParser();
		String login_id = null;
		try{
			Object obj = parser.parse(response.getBody());
			JSONObject jsonObj = (JSONObject) obj;
			login_id = (String)jsonObj.get("login");
		}catch (Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("user_id : " + login_id);
		return login_id;
	}

	@Override
	public HttpSession getSession(HttpServletRequest req, String user_id, int location_id) {

		HttpSession session = req.getSession();
		SessionDto sessionDto = (SessionDto)session.getAttribute("session");

		if (sessionDto == null)
		{
			sessionDto = new SessionDto();
			sessionDto.setUser_id(user_id);
			//임시
			sessionDto.setLocation_id(location_id);
			session.setAttribute("session", sessionDto);
		}
		return session;
	}

	@Override
	public void destorySession(HttpSession httpSession) {
		httpSession.invalidate();
	}
}
