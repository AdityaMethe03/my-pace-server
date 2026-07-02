package com.mypace.my_pace_server.modules.activity.dto;

import com.mypace.my_pace_server.modules.activity.enums.ActivityCategoryEnum;
import com.mypace.my_pace_server.modules.activity.enums.ActivityStatusEnum;
import java.util.Date;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityDto {
  private String id;
  private String userId;
  private String title;
  private String description;
  private ActivityCategoryEnum category;
  private Date scheduledAt;
  private Integer duration;
  private ActivityStatusEnum status;
}
