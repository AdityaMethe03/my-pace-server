package com.mypace.my_pace_server.modules.habit;

import com.mypace.my_pace_server.common.utils.DateUtils;
import com.mypace.my_pace_server.modules.habit.enums.HabitFrequencyEnum;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class HabitStreakCalculator {

  public Habit calculateHabitCurrentStreak(Habit habit) {
    LocalDate today = LocalDate.now();
    LocalDate lastCompleted = DateUtils.toLocalDate(habit.getLastCompleted());

    // ── 1. Already done today — do nothing ──────────────────────────────
    if (today.equals(lastCompleted)) {
      return habit;
    }

    // ── 2. First time ever completing this habit ─────────────────────────
    if (lastCompleted == null) {
      habit.setCurrentStreak(1);
      habit.setLongestStreak(1);
      habit.setLastCompleted(DateUtils.toDate(today));
      return habit;
    }

    // ── 3. Find the last expected date based on frequency ────────────────
    LocalDate lastExpectedDate = getLastExpectedDate(habit, today);

    if (lastExpectedDate == null) {
      habit.setCurrentStreak(1);
      habit.setLastCompleted(DateUtils.toDate(today));
      updateLongestStreak(habit);
      return habit;
    }

    // ── 4. Calculate streak ──────────────────────────────────────────────
    if (lastCompleted.equals(lastExpectedDate)) {
      habit.setCurrentStreak(habit.getCurrentStreak() + 1);
    } else if (lastCompleted.isBefore(lastExpectedDate)) {
      habit.setCurrentStreak(1);
    }

    // ── 5. Update lastCompleted and longestStreak ────────────────────────
    habit.setLastCompleted(DateUtils.toDate(today));
    updateLongestStreak(habit);

    return habit;
  }

  private LocalDate getLastExpectedDate(Habit habit, LocalDate today) {
    HabitFrequencyEnum frequency = habit.getFrequency();

    if (frequency == null) return null;

    switch (frequency) {
      case DAILY:
        {
          return today.minusDays(1);
        }

      case WEEKLY:
        {
          List<DayOfWeek> targetDays = habit.getTargetDays();
          if (targetDays == null || targetDays.isEmpty()) return null;

          // Sort targetDays by value (MON=1 ... SUN=7)
          List<DayOfWeek> sorted =
              targetDays.stream().sorted(Comparator.comparingInt(DayOfWeek::getValue)).toList();

          // Walk backwards from yesterday to find the most recent targetDay
          for (int i = 1; i <= 7; i++) {
            LocalDate candidate = today.minusDays(i);
            if (sorted.contains(candidate.getDayOfWeek())) {
              return candidate;
            }
          }
          return null;
        }

      case MONTHLY:
        {
          List<Integer> targetDates = habit.getTargetDates();
          if (targetDates == null || targetDates.isEmpty()) return null;

          List<Integer> sorted = targetDates.stream().sorted().toList();

          // Walk backwards from yesterday to find the most recent targetDate
          for (int i = 1; i <= 31; i++) {
            LocalDate candidate = today.minusDays(i);
            if (sorted.contains(candidate.getDayOfMonth())) {
              return candidate;
            }
          }
          return null;
        }

      default:
        return null;
    }
  }

  private void updateLongestStreak(Habit habit) {
    int current = habit.getCurrentStreak() != null ? habit.getCurrentStreak() : 0;
    int longest = habit.getLongestStreak() != null ? habit.getLongestStreak() : 0;
    if (current > longest) {
      habit.setLongestStreak(current);
    }
  }
}
