package com.mypace.my_pace_server.modules.task;

import com.mypace.my_pace_server.common.exceptions.ResourceNotFoundException;
import com.mypace.my_pace_server.modules.task.dto.TaskDto;
import com.mypace.my_pace_server.modules.task.dto.TaskResponseDto;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
  private final ModelMapper modelMapper;
  private final TaskRepository taskRepository;

  @Override
  public TaskResponseDto createTask(TaskDto taskDto) {
    Task task = modelMapper.map(taskDto, Task.class);
    task.setCreatedAt(new Date());
    Task savedTask = taskRepository.save(task);
    return modelMapper.map(savedTask, TaskResponseDto.class);
  }

  @Override
  public TaskResponseDto updateTask(TaskDto taskDto, String taskId) {
    Task existingTask =
        taskRepository
            .findById(taskId)
            .orElseThrow(() -> new ResourceNotFoundException("Task with given id does not exist."));

    existingTask.setTitle(taskDto.getTitle());
    existingTask.setDescription(taskDto.getDescription());
    existingTask.setUpdatedAt(new Date());
    existingTask.setStatus(taskDto.getStatus());
    existingTask.setPriority(taskDto.getPriority());
    existingTask.setDueDate(taskDto.getDueDate());

    Task task = taskRepository.save(existingTask);
    return modelMapper.map(task, TaskResponseDto.class);
  }

  @Override
  public void deleteTask(String taskId) {
    Task task =
        taskRepository
            .findById(taskId)
            .orElseThrow(() -> new ResourceNotFoundException("Task with given id does not exist."));
    taskRepository.delete(task);
  }

  @Override
  public Iterable<TaskResponseDto> getAllTasks() {
    return taskRepository.findAll().stream()
        .map(task -> modelMapper.map(task, TaskResponseDto.class))
        .toList();
  }
}
