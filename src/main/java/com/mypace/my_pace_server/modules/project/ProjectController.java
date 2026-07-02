package com.mypace.my_pace_server.modules.project;

import com.mypace.my_pace_server.modules.project.dto.ProjectDto;
import com.mypace.my_pace_server.modules.project.dto.ProjectResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tags(@Tag(name = "V1 Project"))
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api/v1/projects")
public class ProjectController {

  private final ProjectService projectService;

  /***  Register apis ***/
  @PostMapping(value = "/register")
  public ResponseEntity<ProjectResponseDto> createProject(@RequestBody ProjectDto project) {
    project.setId(UUID.randomUUID().toString());
    return ResponseEntity.status(HttpStatus.CREATED).body(projectService.createProject(project));
  }

  /***  Update apis ***/
  @PutMapping(value = "/update/{projectId}")
  public ResponseEntity<ProjectResponseDto> updateProject(
      @RequestBody ProjectDto project, @PathVariable String projectId) {
    return ResponseEntity.ok(projectService.updateProject(project, projectId));
  }

  /***  Delete apis ***/
  @DeleteMapping(value = "/delete/{projectId}")
  public void deleteProject(@PathVariable String projectId) {
    projectService.deleteProject(projectId);
  }

  /***  Get apis  ***/
  @GetMapping(value = "/lookup/search/all")
  public ResponseEntity<Iterable<ProjectResponseDto>> getAllProjects() {
    return ResponseEntity.ok(projectService.getAllProjects());
  }

  /***  helpers ***/

}
