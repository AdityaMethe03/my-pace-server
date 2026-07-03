package com.mypace.my_pace_server.modules.habit;

import com.mypace.my_pace_server.common.exceptions.ResourceNotFoundException;
import com.mypace.my_pace_server.modules.habit.dto.HabitDto;
import com.mypace.my_pace_server.modules.habit.dto.HabitResponseDto;
import com.mypace.my_pace_server.modules.habit.enums.HabitFrequencyEnum;
import java.time.DayOfWeek;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HabitServiceImpl implements HabitService {
  private final ModelMapper modelMapper;
  private final HabitRepository habitRepository;
  private final HabitStreakCalculator habitStreakCalculator;

  @Override
  public HabitResponseDto createHabit(HabitDto habitDto) {
    Habit habit = modelMapper.map(habitDto, Habit.class);
    habit.setCreatedAt(new Date());

    habit = habitStreakCalculator.calculateHabitCurrentStreak(habit);

    if (habitDto.getFrequency() == HabitFrequencyEnum.DAILY) {
      habit.setTargetDays(
          List.of(
              DayOfWeek.MONDAY,
              DayOfWeek.TUESDAY,
              DayOfWeek.WEDNESDAY,
              DayOfWeek.THURSDAY,
              DayOfWeek.FRIDAY,
              DayOfWeek.SATURDAY,
              DayOfWeek.SUNDAY));
      habit.setTargetDates(Collections.emptyList());
    } else if (habitDto.getFrequency() == HabitFrequencyEnum.WEEKLY) {
      habit.setTargetDays(habitDto.getTargetDays());
      habit.setTargetDates(Collections.emptyList());
    } else {
      habit.setTargetDays(Collections.emptyList());
      habit.setTargetDates(habitDto.getTargetDates());
    }

    Habit savedHabit = habitRepository.save(habit);
    return modelMapper.map(savedHabit, HabitResponseDto.class);
  }

  @Override
  public HabitResponseDto updateHabit(HabitDto habitDto, String habitId) {
    Habit existingHabit =
        habitRepository
            .findById(habitId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Habit with given id does not exist."));

    existingHabit.setTitle(habitDto.getTitle());
    existingHabit.setDescription(habitDto.getDescription());
    existingHabit.setUpdatedAt(new Date());
    existingHabit.setActive(habitDto.isActive());
    existingHabit.setFrequency(habitDto.getFrequency());

    existingHabit = habitStreakCalculator.calculateHabitCurrentStreak(existingHabit);

    if (habitDto.getFrequency() == HabitFrequencyEnum.DAILY) {
      existingHabit.setTargetDays(
          List.of(
              DayOfWeek.MONDAY,
              DayOfWeek.TUESDAY,
              DayOfWeek.WEDNESDAY,
              DayOfWeek.THURSDAY,
              DayOfWeek.FRIDAY,
              DayOfWeek.SATURDAY,
              DayOfWeek.SUNDAY));
      existingHabit.setTargetDates(Collections.emptyList());
    } else if (habitDto.getFrequency() == HabitFrequencyEnum.WEEKLY) {
      existingHabit.setTargetDays(habitDto.getTargetDays());
      existingHabit.setTargetDates(Collections.emptyList());
    } else {
      existingHabit.setTargetDays(Collections.emptyList());
      existingHabit.setTargetDates(habitDto.getTargetDates());
    }

    Habit habit = habitRepository.save(existingHabit);
    return modelMapper.map(habit, HabitResponseDto.class);
  }

  @Override
  public void deleteHabit(String habitId) {
    Habit habit =
        habitRepository
            .findById(habitId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Habit with given id does not exist."));
    habitRepository.delete(habit);
  }

  @Override
  public Iterable<HabitResponseDto> getAllHabits() {
    return habitRepository.findAll().stream()
        .map(habit -> modelMapper.map(habit, HabitResponseDto.class))
        .toList();
  }
}
