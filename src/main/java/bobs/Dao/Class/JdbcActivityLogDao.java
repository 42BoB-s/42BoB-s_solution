package bobs.Dao.Class;

import bobs.Dao.ActivityLogDao;
import bobs.Dao.BaseDao;
import bobs.Dto.ActivityLogDto;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcActivityLogDao implements BaseDao<ActivityLogDto>,ActivityLogDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public int create(ActivityLogDto activityLogDto) {
        int chk = 0;
        try {
            activityLogDto.setId(jdbcTemplate.queryForObject(SQL_LOGSEQ, Integer.class));
            jdbcTemplate.update(SQL_INSERLOG,
                    activityLogDto.getId(),
                    activityLogDto.getActivity_status(),
                    activityLogDto.getLocation_id(),
                    activityLogDto.getUser_id());
        } catch (DuplicateKeyException d) {
        d.printStackTrace();
        }
		return chk;
    }

    @Override
    public List<ActivityLogDto> findById(int id) {
        return null;
    }

}
