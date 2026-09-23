package com.example.cicd_demo.service;


import com.example.cicd_demo.dto.TaskRequestDTO;
import com.example.cicd_demo.dto.TaskResponseDTO;
import com.example.cicd_demo.exception.ResourceNotFoundException;
import com.example.cicd_demo.model.Task;
import com.example.cicd_demo.model.TaskStatus;
import com.example.cicd_demo.repository.TaskRepository;
import com.example.cicd_demo.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task existingTask;

    @BeforeEach
    void setUp() {
        existingTask = Task.builder()
                .id(1L)
                .title("Write tests")
                .description("Cover the service layer")
                .status(TaskStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void create_shouldSaveAndReturnTask() {
        TaskRequestDTO request = new TaskRequestDTO("Write tests", "Cover the service layer", TaskStatus.PENDING);
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);

        TaskResponseDTO result = taskService.create(request);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Write test");
        assertThat(result.getStatus()).isEqualTo(TaskStatus.PENDING);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void getById_shouldReturnTask_whenExists() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));

        TaskResponseDTO result = taskService.getById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Write tests");
    }

    @Test
    void getById_shouldThrow_whenNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.getById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void getAll_shouldReturnAllTasks() {
        when(taskRepository.findAll()).thenReturn(List.of(existingTask));

        List<TaskResponseDTO> result = taskService.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Write tests");
    }

    @Test
    void update_shouldModifyAndReturnTask() {
        TaskRequestDTO request = new TaskRequestDTO("Updated title", "Updated description", TaskStatus.IN_PROGRESS);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);

        TaskResponseDTO result = taskService.update(1L, request);

        assertThat(result).isNotNull();
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void delete_shouldRemoveTask_whenExists() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        doNothing().when(taskRepository).delete(existingTask);

        taskService.delete(1L);

        verify(taskRepository, times(1)).delete(existingTask);
    }

    @Test
    void delete_shouldThrow_whenNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
