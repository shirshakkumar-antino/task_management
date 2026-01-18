package com.example.assignment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.assignment.model.Task;

public interface TaskRepository extends JpaRepository<Task,Long>{
    List<Task> findByCreatedByIdOrAssignedToId(Long createdById, Long assignedToId);

    Optional<Task> findByIdAndCreatedById(Long taskId, Long userId);
    Optional<Task>findById(Long taskId);
    @Query("""
            SELECT COUNT(t)
            FROM Task t
            WHERE t.priority = :priority
            AND (t.createdBy.id = :userId OR t.assignedTo.id = :userId)
        """)
        long countByPriorityForUser(
                @Param("priority") String priority,
                @Param("userId") Long userId
        );

        @Query("""
            SELECT COUNT(t)
            FROM Task t
            WHERE t.status = :status
            AND (t.createdBy.id = :userId OR t.assignedTo.id = :userId)
        """)
        long countByStatusForUser(
                @Param("status") String status,
                @Param("userId") Long userId
        );

        long countByPriority(String priority);
        long countByStatus(String status);
}
