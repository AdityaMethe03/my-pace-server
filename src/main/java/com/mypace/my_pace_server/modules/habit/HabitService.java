package com.mypace.my_pace_server.modules.habit;

import com.mypace.my_pace_server.modules.habit.dto.HabitDto;
import com.mypace.my_pace_server.modules.habit.dto.HabitResponseDto;

public interface HabitService {

  // Create a habit
  HabitResponseDto createHabit(HabitDto habit);

  // update habit
  HabitResponseDto updateHabit(HabitDto habit, String habitId);

  // delete a habit
  void deleteHabit(String habitId);

  // get all habits
  Iterable<HabitResponseDto> getAllHabits();
}
