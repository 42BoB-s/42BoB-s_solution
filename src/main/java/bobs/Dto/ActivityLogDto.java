package bobs.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivityLogDto {
    int id;
    String activity_status;
    int location_id;
    String created_at;
    String user_id;
}
