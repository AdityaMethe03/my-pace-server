package com.mypace.my_pace_server.modules.habit;

import com.mypace.my_pace_server.modules.habit.enums.HabitFrequencyEnum;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.DayOfWeek;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "habits")
public class Habit {
  @Id private String id;
  private String userId;
  private String title;
  private String description;
  private Date createdAt;
  private Date updatedAt;
  private HabitFrequencyEnum frequency;
  private List<DayOfWeek> targetDays;
  private Integer currentStreak;
  private Integer longestStreak;
  private Date lastCompleted;
  private List<Date> completionLog;
}
