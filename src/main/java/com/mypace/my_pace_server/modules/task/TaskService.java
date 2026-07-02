package com.mypace.my_pace_server.modules.task;

import com.mypace.my_pace_server.modules.task.dto.TaskDto;
import com.mypace.my_pace_server.modules.task.dto.TaskResponseDto;

public interface TaskService {

  // Create a task
  TaskResponseDto createTask(TaskDto task);

  // update task
  TaskResponseDto updateTask(TaskDto task, String taskId);

  // delete a task
  void deleteTask(String taskId);

  // get all tasks
  Iterable<TaskResponseDto> getAllTasks();
}
