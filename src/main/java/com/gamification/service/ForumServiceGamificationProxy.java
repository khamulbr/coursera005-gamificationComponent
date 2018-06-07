package com.gamification.service;

import com.gamification.model.Badge;
import com.gamification.model.Points;
import com.gamification.repository.AchievementStorage;

public class ForumServiceGamificationProxy implements ForumService {

    private ForumServiceMock forumServiceMock;

    private AchievementStorage achievementStorage;

    public ForumServiceGamificationProxy(ForumServiceMock forumServiceMock, AchievementStorage achievementStorage) {
        this.forumServiceMock = forumServiceMock;
        this.achievementStorage = achievementStorage;
    }

    public AchievementStorage getAchievementStorage() {
        return achievementStorage;
    }

    @Override
    public void addTopic(String user, String topic) {
        try {
            forumServiceMock.addTopic(user, topic);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return;
        }
        achievementStorage.addAchievement(user, new Points("CREATION", 5));
        achievementStorage.addAchievement(user, new Badge("I CAN TALK"));
    }

    @Override
    public void addComment(String user, String topic, String comment) {
        forumServiceMock.addComment(user, topic, comment);
        achievementStorage.addAchievement(user, new Points("PARTICIPATION", 3));
        achievementStorage.addAchievement(user, new Badge("LET ME ADD"));
    }

    @Override
    public void likeTopic(String user, String topic, String topicUser) {
        forumServiceMock.likeTopic(user, topic, topicUser);
        achievementStorage.addAchievement(user, new Points("CREATION", 1));
    }

    @Override
    public void likeComment(String user, String topic, String comment, String commentUser) {
        forumServiceMock.likeComment(user, topic, comment, commentUser);
        achievementStorage.addAchievement(user, new Points("PARTICIPATION", 1));
    }
}
