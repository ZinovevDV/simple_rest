package ru.mail.zinovev_dv.simple_rest.repository;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.mail.zinovev_dv.simple_rest.dto.Task;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Repository
@Primary
public class JdbcOperationsTaskRepository  implements TaskRepository, RowMapper<Task> {
    private final JdbcOperations jdbcOperations;

    @Override
    public List<Task> findAll() {
        return this.jdbcOperations.query("select * from task;", this);
    }

    @Override
    public List<Task> findAllByApplicationUserId(UUID uuid) {
        return this.jdbcOperations.query("select * from task where id_application_user = ?", this, uuid);
    }

    @Override
    public void save(Task task) {
        this.jdbcOperations.update("""
                                insert into task(id, details, completed)
                                values (?, ?, ?)
                                """, new Object[]{task.id(), task.details(), task.completed()});
    }

    @Override
    public Optional<Task> findById(UUID id) {
        return this.jdbcOperations.query("""
                select * from task where id = ?
                """, new Object[]{id}, this)
                .stream().findFirst();
    }

    @Override
    public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Task(rs.getObject("id", UUID.class),
                rs.getString("details"),
                rs.getBoolean("completed"));
    }
}
