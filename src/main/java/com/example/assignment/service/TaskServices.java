package com.example.assignment.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.assignment.mapper.TaskMapper;
import com.example.assignment.model.*;
import com.example.assignment.repository.TaskRepository;
import com.example.assignment.repository.UserRepository;

@Service
public class TaskServices {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskMapper taskMapper;

    public TaskResponse uploadTask(TaskCreateRequest request) {

        User currentUser = getCurrentUser();

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());

        task.setCreatedBy(currentUser);
        if (request.getAssignedToId() != null) {
            User assignee = userRepository.findById(request.getAssignedToId())
                    .orElseThrow(() -> new RuntimeException("Assigned user not found"));
            task.setAssignedTo(assignee);
        }

        return taskMapper.toResponse(taskRepository.save(task));
    }

    public List<TaskResponse> getTasksForCurrentUser() {

        User user = getCurrentUser();

        List<Task> tasks =
                "ADMIN".equals(user.getRole())
                        ? taskRepository.findAll()
                        : taskRepository.findByCreatedByIdOrAssignedToId(user.getId(),user.getId());

        return tasks.stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    public TaskResponse getTaskForCurrentUser(Long taskId) {

        User user = getCurrentUser();

        Task task = taskRepository
                .findByIdAndCreatedById(taskId, user.getId())
                .orElseThrow(() -> new RuntimeException("Task not found"));

        return taskMapper.toResponse(task);
    }

    public TaskResponse updateTask(Long taskId, TaskUpdateRequest request) {

        User currentUser = getCurrentUser();

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        boolean isCreator =
                task.getCreatedBy().getId().equals(currentUser.getId());

        boolean isAdmin =
                "ADMIN".equals(currentUser.getRole());

        boolean isAssignee =
                task.getAssignedTo() != null &&
                task.getAssignedTo().getId().equals(currentUser.getId());

        if (isCreator || isAdmin) {

            task.setTitle(request.getTitle());
            task.setDescription(request.getDescription());
            task.setPriority(request.getPriority());
            task.setDueDate(request.getDueDate());
            task.setStatus(request.getStatus());

        } else if (isAssignee) {

            task.setStatus(request.getStatus());

        } else {
            throw new RuntimeException("Not authorized to update this task");
        }

        return taskMapper.toResponse(taskRepository.save(task));
    }

public void deleteTask(Long taskId) {

    User currentUser = getCurrentUser();

    Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Task not found"));

    boolean isCreator =
            task.getCreatedBy().getId().equals(currentUser.getId());

    boolean isAdmin =
            "ADMIN".equals(currentUser.getRole());

    if (isCreator || isAdmin) {
        taskRepository.delete(task);
    } else {
        throw new RuntimeException("Not authorized to delete this task");
    }
}

    private User getCurrentUser() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
public TaskStatsResponse getTaskStats() {

    User currentUser = getCurrentUser();
    boolean isAdmin = "ADMIN".equals(currentUser.getRole());

    TaskStatsResponse response = new TaskStatsResponse();

    if (isAdmin) {

        response.setTotalTasks(taskRepository.count());
        response.setCompletedTasks(taskRepository.countByStatus("COMPLETED"));
        response.setPendingTasks(taskRepository.countByStatus("OPEN"));

        response.setTasksByPriority(
                Map.of(
                        "HIGH", taskRepository.countByPriority("HIGH"),
                        "MEDIUM", taskRepository.countByPriority("MEDIUM"),
                        "LOW", taskRepository.countByPriority("LOW")
                )
        );

    } else {

        Long userId = currentUser.getId();

        response.setTotalTasks(
                taskRepository.countByStatusForUser("OPEN", userId)
              + taskRepository.countByStatusForUser("COMPLETED", userId)
        );

        response.setCompletedTasks(
                taskRepository.countByStatusForUser("COMPLETED", userId)
        );

        response.setPendingTasks(
                taskRepository.countByStatusForUser("OPEN", userId)
        );

        response.setTasksByPriority(
                Map.of(
                        "HIGH", taskRepository.countByPriorityForUser("HIGH", userId),
                        "MEDIUM", taskRepository.countByPriorityForUser("MEDIUM", userId),
                        "LOW", taskRepository.countByPriorityForUser("LOW", userId)
                )
        );
    }
    return response;
}

}
