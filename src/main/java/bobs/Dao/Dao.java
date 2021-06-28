package bobs.Dao;

import java.util.List;

public interface Dao<Dto> {

    int create(Dto dto);
    List<Dto> findById(int id);
}
