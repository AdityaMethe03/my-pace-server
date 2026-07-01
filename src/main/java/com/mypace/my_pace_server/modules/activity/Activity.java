package com.mypace.my_pace_server.modules.activity;

import com.mypace.my_pace_server.modules.activity.enums.ActivityCategoryEnum;
import com.mypace.my_pace_server.modules.activity.enums.ActivityStatusEnum;
import com.mypace.my_pace_server.modules.project.enums.CategoryEnum;
import com.mypace.my_pace_server.common.enums.PriorityEnum;
import com.mypace.my_pace_server.modules.project.enums.ProjectStatusEnum;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "activities")
public class Activity {
  @Id private String id;
  private String userId;
  private String title;
  private String description;
  private Date createdAt;
  private Date updatedAt;
  private ActivityCategoryEnum category;
  private Date scheduledAt;
  private Integer duration;
  private ActivityStatusEnum status;
}
