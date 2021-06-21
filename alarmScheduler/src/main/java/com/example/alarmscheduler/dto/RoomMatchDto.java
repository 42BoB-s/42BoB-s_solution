package com.example.alarmscheduler.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoomMatchDto {
    private int room_id;
    private String user_id;
    private String enter_at;
}