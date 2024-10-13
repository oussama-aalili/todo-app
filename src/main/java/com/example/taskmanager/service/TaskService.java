package com.example.taskmanager.service;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(int id) {
        return taskRepository.findById(id);
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Optional<Task> updateTask(int id, Task updatedTask) {
        // Check if the task exists
        Optional<Task> existingTask = getTaskById(id);
        if (existingTask.isPresent()) {
            updatedTask.setId(id);  // Set the ID of the task being updated
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
}
