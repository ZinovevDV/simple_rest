package ru.mail.zinovev_dv.simple_rest.repository;

import ru.mail.zinovev_dv.simple_rest.dto.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository {
    List<Task> findAll();

    List<Task> findAllByApplicationUserId(UUID uuid);
    void save(Task task);

    Optional<Task> findById(UUID id);


}
