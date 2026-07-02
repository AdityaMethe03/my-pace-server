package com.mypace.my_pace_server.modules.task.dto;

import com.mypace.my_pace_server.common.enums.PriorityEnum;
import com.mypace.my_pace_server.modules.task.enums.TaskStatusEnum;
import java.util.Date;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskResponseDto {
  private String id;
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
