package bobs.Controller;

import bobs.Service.RoomServiceImpl;
import bobs.domain.CanceledRoom;
import bobs.domain.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class RoomController {

	private final RoomServiceImpl roomService;

	@Autowired
	public RoomController(RoomServiceImpl roomService) {
		this.roomService = roomService;
	}

	@GetMapping("/main")
	public String listRoom(@RequestParam("id") String id, Model model) {
		System.out.println("top");
		List<Room> rooms = roomService.findRooms(id);
		model.addAttribute("rooms", rooms);
		model.addAttribute("user_id", id);
		System.out.println("bottom");
		return "mainn";
	}

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

@PostMapping("/main")
public Object cancel(CanceledRoom canceledRoom) {
		List<String> leftParticipants = roomService.cancelRoom(canceledRoom);
		return leftParticipants;
	}
}
