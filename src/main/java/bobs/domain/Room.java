package bobs.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Room {

	private String enter_at;
	private List<String> participants;
	private String location_name;
	private String category_name;
	private int room_id;
}
