package com.example.taskmanager.service;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.ai.chat.client.ChatClient;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    private TaskService taskService;
    private TaskRepository taskRepository;
    private ChatClient chatClient;
    private ChatClient.Builder chatClientBuilder;

    @BeforeEach
    public void setUp() {
        taskRepository = Mockito.mock(TaskRepository.class);  // Mock TaskRepository
        chatClient = Mockito.mock(ChatClient.class);          // Mock ChatClient
        chatClientBuilder = Mockito.mock(ChatClient.Builder.class);  // Mock ChatClient.Builder

        // Setup builder behavior
        when(chatClientBuilder.build()).thenReturn(chatClient);

        // Initialize TaskService with mocked dependencies
        taskService = new TaskService(taskRepository, chatClientBuilder);
    }

    @Test
    public void testGetAllTasks() {
        when(taskRepository.findAll()).thenReturn(List.of());  // Mock repository behavior

        List<Task> result = taskService.getAllTasks();

        assertTrue(result.isEmpty(), "Task list should be empty initially.");
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    public void testCreateTask() {
        Task task = new Task(0, "New Task", "Old Description", false);
        Task savedTask = new Task(1, "New Task", "Improved Description", false);

//        when(chatClient.prompt())
//                .thenReturn(new ChatClient.PromptBuilder(chatClient)); // Mock prompt interaction
        // Create mocks for each step of the chain
        ChatClient.ChatClientRequestSpec promptMock = Mockito.mock(ChatClient.ChatClientRequestSpec.class);
        ChatClient.CallResponseSpec responseMock = Mockito.mock(ChatClient.CallResponseSpec.class);

        // Stub the behavior of the chain
        when(chatClient.prompt()).thenReturn(promptMock);
        when(promptMock.user(anyString())).thenReturn(promptMock);
        when(promptMock.call()).thenReturn(responseMock);
        when(responseMock.content()).thenReturn("Improved Description");

      //  when(chatClient.prompt().user(anyString()).call().content()).thenReturn("Improved Description");
        when(taskRepository.save(task)).thenReturn(savedTask);

        Task result = taskService.createTask(task);

        assertEquals("Improved Description", result.getDescription(), "Description should be improved.");
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    public void testUpdateTask() {
        Task existingTask = new Task(1, "Old Task", "Old Description", false);
        Task updatedTask = new Task(1, "Updated Task", "Updated Description", true);
        ChatClient.ChatClientRequestSpec promptMock = Mockito.mock(ChatClient.ChatClientRequestSpec.class);
        ChatClient.CallResponseSpec responseMock = Mockito.mock(ChatClient.CallResponseSpec.class);

        when(taskRepository.findById(1)).thenReturn(Optional.of(existingTask));


        // Stub the behavior of the chain
        when(chatClient.prompt()).thenReturn(promptMock);
        when(promptMock.user(anyString())).thenReturn(promptMock);
        when(promptMock.call()).thenReturn(responseMock);
        when(responseMock.content()).thenReturn("Improved Description");
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        Optional<Task> result = taskService.updateTask(1, updatedTask);

        assertTrue(result.isPresent(), "Task should be updated successfully.");
        assertEquals("Improved Description", result.get().getDescription(), "Description should be improved.");
        verify(taskRepository, times(1)).findById(1);
        verify(taskRepository, times(1)).save(updatedTask);
    }

    @Test
    public void testDeleteTask() {
        when(taskRepository.existsById(1)).thenReturn(true);

        boolean result = taskService.deleteTask(1);

        assertTrue(result, "Task should be deleted successfully.");
        verify(taskRepository, times(1)).deleteById(1);
    }

    @Test
    public void testSearchTasks() {
        Task task1 = new Task(1, "Test Task 1", "Description 1", false);
        Task task2 = new Task(2, "Another Task", "Description 2", true);

        when(taskRepository.findAll()).thenReturn(List.of(task1, task2));

        List<Task> result = taskService.searchTasks("Test", null, null);

        assertEquals(1, result.size(), "Should find one task matching the search criteria.");
        assertEquals("Test Task 1", result.get(0).getTitle(), "Title should match the search criteria.");
    }

    @Test
    public void testImproveTaskDescription() {
        // Create mocks for each step of the chain
        ChatClient.ChatClientRequestSpec promptMock = Mockito.mock(ChatClient.ChatClientRequestSpec.class);
        ChatClient.CallResponseSpec responseMock = Mockito.mock(ChatClient.CallResponseSpec.class);

        // Stub the behavior of the chain
        when(chatClient.prompt()).thenReturn(promptMock);
        when(promptMock.user(anyString())).thenReturn(promptMock);
        when(promptMock.call()).thenReturn(responseMock);
        when(responseMock.content()).thenReturn("Improved Description");

        // Test the service method
        Task task = new Task(0, "Task Title", "Old Description", false);
        String result = taskService.improveTaskDescription(task);

        // Verify the result
        assertEquals("Improved Description", result, "The task description should be improved.");
    }

}