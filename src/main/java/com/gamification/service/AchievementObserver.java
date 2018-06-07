package com.gamification.service;

import com.gamification.model.Achievement;

public interface AchievementObserver {

    void achievementUpdate(String user, Achievement a);
}
