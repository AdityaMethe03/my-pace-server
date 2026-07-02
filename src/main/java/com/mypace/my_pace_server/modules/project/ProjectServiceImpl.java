package com.mypace.my_pace_server.modules.project;

import com.mypace.my_pace_server.common.exceptions.ResourceNotFoundException;
import com.mypace.my_pace_server.modules.project.dto.ProjectDto;
import com.mypace.my_pace_server.modules.project.dto.ProjectResponseDto;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
  private final ModelMapper modelMapper;
  private final ProjectRepository projectRepository;

  @Override
  public ProjectResponseDto createProject(ProjectDto projectDto) {
    Project project = modelMapper.map(projectDto, Project.class);
    project.setCreatedAt(new Date());
    Project savedProject = projectRepository.save(project);
    return modelMapper.map(savedProject, ProjectResponseDto.class);
  }

  @Override
  public ProjectResponseDto updateProject(ProjectDto projectDto, String projectId) {
    Project existingProject =
        projectRepository
            .findById(projectId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Project with given id does not exist."));

    existingProject.setTitle(projectDto.getTitle());
    existingProject.setDescription(projectDto.getDescription());
    existingProject.setUpdatedAt(new Date());
    existingProject.setStatus(projectDto.getStatus());
    existingProject.setDeadline(projectDto.getDeadline());
    existingProject.setPriority(projectDto.getPriority());
    existingProject.setCategory(projectDto.getCategory());
    existingProject.setColor(projectDto.getColor());

    Project project = projectRepository.save(existingProject);
    return modelMapper.map(project, ProjectResponseDto.class);
  }

  @Override
  public void deleteProject(String projectId) {
    Project project =
        projectRepository
            .findById(projectId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Project with given id does not exist."));
    projectRepository.delete(project);
  }

  @Override
  public Iterable<ProjectResponseDto> getAllProjects() {
    return projectRepository.findAll().stream()
        .map(project -> modelMapper.map(project, ProjectResponseDto.class))
        .toList();
  }
}
