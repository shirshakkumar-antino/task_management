package com.example.assignment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.assignment.model.Task;

public interface TaskRepository extends JpaRepository<Task,Long>{
    List<Task> findByCreatedByIdOrAssignedToId(Long createdById, Long assignedToId);

    Optional<Task> findByIdAndCreatedById(Long taskId, Long userId);
    Optional<Task>findById(Long taskId);
}
