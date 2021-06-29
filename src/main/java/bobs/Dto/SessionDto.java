package bobs.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SessionDto {
	String user_id;
	int location_id;
	String email;
}
