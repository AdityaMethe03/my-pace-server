package com.mypace.my_pace_server.modules.activity;

import com.mypace.my_pace_server.common.exceptions.ResourceNotFoundException;
import com.mypace.my_pace_server.modules.activity.dto.ActivityDto;
import com.mypace.my_pace_server.modules.activity.dto.ActivityResponseDto;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {
  private final ModelMapper modelMapper;
  private final ActivityRepository activityRepository;

  @Override
  public ActivityResponseDto createActivity(ActivityDto activityDto) {
    Activity activity = modelMapper.map(activityDto, Activity.class);
    activity.setCreatedAt(new Date());
    Activity savedActivity = activityRepository.save(activity);
    return modelMapper.map(savedActivity, ActivityResponseDto.class);
  }

  @Override
  public ActivityResponseDto updateActivity(ActivityDto activityDto, String activityId) {
    Activity existingActivity =
        activityRepository
            .findById(activityId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Activity with given id does not exist."));

    existingActivity.setTitle(activityDto.getTitle());
    existingActivity.setDescription(activityDto.getDescription());
    existingActivity.setUpdatedAt(new Date());
    existingActivity.setCategory(activityDto.getCategory());
    existingActivity.setScheduledAt(activityDto.getScheduledAt());
    existingActivity.setDuration(activityDto.getDuration());
    existingActivity.setStatus(activityDto.getStatus());

    Activity activity = activityRepository.save(existingActivity);
    return modelMapper.map(activity, ActivityResponseDto.class);
  }

  @Override
  public void deleteActivity(String activityId) {
    Activity activity =
        activityRepository
            .findById(activityId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Activity with given id does not exist."));
    activityRepository.delete(activity);
  }

  @Override
  public Iterable<ActivityResponseDto> getAllActivities() {
    return activityRepository.findAll().stream()
        .map(activity -> modelMapper.map(activity, ActivityResponseDto.class))
        .toList();
  }
}
