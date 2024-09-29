package com.example.taskmanager.service;

import com.example.taskmanager.model.Task;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private List<Task> tasks = new ArrayList<>();
    private int currentId = 1;

    public List<Task> getAllTasks() {
        return tasks;
    }

    public Optional<Task> getTaskById(int id) {
        return tasks.stream().filter(task -> task.getId() == id).findFirst();
    }

    public Task createTask(Task task) {
        task.setId(currentId++);
        tasks.add(task);
        return task;
    }

    public Optional<Task> updateTask(int id, Task updatedTask) {
        Optional<Task> existingTask = getTaskById(id);
        existingTask.ifPresent(task -> {
            task.setTitle(updatedTask.getTitle());
            task.setDescription(updatedTask.getDescription());
            task.setCompleted(updatedTask.isCompleted());
        });
        return existingTask;
    }

    public boolean deleteTask(int id) {
        return tasks.removeIf(task -> task.getId() == id);
    }

    // Search tasks by title, description, or completed status
    public List<Task> searchTasks(String title, String description, Boolean completed) {
        return tasks.stream()
                .filter(task -> (title == null || task.getTitle().toLowerCase().contains(title.toLowerCase())) &&
                        (description == null || task.getDescription().toLowerCase().contains(description.toLowerCase())) &&
                        (completed == null || task.isCompleted() == completed))
                .collect(Collectors.toList());
    }




}
