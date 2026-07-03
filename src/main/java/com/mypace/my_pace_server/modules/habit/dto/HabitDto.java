package com.mypace.my_pace_server.modules.habit.dto;

import com.mypace.my_pace_server.modules.habit.enums.HabitFrequencyEnum;
import java.time.DayOfWeek;
import java.util.List;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HabitDto {
  private String id;
  private String userId;
  private String title;
  private String description;
  private boolean isActive;
  private HabitFrequencyEnum frequency;
  private List<DayOfWeek> targetDays;
  private List<Integer> targetDates;
}
