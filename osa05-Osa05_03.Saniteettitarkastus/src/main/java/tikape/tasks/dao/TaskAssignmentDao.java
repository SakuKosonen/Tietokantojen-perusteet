package tikape.tasks.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import tikape.tasks.database.Database;
import tikape.tasks.domain.TaskAssignment;

public class TaskAssignmentDao implements Dao<TaskAssignment, Integer> {

    private Database database;

    public TaskAssignmentDao(Database database) {
        this.database = database;
    }

    @Override
    public TaskAssignment findOne(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<TaskAssignment> findAll() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TaskAssignment saveOrUpdate(TaskAssignment object) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO TaskAssignment (task_id, user_id, completed) VALUES (?, ?, 0)");
            stmt.setInt(1, object.getTaskId());
            stmt.setInt(2, object.getUserId());
            stmt.executeUpdate();
        }

        return null;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
