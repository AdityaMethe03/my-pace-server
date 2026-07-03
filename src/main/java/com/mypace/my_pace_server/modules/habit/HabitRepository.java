package com.mypace.my_pace_server.modules.habit;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface HabitRepository extends MongoRepository<Habit, String> {}
