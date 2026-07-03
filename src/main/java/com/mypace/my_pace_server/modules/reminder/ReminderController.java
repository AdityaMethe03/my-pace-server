package com.mypace.my_pace_server.modules.reminder;

import com.mypace.my_pace_server.modules.reminder.dto.ReminderDto;
import com.mypace.my_pace_server.modules.reminder.dto.ReminderResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Tags(@Tag(name = "V1 Reminder"))
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api/v1/reminder")
public class ReminderController {

  private final ReminderService reminderService;

  /***  Register apis ***/
  @PostMapping(value = "/register")
  public ResponseEntity<ReminderResponseDto> createReminder(@RequestBody ReminderDto reminder) {
    reminder.setId(UUID.randomUUID().toString());
    return ResponseEntity.status(HttpStatus.CREATED).body(reminderService.createReminder(reminder));
  }

  /***  Update apis ***/
  @PutMapping(value = "/update/{reminderId}")
  public ResponseEntity<ReminderResponseDto> updateReminder(
          @RequestBody ReminderDto reminder, @PathVariable String reminderId) {
    return ResponseEntity.ok(reminderService.updateReminder(reminder, reminderId));
  }

  /***  Delete apis ***/
  @DeleteMapping(value = "/delete/{reminderId}")
  public void deleteReminder(@PathVariable String reminderId) {
    reminderService.deleteReminder(reminderId);
  }

  /***  Get apis  ***/
  @GetMapping(value = "/lookup/search/all")
  public ResponseEntity<Iterable<ReminderResponseDto>> getAllReminders() {
    return ResponseEntity.ok(reminderService.getAllReminders());
  }

  /***  helpers ***/

}
