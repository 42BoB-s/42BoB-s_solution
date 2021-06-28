package bobs.Dao.Class;

import bobs.Dao.Dao;
import bobs.Dao.LocationDao;
import bobs.Dto.BaseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcLocationDao implements Dao<BaseDto>, LocationDao {

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
