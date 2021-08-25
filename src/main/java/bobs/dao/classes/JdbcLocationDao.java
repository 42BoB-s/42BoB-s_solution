package bobs.dao.classes;

import bobs.dao.BaseDao;
import bobs.dao.LocationDao;
import bobs.dto.BaseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcLocationDao implements BaseDao<BaseDto>, LocationDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public int create(BaseDto locationDto) {
        return 0;
    }

    @Override
    public List<BaseDto> findById(int id) {
        return jdbcTemplate.query(SQL_GETLOCATION, rowMapper, id);
    }

}
