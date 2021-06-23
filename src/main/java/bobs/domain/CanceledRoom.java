package bobs.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;

@Getter @Setter
@Controller
public class CanceledRoom {
	private String user_id;
	private int room_id;
}
