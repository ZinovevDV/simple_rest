package ru.mail.zinovev_dv.simple_rest.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.ErrorResponse;
import org.springframework.web.util.UriComponentsBuilder;
import ru.mail.zinovev_dv.simple_rest.Errors.ErrorsData;
import ru.mail.zinovev_dv.simple_rest.dto.Task;
import ru.mail.zinovev_dv.simple_rest.dto.TaskNew;
import ru.mail.zinovev_dv.simple_rest.repository.TaskRepository;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    TaskRepository taskRepository;
    @Mock
    MessageSource messageSource;

    @InjectMocks
    TaskController controller;

    @Test
    @DisplayName("Тест списка задач")
    void getAllTasks_ReturnsValidResponseEntity() {
        // given
        var tasks = List.of(new Task(UUID.randomUUID(), "First task", false),
                new Task(UUID.randomUUID(), "Second task", true));
        Mockito.doReturn(tasks).when(this.taskRepository).findAll();
        // when
        var responseEntity = this.controller.getAllTasks();
        // then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(tasks, responseEntity.getBody());

    }

    @Test
    void addTask_TaskIsValid_ReturnsValidResponseEntity() {
        // given
        var details = "Third task";
        // when
        var responseEntity = this.controller.addTask(new TaskNew(details),
                UriComponentsBuilder.fromUriString("http://localhost:8080"),
                Locale.ENGLISH);
        // then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        if (responseEntity.getBody() instanceof  Task task){
            assertNotNull(task.id());
            assertEquals(details, task.details());
            assertFalse(task.completed());

            assertEquals(URI.create("http://localhost:8080/api/tasks/" + task.id()),
                    responseEntity.getHeaders().getLocation());

            Mockito.verify(this.taskRepository).save(task);
        } else {
            assertInstanceOf(Task.class, responseEntity.getBody());
        }
        // проверяем, что не было других лишних вызовов
        Mockito.verifyNoMoreInteractions(this.taskRepository);
    }


    @Test
    void addTask_TaskIsInvalid_ReturnsValidResponseEntity() {
        // given
        String details = " ";
        var locale = Locale.US;
        var errorMessage = "Details is empty";
        var errors = new ErrorsData(List.of(errorMessage));
        Mockito.doReturn(errorMessage).when(this.messageSource)
                .getMessage("tasks.create.details.errors.not_set", new Object[0], locale);

        // when
        var responseEntity = this.controller.addTask(new TaskNew(details),
                UriComponentsBuilder.fromUriString("http://localhost:8080"),
                locale);

        // then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(errors, responseEntity.getBody());

        Mockito.verifyNoInteractions(taskRepository);
    }

}

