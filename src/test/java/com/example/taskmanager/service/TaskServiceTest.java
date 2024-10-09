package com.example.taskmanager.service;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TaskServiceTest {

    private TaskService taskService;

    @BeforeEach
    public void setUp() {
        taskService = new TaskService();
    }

    @Test
    public void testGetAllTasks() {
        // Call the service method
        List<Task> result = taskService.getAllTasks();

        // Verify the results
        assertTrue(result.isEmpty(), "Task list should be empty initially.");
    }

    @Test
    public void testCreateTask() {
        Task task = new Task(0, "New Task", "New Description", false);

        // Call the service method
        Task createdTask = taskService.createTask(task);

        // Verify that the task is created and has an ID
        assertNotNull(createdTask.getId(), "Task ID should not be null after creation.");
        assertEquals("New Task", createdTask.getTitle(), "Task title should match.");
        assertEquals("New Description", createdTask.getDescription(), "Task description should match.");
        assertFalse(createdTask.isCompleted(), "Task should not be completed.");
    }

    @Test
    public void testGetTaskById() {
        Task task = new Task(0, "Task 1", "Description 1", false);
        taskService.createTask(task);  // Create the task first

        // Call the service method
        Optional<Task> result = taskService.getTaskById(1);

        // Verify the results
        assertTrue(result.isPresent(), "Task should be found.");
        assertEquals("Task 1", result.get().getTitle(), "Task title should match.");
    }

    @Test
    public void testUpdateTask() {
        Task task = new Task(0, "Old Task", "Old Description", false);
        taskService.createTask(task);  // Create the task first

        Task updatedTask = new Task(0, "Updated Task", "Updated Description", true);

        // Call the service method
        Optional<Task> result = taskService.updateTask(1, updatedTask);

        // Verify that the task is updated
        assertTrue(result.isPresent(), "Task should be found for update.");
        assertEquals("Updated Task", result.get().getTitle(), "Task title should be updated.");
        assertTrue(result.get().isCompleted(), "Task should be marked as completed.");
    }

    @Test
    public void testDeleteTask() {
        Task task = new Task(0, "Task to Delete", "Delete me", false);
        taskService.createTask(task);  // Create the task first

        // Call the service method to delete
        boolean result = taskService.deleteTask(1);

        // Verify that the task is deleted
        assertTrue(result, "Task should be deleted.");
        assertFalse(taskService.getAllTasks().contains(task), "Task list should not contain the deleted task.");
    }

    @Test
    public void testSearchTasks() {
        taskService.createTask(new Task(0, "Test Task 1", "Description 1", false));
        taskService.createTask(new Task(0, "Another Task", "Description 2", true));

        // Call the service method for searching by title
        List<Task> result = taskService.searchTasks("Test", null, null);

        // Verify the results
        assertEquals(1, result.size(), "Should find one task with the title 'Test Task 1'");
        assertEquals("Test Task 1", result.get(0).getTitle(), "Task title should match.");

        // Call the service method for searching by completed status
        result = taskService.searchTasks(null, null, true);

        // Verify the results
        assertEquals(1, result.size(), "Should find one task that is completed.");
        assertEquals("Another Task", result.get(0).getTitle(), "Task title should match.");
    }
}
