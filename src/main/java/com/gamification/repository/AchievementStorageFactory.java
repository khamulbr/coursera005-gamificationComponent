package com.gamification.repository;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class AchievementStorageFactory {

    private static AchievementStorage achievementStorage;

    private static Class<?> clazz = null;

    public static AchievementStorage getAchievementStorage() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (achievementStorage == null || (!clazz.getName().equals(achievementStorage.getClass().getName()))) {
            Class<?> clazz2 = Class.forName(clazz.getName());
            Constructor<?> constructor = clazz2.getConstructor();
            achievementStorage = (AchievementStorage) constructor.newInstance();
        }
        return achievementStorage;
    }

    public static void setAchievementStorage(AchievementStorage a){
        clazz = a.getClass();
    }

    public static void clearAchievementStorage() {
        achievementStorage = null;
    }
}
