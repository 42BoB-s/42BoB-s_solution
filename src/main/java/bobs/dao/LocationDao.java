package bobs.dao;

import bobs.dto.BaseDto;
import org.springframework.jdbc.core.RowMapper;

public interface LocationDao {
    String SQL_GETLOCATION = "SELECT * FROM location WHERE id = ?";

    RowMapper<BaseDto> rowMapper = (rs, rowNum) -> {
        BaseDto location = new BaseDto();
        location.setId(rs.getInt("id"));
        location.setName(rs.getString("name"));
        return location;
    };
}
