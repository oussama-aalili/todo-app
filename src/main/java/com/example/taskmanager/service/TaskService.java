package com.example.taskmanager.service;

import com.example.taskmanager.model.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private List<Task> tasks = new ArrayList<>();
    private int currentId = 1;

    // Create a logger instance for this class
    private static final Logger logger = LogManager.getLogger(TaskService.class);

    public List<Task> getAllTasks() {
        return tasks;
    }

    public Optional<Task> getTaskById(int id) {
        logger.info("Fetching task with ID: {}", id);
        Task task1 = tasks.stream().filter(task -> task.getId() == id).findFirst().orElseGet(() -> {
            logger.warn("Task with ID: {} not found", id);
          return null;
        });

        return Optional.ofNullable(task1);
    }


    public Task createTask(Task task) {
        task.setId(currentId++);
        logger.info("Adding a new task with title: {}", task.getTitle());
        tasks.add(task);
        logger.debug("Current number of tasks: {}", tasks.size());

        return task;
    }

    public Optional<Task> updateTask(int id, Task updatedTask) {
        logger.info("Updating task with ID: {}", id);
        Optional<Task> existingTask = getTaskById(id);
        existingTask.ifPresent(task -> {
            task.setTitle(updatedTask.getTitle());
            task.setDescription(updatedTask.getDescription());
            task.setCompleted(updatedTask.isCompleted());
            logger.debug("Task with ID: {} updated successfully", id);
        });
        return existingTask;
    }

    public boolean deleteTask(int id) {
        logger.info("Deleting task with ID: {}", id);
        return tasks.removeIf(task -> task.getId() == id);
    }

    // Search tasks by title, description, or completed status
    public List<Task> searchTasks(String title, String description, Boolean completed) {
        logger.info("Searching task with title: {}", title);
        return tasks.stream()
                .filter(task -> (title == null || task.getTitle().toLowerCase().contains(title.toLowerCase())) &&
                        (description == null || task.getDescription().toLowerCase().contains(description.toLowerCase())) &&
                        (completed == null || task.isCompleted() == completed))
                .collect(Collectors.toList());
    }
}
