package bobs.Controller;

import bobs.Dto.RoomInfoDto;
import bobs.Dto.RoomMatchDto;
import bobs.Dto.SessionDto;
import bobs.Service.RoomServiceImpl;
import bobs.domain.CanceledRoom;
import bobs.domain.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.util.*;

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
	
	@PostMapping("/enter")
	public @ResponseBody int enterOrCreate(HttpServletResponse response, @RequestBody Map<String, String> request, HttpSession httpSession){
		RoomInfoDto roomInfoDto = new RoomInfoDto();
		roomInfoDto.setLocation_id(Integer.parseInt(request.get("location")));
		roomInfoDto.setCategory_id(Integer.parseInt(request.get("menu")));
		RoomMatchDto roomMatchDto = new RoomMatchDto();
		SessionDto sessionDto = (SessionDto) httpSession.getAttribute("session");
		String id = "";
		if (sessionDto != null)
			id = sessionDto.getUser_id();
		roomMatchDto.setUser_id(id);
		System.out.println("revervation");
		String endTime = request.get("timeTo");

		//false 일때 alert 출력해서 실패했다고 출력 필요.
		//id는 roomMatchDto가 아니라 session에서 받아와야함.
/*방 생성 및 참여에 실패했다는 경고메시지 출력*/
		return roomService.findVaildRoom(roomInfoDto, roomMatchDto, endTime);
	}
	
	@PostMapping("/cancel")
	public String cancel(@RequestBody Map<String, String> request) {
		CanceledRoom canceledRoom = new CanceledRoom();
		canceledRoom.setRoom_id(Integer.parseInt(request.get("room_id")));
		canceledRoom.setUser_id(request.get("id"));
		System.out.println("cancel");
		roomService.cancelRoom(canceledRoom);
		//System.out.println(readJSONStringFromRequestBody(request));
		return "mainn";
	}
}
