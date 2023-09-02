package ru.mail.zinovev_dv.simple_rest.repository;

import org.springframework.stereotype.Repository;
import ru.mail.zinovev_dv.simple_rest.dto.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TaskRepositoryInMem implements TaskRepository{

    private final List<Task> tasks = new ArrayList<>() {{
        this.add(new Task("First task"));
        this.add(new Task("Second task"));
    }};

    @Override
    public List<Task> findAll() {
        return this.tasks;
    }

    @Override
    public void save(Task task) {
        this.tasks.add(task);
    }

    @Override
    public Optional<Task> findById(UUID id) {
        return this.tasks.stream()
                .filter(task -> task.id().equals(id))
                .findAny();
    }

}
