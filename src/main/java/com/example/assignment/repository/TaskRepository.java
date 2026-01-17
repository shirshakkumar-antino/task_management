package com.example.assignment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.assignment.model.Task;

public interface TaskRepository extends JpaRepository<Task,Integer>{
    List<Task> findByUserId(Long userId);
    Optional<Task> findByIdAndUserId(Long taskId, Long userId);
}
