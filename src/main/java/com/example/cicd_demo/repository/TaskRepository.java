package com.example.cicd_demo.repository;


import com.example.cicd_demo.model.Task;
import com.example.cicd_demo.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByStatus(TaskStatus status);
}
