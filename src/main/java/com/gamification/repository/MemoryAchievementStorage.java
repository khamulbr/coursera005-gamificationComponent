package com.gamification.repository;

import com.gamification.model.Achievement;
import com.gamification.service.AchievementObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MemoryAchievementStorage implements AchievementStorage {

    private HashMap<String, List<Achievement>> achievements = new HashMap<>();

    private List<AchievementObserver> achievementObservers = new ArrayList<>();

    public MemoryAchievementStorage() {
    }

    @Override
    public void addAchievement(String user, Achievement a) {
        List<Achievement> achievementList = getAchievements(user);

        if (achievementList == null || achievementList.isEmpty()) {
            achievementList = new ArrayList<>();
            achievementList.add(a);
        } else if (getAchievement(user, a.getName())==null) {
            achievementList.add(a);
        } else {
            Achievement achievement = getAchievement(user, a.getName());
            achievement.process(a);
            achievementList.set(achievementList.indexOf(achievement), achievement);
            notifyObservers(user, achievement);
        }
        achievements.put(user, achievementList);
    }

    @Override
    public List<Achievement> getAchievements(String user) {
        return achievements.get(user);
    }

    @Override
    public Achievement getAchievement(String user, String achievementName) {
        return getAchievements(user).stream().filter(it -> it.getName().equals(achievementName)).findFirst().orElse(null);
    }

    @Override
    public void addObserver(AchievementObserver achievementObserver){
        achievementObservers.add(achievementObserver);
    }

    @Override
    public void removeObserver(AchievementObserver achievementObserver){
        achievementObservers.remove(achievementObserver);
    }

    @Override
    public void notifyObservers(String user, Achievement achievement){
        Iterator it = achievementObservers.iterator();
        while(it.hasNext()){
            AchievementObserver o = (AchievementObserver)it.next();
            o.achievementUpdate(user, achievement);
        }
    }
}
