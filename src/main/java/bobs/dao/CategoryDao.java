package bobs.dao;

import bobs.dto.BaseDto;
import org.springframework.jdbc.core.RowMapper;

public interface CategoryDao {
    String SQL_GETCATEGORY = "SELECT * FROM category WHERE id = ?";

    RowMapper<BaseDto> rowMapper = (rs, rowNum) -> {
        BaseDto category = new BaseDto();
        category.setId(rs.getInt("id"));
        category.setName(rs.getString("name"));
        return category;
    };
}
