package com.mypace.my_pace_server.modules.habit;

import com.mypace.my_pace_server.modules.habit.dto.HabitDto;
import com.mypace.my_pace_server.modules.habit.dto.HabitResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tags(@Tag(name = "V1 Habit"))
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api/v1/habit")
public class HabitController {

  private final HabitService habitService;

  /***  Register apis ***/
  @PostMapping(value = "/register")
  public ResponseEntity<HabitResponseDto> createHabit(@RequestBody HabitDto habit) {
    habit.setId(UUID.randomUUID().toString());
    return ResponseEntity.status(HttpStatus.CREATED).body(habitService.createHabit(habit));
  }

  /***  Update apis ***/
  @PutMapping(value = "/update/{habitId}")
  public ResponseEntity<HabitResponseDto> updateHabit(
      @RequestBody HabitDto habit, @PathVariable String habitId) {
    return ResponseEntity.ok(habitService.updateHabit(habit, habitId));
  }

  /***  Delete apis ***/
  @DeleteMapping(value = "/delete/{habitId}")
  public void deleteHabit(@PathVariable String habitId) {
    habitService.deleteHabit(habitId);
  }

  /***  Get apis  ***/
  @GetMapping(value = "/lookup/search/all")
  public ResponseEntity<Iterable<HabitResponseDto>> getAllHabits() {
    return ResponseEntity.ok(habitService.getAllHabits());
  }

  /***  helpers ***/

}
