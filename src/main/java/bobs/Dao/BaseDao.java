package bobs.Dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.List;

public interface Dao<Dto> {
    JdbcTemplate jdbcTemplate = new JdbcTemplate();

    int create(Dto dto);
    List<Dto> findById(int id);
}