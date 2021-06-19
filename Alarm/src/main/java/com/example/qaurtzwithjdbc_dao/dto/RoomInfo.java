package com.example.qaurtzwithjdbc_dao.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RoomInfo {

    int id;
    String created_at;
    int max_people;
    String deadline;
    String roomStatus;
    String category_id;
    int location_id;

    public RoomInfo() {
    }

    public RoomInfo(String created_at, int max_people, String deadline, String roomStatus, String category_id, int location_id) {
        this.created_at = created_at;
        this.max_people = max_people;
        this.deadline = deadline;
        this.roomStatus = roomStatus;
        this.category_id = category_id;
        this.location_id = location_id;
    }
}
