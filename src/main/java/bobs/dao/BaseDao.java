package bobs.dao;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public interface BaseDao<Dto> {
    JdbcTemplate jdbcTemplate = new JdbcTemplate();

    int create(Dto dto);
    List<Dto> findById(int id);
}
