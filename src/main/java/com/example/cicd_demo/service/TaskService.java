package com.example.cicd_demo.service;



import com.example.cicd_demo.dto.TaskRequestDTO;
import com.example.cicd_demo.dto.TaskResponseDTO;
import com.example.cicd_demo.model.TaskStatus;

import java.util.List;

public interface TaskService {

    TaskResponseDTO create(TaskRequestDTO request);

    TaskResponseDTO getById(Long id);

    List<TaskResponseDTO> getAll();

    List<TaskResponseDTO> getByStatus(TaskStatus status);

    TaskResponseDTO update(Long id, TaskRequestDTO request);

    void delete(Long id);
}
