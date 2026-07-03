package com.mypace.my_pace_server.modules.reminder;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReminderRepository extends MongoRepository<Reminder, String> {}
