package ru.mail.zinovev_dv.simple_rest.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql("/tasks_rest_controller/tests.sql")
@Transactional
@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class TasksRestControllerITest {
    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser
    void getAllTasks_ReturnValidResponseEntity() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.get("/api/tasks");
        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                [
                                    {
                                        "id": "5c3f7277-ccda-4de9-a0f8-d92edcb70b0c",
                                        "details": "first task",
                                        "completed": false
                                    }
                                ]
                                """)
                );
    }

    @Test
    void getAllTasksAuth_ReturnValidResponseEntity() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.get("/api/tasks")
                .with(httpBasic("user", "password"));
        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                [
                                    {
                                        "id": "5c3f7277-ccda-4de9-a0f8-d92edcb70b0c",
                                        "details": "first task",
                                        "completed": false
                                    }
                                ]
                                """)
                );
    }

    @Test
    void handleCreateNewTask_PayloadIsValid_ReturnsValidResponseEntity() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.post("/api/tasks")
                .with(httpBasic("user", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "details": "Третья задача"
                        }
                        """);

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andExpectAll(
                        status().isCreated(),
                        header().exists(HttpHeaders.LOCATION),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                {
                                    "details": "Третья задача",
                                    "completed": false
                                }
                                """),
                        jsonPath("$.id").exists()
                );
    }
}
