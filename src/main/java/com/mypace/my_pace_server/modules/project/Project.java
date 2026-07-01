package com.mypace.my_pace_server.modules.project;

import com.mypace.my_pace_server.modules.project.enums.CategoryEnum;
import com.mypace.my_pace_server.modules.project.enums.PriorityEnum;
import com.mypace.my_pace_server.modules.project.enums.ProjectStatusEnum;
import java.util.Date;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "projects")
public class Project {
  @Id private String id;
  private String userId;
  private String title;
  private String description;
  private Date createdAt;
  private Date updatedAt;
  private ProjectStatusEnum status;
  private Date deadline;
  private PriorityEnum priority;
  private CategoryEnum category;
  private String color;
}
