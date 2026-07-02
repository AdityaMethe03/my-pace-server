package com.mypace.my_pace_server.modules.task;

import com.mypace.my_pace_server.modules.task.dto.TaskDto;
import com.mypace.my_pace_server.modules.task.dto.TaskResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tags(@Tag(name = "V1 Task"))
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api/v1/task")
public class TaskController {

  private final TaskService taskService;

  /***  Register apis ***/
  @PostMapping(value = "/register")
  public ResponseEntity<TaskResponseDto> createTask(@RequestBody TaskDto task) {
    task.setId(UUID.randomUUID().toString());
    return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(task));
  }

  /***  Update apis ***/
  @PutMapping(value = "/update/{taskId}")
  public ResponseEntity<TaskResponseDto> updateTask(
      @RequestBody TaskDto task, @PathVariable String taskId) {
    return ResponseEntity.ok(taskService.updateTask(task, taskId));
  }

  /***  Delete apis ***/
  @DeleteMapping(value = "/delete/{taskId}")
  public void deleteTask(@PathVariable String taskId) {
    taskService.deleteTask(taskId);
  }

  /***  Get apis  ***/
  @GetMapping(value = "/lookup/search/all")
  public ResponseEntity<Iterable<TaskResponseDto>> getAllTasks() {
    return ResponseEntity.ok(taskService.getAllTasks());
  }

  /***  helpers ***/

}
