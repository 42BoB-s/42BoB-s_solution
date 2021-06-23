package bobs.Controller;

import bobs.Dto.RoomInfoDto;
import bobs.Dto.RoomMatchDto;
import bobs.Service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class TestController {
    private final RoomService roomService;
    @Autowired
    public TestController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/test")
    public List<RoomInfoDto> test(RoomInfoDto roomInfoDto, RoomMatchDto roomMatchDto, String startTime, String endTime)
    {
        roomInfoDto.setCategory_id(1);
        roomInfoDto.setLocation_id(1);
        roomMatchDto.setUser_id("99999");
        startTime = "2021-06-01 12:00:00";
        endTime = "2021-06-22 14:00:00";
        return roomService.findVaildRoom(roomInfoDto, roomMatchDto, startTime, endTime);
    }
}