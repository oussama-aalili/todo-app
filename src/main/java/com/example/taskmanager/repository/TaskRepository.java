package com.example.taskmanager.repository;

import com.example.taskmanager.model.Task;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Task t WHERE t.completed = true")
    int deleteCompletedTasks();
}