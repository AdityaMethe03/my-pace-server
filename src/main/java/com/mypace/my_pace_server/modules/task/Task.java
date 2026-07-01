package com.mypace.my_pace_server.modules.task;

import com.mypace.my_pace_server.common.enums.PriorityEnum;
import com.mypace.my_pace_server.modules.task.enums.TaskStatusEnum;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "tasks")
public class Task {
  @Id private String id;
  private String userId;
  private String title;
  private String description;
  private Date createdAt;
  private Date updatedAt;
  private String projectId;
  private TaskStatusEnum status;
  private PriorityEnum priority;
  private Date dueDate;
}
