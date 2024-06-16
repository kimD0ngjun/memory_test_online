package com.example.mini_project.domain.game.service;

import com.example.mini_project.domain.user.entity.User;

import java.util.List;

public interface RecordService {

    void createRecord(User user, int level, int gameScore);

    List<Record> getRecords(User user);

    void deleteRecord(Long recordId);
}
