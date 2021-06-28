package bobs.Dao;

public interface ActivityLogDao {

    final String SQL_LOGSEQ = "SELECT IFNULL(MAX(id) + 1, 1) FROM activity_log";
    final String SQL_INSERLOG = "INSERT INTO activity_log(id, activity_status, location_id, created_at, user_id) " +
            "values(?, ?, ?, NOW(), ?)";

}
