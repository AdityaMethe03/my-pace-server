package com.mypace.my_pace_server.modules.reminder.dto;

import com.mypace.my_pace_server.modules.reminder.enums.LinkedTypeEnum;
import com.mypace.my_pace_server.modules.reminder.enums.ReminderRepeatEnum;
import com.mypace.my_pace_server.modules.reminder.enums.ReminderStatusEnum;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReminderResponseDto {
  private String id;
  private String userId;
  private String title;
  private String description;
  private Date createdAt;
  private Date updatedAt;
  private Date scheduledAt;
  private ReminderRepeatEnum repeat;
  private LinkedTypeEnum linkedType;
  private String linkedId;
  private ReminderStatusEnum status;
}
