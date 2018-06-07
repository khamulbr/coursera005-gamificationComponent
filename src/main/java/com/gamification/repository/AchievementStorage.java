package com.gamification.repository;

import com.gamification.model.Achievement;
import com.gamification.service.AchievementObserver;

import java.util.List;

public interface AchievementStorage {

    void addAchievement(String user, Achievement a);

    List<Achievement> getAchievements(String user);

    Achievement getAchievement(String user, String achievementName);

    void addObserver(AchievementObserver achievementObserver);

    void removeObserver(AchievementObserver achievementObserver);

    void notifyObservers(String user, Achievement achievement);
}
