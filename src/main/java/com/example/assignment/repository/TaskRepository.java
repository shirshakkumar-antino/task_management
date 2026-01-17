package com.example.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.assignment.model.Task;

public interface TaskRepository extends JpaRepository<Task,Integer>{
    
}
