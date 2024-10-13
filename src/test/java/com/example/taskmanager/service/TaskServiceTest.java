package com.example.taskmanager.service;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    private TaskService taskService;
    private TaskRepository taskRepository;

    @BeforeEach
    public void setUp() {
        taskRepository = Mockito.mock(TaskRepository.class);  // Mock the TaskRepository
        taskService = new TaskService(taskRepository);        // Inject the mock into TaskService
    }

    @Test
    public void testGetAllTasks() {
        // Setup the mock behavior
        when(taskRepository.findAll()).thenReturn(List.of());

        // Call the service method
        List<Task> result = taskService.getAllTasks();

        // Verify the results
        assertTrue(result.isEmpty(), "Task list should be empty initially.");
        verify(taskRepository, times(1)).findAll();  // Verify that findAll was called once
    }

    @Test
    public void testCreateTask() {
        Task task = new Task(0, "New Task", "New Description", false);
        Task savedTask = new Task(1, "New Task", "New Description", false); // Task after saving (with ID)

        // Setup the mock behavior
        when(taskRepository.save(task)).thenReturn(savedTask);

        // Call the service method
        Task createdTask = taskService.createTask(task);

        // Verify that the task is created and has an ID
        assertNotNull(createdTask.getId(), "Task ID should not be null after creation.");
        assertEquals("New Task", createdTask.getTitle(), "Task title should match.");
        assertEquals("New Description", createdTask.getDescription(), "Task description should match.");
        assertFalse(createdTask.isCompleted(), "Task should not be completed.");
        verify(taskRepository, times(1)).save(task);  // Verify that save was called once
    }

    @Test
    public void testGetTaskById() {
        Task task = new Task(1, "Task 1", "Description 1", false);

        // Setup the mock behavior
        when(taskRepository.findById(1)).thenReturn(Optional.of(task));

        // Call the service method
        Optional<Task> result = taskService.getTaskById(1);

        // Verify the results
        assertTrue(result.isPresent(), "Task should be found.");
        assertEquals("Task 1", result.get().getTitle(), "Task title should match.");
        verify(taskRepository, times(1)).findById(1);  // Verify that findById was called once
    }

    @Test
    public void testUpdateTask() {
        Task existingTask = new Task(1, "Old Task", "Old Description", false);
        Task updatedTask = new Task(1, "Updated Task", "Updated Description", true);

        // Setup the mock behavior
        when(taskRepository.findById(1)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(updatedTask)).thenReturn(updatedTask);

        // Call the service method
        Optional<Task> result = taskService.updateTask(1, updatedTask);

        // Verify that the task is updated
        assertTrue(result.isPresent(), "Task should be found for update.");
        assertEquals("Updated Task", result.get().getTitle(), "Task title should be updated.");
        assertTrue(result.get().isCompleted(), "Task should be marked as completed.");
        verify(taskRepository, times(1)).findById(1);  // Verify that findById was called once
        verify(taskRepository, times(1)).save(updatedTask);  // Verify that save was called once
    }

    @Test
    public void testDeleteTask() {
        Task task = new Task(1, "Task to Delete", "Delete me", false);

        // Setup the mock behavior
        when(taskRepository.existsById(1)).thenReturn(true);

        // Call the service method to delete
        boolean result = taskService.deleteTask(1);

        // Verify that the task is deleted
        assertTrue(result, "Task should be deleted.");
        verify(taskRepository, times(1)).deleteById(1);  // Verify that deleteById was called once
    }

    @Test
    public void testSearchTasks() {
        Task task1 = new Task(1, "Test Task 1", "Description 1", false);
        Task task2 = new Task(2, "Another Task", "Description 2", true);

        // Setup the mock behavior
        when(taskRepository.findAll()).thenReturn(List.of(task1, task2));

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
