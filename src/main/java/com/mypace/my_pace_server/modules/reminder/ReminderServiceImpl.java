package com.mypace.my_pace_server.modules.reminder;

import com.mypace.my_pace_server.common.exceptions.ResourceNotFoundException;
import com.mypace.my_pace_server.modules.reminder.dto.ReminderDto;
import com.mypace.my_pace_server.modules.reminder.dto.ReminderResponseDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class ReminderServiceImpl implements ReminderService {

    private final ModelMapper modelMapper;
    private final ReminderRepository reminderRepository;

    @Override
    public ReminderResponseDto createReminder(ReminderDto reminderDto) {
        Reminder reminder = modelMapper.map(reminderDto, Reminder.class);
        reminder.setCreatedAt(new Date());
        Reminder savedReminder = reminderRepository.save(reminder);
        return modelMapper.map(savedReminder, ReminderResponseDto.class);
    }

    @Override
    public ReminderResponseDto updateReminder(ReminderDto reminderDto, String reminderId) {
        Reminder existingReminder =
                reminderRepository
                        .findById(reminderId)
                        .orElseThrow(
                                () -> new ResourceNotFoundException("Reminder with given id does not exist."));

        existingReminder.setTitle(reminderDto.getTitle());
        existingReminder.setDescription(reminderDto.getDescription());
        existingReminder.setUpdatedAt(new Date());
        existingReminder.setScheduledAt(reminderDto.getScheduledAt());
        existingReminder.setRepeat(reminderDto.getRepeat());
        existingReminder.setLinkedType(reminderDto.getLinkedType());
        existingReminder.setLinkedId(reminderDto.getLinkedId());
        existingReminder.setStatus(reminderDto.getStatus());

        Reminder reminder = reminderRepository.save(existingReminder);
        return modelMapper.map(reminder, ReminderResponseDto.class);
    }

    @Override
    public void deleteReminder(String reminderId) {
        Reminder reminder =
                reminderRepository
                        .findById(reminderId)
                        .orElseThrow(
                                () -> new ResourceNotFoundException("Reminder with given id does not exist."));
        reminderRepository.delete(reminder);
    }

    @Override
    public Iterable<ReminderResponseDto> getAllReminders() {
        return reminderRepository.findAll().stream()
                .map(reminder -> modelMapper.map(reminder, ReminderResponseDto.class))
                .toList();
    }
}
