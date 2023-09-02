package ru.mail.zinovev_dv.simple_rest.controllers;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.mail.zinovev_dv.simple_rest.Errors.ErrorsData;
import ru.mail.zinovev_dv.simple_rest.dto.Task;
import ru.mail.zinovev_dv.simple_rest.dto.TaskNew;
import ru.mail.zinovev_dv.simple_rest.repository.TaskRepository;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;


@AllArgsConstructor
@RestController
@RequestMapping("api/tasks")
public class TaskController {
    private final TaskRepository taskRepository;

    private final MessageSource messageSource;

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(){
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.taskRepository.findAll());

    }

    @PostMapping
    public ResponseEntity<?> addTask(@RequestBody TaskNew task,
                                     UriComponentsBuilder uriComponentsBuilder,
                                     Locale locale){
        if(task.details() == null || task.details().isBlank()){
            final var message = this.messageSource.getMessage("tasks.create.details.errors.not_set",
                    new Object[0], locale);
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ErrorsData((List.of(message))));
        }
        else {

            var newTask = new Task(task.details());
            this.taskRepository.save(newTask);

            return ResponseEntity.created(uriComponentsBuilder
                            .path("/api/tasks/{taskId}")
                            .build(Map.of("taskId", newTask.id())))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body((newTask));
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<Task> findTask(@PathVariable("id") UUID id){
        return ResponseEntity.of(this.taskRepository.findById(id));
    }
}
