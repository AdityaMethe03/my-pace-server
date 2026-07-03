package com.mypace.my_pace_server.modules.reminder;

import com.mypace.my_pace_server.modules.reminder.dto.ReminderDto;
import com.mypace.my_pace_server.modules.reminder.dto.ReminderResponseDto;

public interface ReminderService {

    // Create a reminder
    ReminderResponseDto createReminder(ReminderDto reminder);

    // update reminder
    ReminderResponseDto updateReminder(ReminderDto reminder, String reminderId);

    // delete a reminder
    void deleteReminder(String reminderId);

    // get all reminders
    Iterable<ReminderResponseDto> getAllReminders();
}
