package com.mypace.my_pace_server.modules.project;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tags(@Tag(name = "V1 Project"))
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api/v1/projects")
public class ProjectController {

  private final ProjectService projectService;

  /***  Register apis ***/

  /***  Update apis ***/

  /***  Delete apis ***/

  /***  Get apis  ***/

  /***  helpers ***/

}
