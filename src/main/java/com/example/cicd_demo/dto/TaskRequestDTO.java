package com.example.cicd_demo.dto;


import com.example.cicd_demo.model.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Payload used to create or update a task")
public class TaskRequestDTO {

    @NotBlank(message = "title must not be blank")
    @Size(max = 120, message = "title must be at most 120 characters")
    @Schema(description = "Task title", example = "Write project README")
    private String title;

    @Size(max = 500, message = "description must be at most 500 characters")
    @Schema(description = "Task description", example = "Explain how to run and deploy the API")
    private String description;

    @Schema(description = "Task status", example = "PENDING")
    private TaskStatus status;
}
