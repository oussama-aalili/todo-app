package com.example.taskmanager.controller;

import com.example.taskmanager.controller.TaskController;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.service.TaskService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)  // Focus on testing only TaskController
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Mock the TaskService to avoid using the actual service
    @MockBean
    private TaskService taskService;

    // Test: Get All Tasks
    @Test
    public void testGetAllTasks() throws Exception {
        // Mocking TaskService to return an empty list
        Mockito.when(taskService.getAllTasks()).thenReturn(Collections.emptyList());

        // Perform a GET request to /api/tasks
        mockMvc.perform(get("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());  // Expecting an empty array
    }

    // Test: Get Task by ID
    @Test
    public void testGetTaskById() throws Exception {
        // Mocking TaskService to return a specific Task
        Task task = new Task(1, "Test Task", "Test Description", false);
        Mockito.when(taskService.getTaskById(1)).thenReturn(Optional.of(task));

        // Perform a GET request to /api/tasks/1
        mockMvc.perform(get("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Task"));
    }

    // Test: Create New Task
    @Test
    public void testCreateTask() throws Exception {
        // Mocking TaskService to return the created Task
        Task task = new Task(1, "New Task", "New Description", false);
        Mockito.when(taskService.createTask(any(Task.class))).thenReturn(task);

        // Perform a POST request to /api/tasks
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"New Task\", \"description\": \"New Description\", \"completed\": false}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("New Task"));
    }

    // Test: Update Task by ID
    @Test
    public void testUpdateTask() throws Exception {
        // Mocking TaskService to return the updated Task
        Task updatedTask = new Task(1, "Updated Task", "Updated Description", true);
        Mockito.when(taskService.updateTask(eq(1), any(Task.class))).thenReturn(Optional.of(updatedTask));

        // Perform a PUT request to /api/tasks/1
        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Updated Task\", \"description\": \"Updated Description\", \"completed\": true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Updated Task"))
                .andExpect(jsonPath("$.completed").value(true));
    }

    // Test: Delete Task by ID
    @Test
    public void testDeleteTask() throws Exception {
        // Mocking TaskService to return true for deletion success
        Mockito.when(taskService.deleteTask(1)).thenReturn(true);

        // Perform a DELETE request to /api/tasks/1
        mockMvc.perform(delete("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    // Test: Search Tasks
    @Test
    public void testSearchTasks() throws Exception {
        // Mocking TaskService to return a list with one task
        Task task = new Task(1, "Test Task", "Description", false);
        Mockito.when(taskService.searchTasks("Test Task", "Description", false))
                .thenReturn(Collections.singletonList(task));

        // Perform a GET request to /api/tasks/search with parameters
        mockMvc.perform(get("/api/tasks/search")
                        .param("title", "Test Task")
                        .param("description", "Description")
                        .param("completed", "false")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Task"))
                .andExpect(jsonPath("$[0].completed").value(false));
    }
}
