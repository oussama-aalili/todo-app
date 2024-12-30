package com.example.taskmanager.service;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service  // Marks this class as a service component for Spring Dependency Injection
public class TaskService {

    private TaskRepository taskRepository;  // The repository used to interact with the database
    private ChatClient chatClient;  // ChatClient used to improve task descriptions using an AI model

    // Logger for logging information and debugging
    private static final Logger logger = LogManager.getLogger(TaskService.class);

    // Constructor injecting dependencies (TaskRepository and ChatClient)
    public TaskService(TaskRepository taskRepository, ChatClient.Builder builder) {
        this.taskRepository = taskRepository;
        this.chatClient = builder.build();  // Initialize the chatClient using the builder pattern
    }

    // Fetch all tasks from the database
    public List<Task> getAllTasks() {
        return taskRepository.findAll();  // Returns all tasks in the database
    }

    // Fetch a task by its ID from the database
    public Optional<Task> getTaskById(int id) {
        logger.info("Fetching task with ID: {}", id);  // Log the action for traceability
        return taskRepository.findById(id);  // Use repository method to find the task by ID
    }

    // Create a new task and save it in the database
    public Task createTask(Task task) {
        logger.info("Adding a new task with title: {}", task.getTitle());  // Log task creation
        task.setDescription(improveTaskDescription(task));  // Improve the task description using AI
        return taskRepository.save(task);  // Save the task in the database and return the saved task
    }

    // Update an existing task by ID
    public Optional<Task> updateTask(int id, Task updatedTask) {
        logger.info("Updating task with ID: {}", id);  // Log the update action
        Optional<Task> existingTask = getTaskById(id);  // Check if the task exists

        if (existingTask.isPresent()) {
            updatedTask.setId(id);  // Ensure the updated task has the correct ID
            updatedTask.setDescription(improveTaskDescription(updatedTask));  // Improve task description
            logger.debug("Task with ID: {} updated successfully", id);  // Log successful update
            return Optional.of(taskRepository.save(updatedTask));  // Save and return the updated task
        }
        return Optional.empty();  // Return empty if the task was not found
    }

    // Delete a task by ID
    public boolean deleteTask(int id) {
        if (taskRepository.existsById(id)) {
            logger.info("Deleting task with ID: {}", id);  // Log deletion action
            taskRepository.deleteById(id);  // Delete the task from the database
            return true;  // Return true if the task was deleted successfully
        }
        return false;  // Return false if the task was not found
    }

    // Search for tasks based on title, description, and completion status
    public List<Task> searchTasks(String title, String description, Boolean completed) {
        logger.info("Searching task with title: {}", title);  // Log the search action
        return taskRepository.findAll().stream()  // Stream all tasks for filtering
                .filter(task -> (title == null || task.getTitle().toLowerCase().contains(title.toLowerCase())) &&
                        (description == null || task.getDescription().toLowerCase().contains(description.toLowerCase())) &&
                        (completed == null || task.isCompleted() == completed))  // Filter by provided criteria
                .toList();  // Return the filtered list of tasks
    }

    // Use ChatClient to improve the task description according to SMART criteria
    public String improveTaskDescription(Task task) {
        String response = chatClient.prompt()
                .user("You are an expert in productivity and task management. Your goal is to rewrite the description of the following task so that it adheres to the SMART criteria:\n" +
                        "Specific: Clearly define what needs to be done.\n" +
                        "Measurable: Include a way to measure progress or success.\n" +
                        "Achievable: Ensure the task is realistic and within reach.\n" +
                        "Relevant: Align the task with broader goals or purposes.\n" +
                        "Time-bound: Specify a deadline or timeframe.\n" +
                        "Here is the current task description: " + task.getDescription() +
                        " Please provide an improved version of the task description that meets these criteria. Keep the language professional and concise. Please ensure the improved task description is concise and does not exceed 254 characters, including spaces.")
                .call()
                .content();  // Call the AI service and get the improved description

        // Ensure the response is not longer than 255 characters
        return response.length() > 255 ? response.substring(0, 255) : response;  // Return the improved description
    }
}
