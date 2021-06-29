package bobs.Dao.Class;

import bobs.Dao.BaseDao;
import bobs.Dao.CategoryDao;
import bobs.Dto.BaseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcCategoryBaseDao implements BaseDao<BaseDto>, CategoryDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public int create(BaseDto categoryDto) {
        return 0;
    }

    @Override
    public List<BaseDto> findById(int id) {
        return jdbcTemplate.query(SQL_GETCATEGORY, rowMapper, id);
    }

}
