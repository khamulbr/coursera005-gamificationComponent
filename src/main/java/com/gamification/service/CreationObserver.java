package com.gamification.service;

import com.gamification.model.Achievement;
import com.gamification.model.Badge;
import com.gamification.model.Points;
import com.gamification.repository.AchievementStorage;

public class CreationObserver implements AchievementObserver{

    private AchievementStorage achievementStorage;

    public CreationObserver(AchievementStorage achievementStorage) {
        this.achievementStorage = achievementStorage;
    }

    @Override
    public void achievementUpdate(String user, Achievement a) {
        if (a instanceof Points){
            Points points = (Points) a;
            if (a.getName().equals("CREATION") && points.getValue() >= 100){
                if (achievementStorage.getAchievement(user, "INVENTOR") == null) {
                    achievementStorage.addAchievement(user, new Badge("INVENTOR"));
                }
            }
        }
    }
}
