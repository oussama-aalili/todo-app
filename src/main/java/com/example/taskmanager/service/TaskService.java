package com.example.taskmanager.service;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private TaskRepository taskRepository;

    private ChatClient chatClient;


    public TaskService(TaskRepository taskRepository, ChatClient.Builder builder) {
        this.taskRepository = taskRepository;
        this.chatClient = builder.build();
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(int id) {
        return taskRepository.findById(id);
    }

    public Task createTask(Task task) {
        task.setDescription(improveTaskDescription(task));
        return taskRepository.save(task);
    }

    public Optional<Task> updateTask(int id, Task updatedTask) {
        // Check if the task exists
        Optional<Task> existingTask = getTaskById(id);
        if (existingTask.isPresent()) {
            updatedTask.setId(id);  // Set the ID of the task being updated
            updatedTask.setDescription(improveTaskDescription(updatedTask));
            return Optional.of(taskRepository.save(updatedTask));  // Save the updated task
        }
        return Optional.empty();
    }

    public boolean deleteTask(int id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;  // Return true if the task was deleted
        }
        return false;  // Return false if the task was not found
    }

    // Search tasks by title, description, or completed status
    public List<Task> searchTasks(String title, String description, Boolean completed) {
        // Since we're using a database, we can build custom queries in the repository for better searching
        // For now, we can return all tasks and filter in memory as a simple implementation
        return taskRepository.findAll().stream()
                .filter(task -> (title == null || task.getTitle().toLowerCase().contains(title.toLowerCase())) &&
                        (description == null || task.getDescription().toLowerCase().contains(description.toLowerCase())) &&
                        (completed == null || task.isCompleted() == completed))
                .toList();
    }

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
                .content();

        // Ensure the response does not exceed the limit
        return response.length() > 255 ? response.substring(0, 255) : response;
    }

}
