package com.mypace.my_pace_server.modules.habit.dto;

import com.mypace.my_pace_server.modules.habit.enums.HabitFrequencyEnum;
import java.time.DayOfWeek;
import java.util.Date;
import java.util.List;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HabitResponseDto {
  private String id;
  private String userId;
  private String title;
  private String description;
  private Date createdAt;
  private Date updatedAt;
  private boolean isActive;
  private HabitFrequencyEnum frequency;
  private List<DayOfWeek> targetDays;
  private List<Integer> targetDates;
  private Integer currentStreak;
  private Integer longestStreak;
  private Date lastCompleted;
}
