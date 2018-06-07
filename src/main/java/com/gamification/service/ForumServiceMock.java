package com.gamification.service;

import com.gamification.repository.AchievementStorage;

public class ForumServiceMock implements ForumService {

    private boolean exceptionWhenCallAddTopic;

    @Override
    public void addTopic(String user, String topic) {
        if (exceptionWhenCallAddTopic) throw new RuntimeException("Erro ao chamar AddTopic");
    }

    @Override
    public void addComment(String user, String topic, String comment) {

    }

    @Override
    public void likeTopic(String user, String topic, String topicUser) {

    }

    @Override
    public void likeComment(String user, String topic, String comment, String commentUser) {

    }

    @Override
    public AchievementStorage getAchievementStorage() {
        return null;
    }

    public void setExceptionWhenCallAddTopic() {
        exceptionWhenCallAddTopic = true;
    }
}
