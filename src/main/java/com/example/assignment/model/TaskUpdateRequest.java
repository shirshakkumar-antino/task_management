package com.example.assignment.model;

import java.sql.Date;

import lombok.Data;

@Data
public class TaskUpdateRequest {
    private String title;
    private String description;
    private String status;
    private String priority;
    private Date dueDate;
}
