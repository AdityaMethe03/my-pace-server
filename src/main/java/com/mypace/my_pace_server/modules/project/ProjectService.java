package com.mypace.my_pace_server.modules.project;

import com.mypace.my_pace_server.modules.project.dto.ProjectDto;
import com.mypace.my_pace_server.modules.project.dto.ProjectResponseDto;

public interface ProjectService {

  // Create a project
  ProjectResponseDto createProject(ProjectDto project);

  // update project
  ProjectResponseDto updateProject(ProjectDto project, String projectId);

  // delete a project
  void deleteProject(String projectId);

  // get all projects
  Iterable<ProjectResponseDto> getAllProjects();
}
