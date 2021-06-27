package bobs.Controller;

import bobs.Dto.SessionDto;
import bobs.Service.RoomServiceImpl;
import bobs.domain.CanceledRoom;
import bobs.domain.Room;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class RoomController {
	
	private final RoomServiceImpl roomService;
	
	@Autowired
	public RoomController(RoomServiceImpl roomService) {
		this.roomService = roomService;
	}
	
	@GetMapping("/main")
	public String listRoom(HttpSession httpSession, Model model) {
		
		//dohelee 추가
		SessionDto sessionDto = (SessionDto) httpSession.getAttribute("session");
		String id = "";
		if (sessionDto != null)
			id = sessionDto.getUser_id();
		
		List<Room> rooms = roomService.findRooms(id);
		model.addAttribute("rooms", rooms);
		model.addAttribute("user_id", id);
		return "mainn";
	}

	private String readJSONStringFromRequestBody(HttpServletRequest request){
		StringBuffer json = new StringBuffer();
		String line = null;

		try {
			BufferedReader reader = request.getReader();
			while((line = reader.readLine()) != null) {
				json.append(line);
			}

		}catch(Exception e) {
			System.out.println("Error reading JSON string: " + e.toString());
		}
		return json.toString();
	}

	@PostMapping("/enter")
	public String test(HttpServletRequest request) {
		System.out.println("revervation");
		System.out.println(readJSONStringFromRequestBody(request));
		return "mainn";
	}

	@PostMapping("/cancel")
	public String cancelTest(HttpServletRequest request) {
		System.out.println("cancel");
		System.out.println(readJSONStringFromRequestBody(request));
		return "mainn";
	}
//@PostMapping("/main")
//public String cancel(HttpSession httpSession, CanceledRoom canceledRoom) {
//
//		SessionDto sessionDto = (SessionDto)httpSession.getAttribute("session");
//		String id = "";
//		if (sessionDto != null)
//			id = sessionDto.getUser_id();
//
//		roomService.cancelRoom(canceledRoom);
//		return "mainn";
//	}
	
	//	@GetMapping("/main")
//	public String listRoom(@RequestParam(value="id", required = false) String id, Model model){
//		ObjectMapper objectMapper = new ObjectMapper();
//		try{
//			String rooms = objectMapper.writeValueAsString(roomService.findRooms(id));
//			model.addAttribute("rooms", rooms);
//		} catch (JsonProcessingException e)
//		{
//			e.printStackTrace();;
//		}
//		return "mainn";
//	}

//	@GetMapping("/main")
//	public Object listRoom(@RequestBody User user) {
//		Map<String, Object> data = new HashMap<String, Object>();
//		data.put("user_id", user.getUser_id());
//		data.put("rooms", roomService.findRooms((user.getUser_id())));
//		return data;
//	}
}
