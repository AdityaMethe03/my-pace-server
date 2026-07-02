package com.mypace.my_pace_server.modules.activity;

import com.mypace.my_pace_server.modules.activity.dto.ActivityDto;
import com.mypace.my_pace_server.modules.activity.dto.ActivityResponseDto;

public interface ActivityService {

  // Create an activity
  ActivityResponseDto createActivity(ActivityDto activity);

  // update activity
  ActivityResponseDto updateActivity(ActivityDto activity, String activityId);

  // delete an activity
  void deleteActivity(String activityId);

  // get all activities
  Iterable<ActivityResponseDto> getAllActivities();
}
