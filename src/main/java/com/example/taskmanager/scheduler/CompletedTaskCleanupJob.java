package com.example.taskmanager.scheduler;

import com.example.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CompletedTaskCleanupJob {

    @Autowired
    private TaskRepository taskRepository;

    @Scheduled(cron = "0 0 20 * * ?") // Runs every day at 8 PM
    public void deleteCompletedTasks() {
        int deletedCount = taskRepository.deleteCompletedTasks();
        System.out.println("Deleted " + deletedCount + " completed tasks.");
    }
}
