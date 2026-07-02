package com.mypace.my_pace_server.modules.project.dto;

import com.mypace.my_pace_server.common.enums.PriorityEnum;
import com.mypace.my_pace_server.modules.project.enums.CategoryEnum;
import com.mypace.my_pace_server.modules.project.enums.ProjectStatusEnum;
import java.util.Date;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectDto {
  private String id;
  private String userId;
  private String title;
  private String description;
  private ProjectStatusEnum status;
  private Date deadline;
  private PriorityEnum priority;
  private CategoryEnum category;
  private String color;
}
