package bobs.Dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomInfoDto {
	private int id;
	private String created_at;
	private int max_people;
	private String deadline;
	private String room_status;
	private int category_id;
	private int location_id;
}
