package com.example.qaurtzwithjdbc_dao.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RoomMatch {
    int id;
    String user_id;
    String enter_at;

    public RoomMatch() {

    }

    public RoomMatch(int id, String user_id, String enter_at) {
        this.id = id;
        this.user_id = user_id;
        this.enter_at = enter_at;
    }

}
