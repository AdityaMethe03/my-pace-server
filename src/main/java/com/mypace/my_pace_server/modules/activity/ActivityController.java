package com.mypace.my_pace_server.modules.activity;

import com.mypace.my_pace_server.modules.activity.dto.ActivityDto;
import com.mypace.my_pace_server.modules.activity.dto.ActivityResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tags(@Tag(name = "V1 Activity"))
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api/v1/activity")
public class ActivityController {

  private final ActivityService activityService;

  /***  Register apis ***/
  @PostMapping(value = "/register")
  public ResponseEntity<ActivityResponseDto> createActivity(@RequestBody ActivityDto activity) {
    activity.setId(UUID.randomUUID().toString());
    return ResponseEntity.status(HttpStatus.CREATED).body(activityService.createActivity(activity));
  }

  /***  Update apis ***/
  @PutMapping(value = "/update/{activityId}")
  public ResponseEntity<ActivityResponseDto> updateActivity(
      @RequestBody ActivityDto activity, @PathVariable String activityId) {
    return ResponseEntity.ok(activityService.updateActivity(activity, activityId));
  }

  /***  Delete apis ***/
  @DeleteMapping(value = "/delete/{activityId}")
  public void deleteActivity(@PathVariable String activityId) {
    activityService.deleteActivity(activityId);
  }

  /***  Get apis  ***/
  @GetMapping(value = "/lookup/search/all")
  public ResponseEntity<Iterable<ActivityResponseDto>> getAllActivities() {
    return ResponseEntity.ok(activityService.getAllActivities());
  }

  /***  helpers ***/

}
