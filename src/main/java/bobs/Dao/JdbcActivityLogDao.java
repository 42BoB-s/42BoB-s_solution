package bobs.Dao;

import bobs.Dto.ActivityLogDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class JdbcActivityLogDao implements ActivityLogDao{

    private final JdbcTemplate jdbcTemplate;
    private final String SQL_LOGSEQ = "SELECT IFNULL(MAX(id) + 1, 1) FROM activity_log";
    private final String SQL_INSERLOG = "INSERT INTO activity_log(id, activity_status, location_id, created_at, user_id) " +
            "values(?, ?, ?, NOW(), ?)";


    @Autowired
    public JdbcActivityLogDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void leaveLog(ActivityLogDto activityLogDto) {
        activityLogDto.setId(jdbcTemplate.queryForObject(SQL_LOGSEQ, Integer.class));
        jdbcTemplate.update(SQL_INSERLOG,
                activityLogDto.getId(),
                activityLogDto.getActivity_status(),
                activityLogDto.getLocation_id(),
                activityLogDto.getUser_id());
    }
}
