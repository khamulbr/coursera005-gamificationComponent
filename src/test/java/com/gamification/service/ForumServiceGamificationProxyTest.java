package com.gamification.service;

import com.gamification.model.Achievement;
import com.gamification.model.Badge;
import com.gamification.model.Points;
import com.gamification.repository.AchievementStorage;
import com.gamification.repository.AchievementStorageFactory;
import com.gamification.repository.MemoryAchievementStorage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.junit.Assert.*;

public class ForumServiceGamificationProxyTest {

    private AchievementStorage achievementStorage;
    private MemoryAchievementStorage memoryAchievementStorage = new MemoryAchievementStorage();

    @Before
    public void setUp() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        AchievementStorageFactory.setAchievementStorage(memoryAchievementStorage);
        achievementStorage = AchievementStorageFactory.getAchievementStorage();
    }

    @After
    public void tearDown() {
        AchievementStorageFactory.clearAchievementStorage();
    }

    @Test
    public void addOneTopic() {
        ForumServiceMock forumServiceMock = new ForumServiceMock();
        ForumService forumService = new ForumServiceGamificationProxy(forumServiceMock, achievementStorage);

        forumService.addTopic("user1", "topic1");

        Points pointsGenerated = (Points) forumService.getAchievementStorage().getAchievement("user1", "CREATION");
        Badge badgeGenerated = (Badge) forumService.getAchievementStorage().getAchievement("user1", "I CAN TALK");
        assertEquals(5, pointsGenerated.getValue());
        assertEquals("I CAN TALK", badgeGenerated.getName());
    }

    @Test
    public void addOneComment() {
        ForumServiceMock forumServiceMock = new ForumServiceMock();
        ForumService forumService = new ForumServiceGamificationProxy(forumServiceMock, achievementStorage);

        forumService.addComment("user1", "topic1", "comment1");

        Points pointsGenerated = (Points) forumService.getAchievementStorage().getAchievement("user1", "PARTICIPATION");
        Badge badgeGenerated = (Badge) forumService.getAchievementStorage().getAchievement("user1", "LET ME ADD");

        assertEquals(3, pointsGenerated.getValue());
        assertEquals("LET ME ADD", badgeGenerated.getName());
    }

    @Test
    public void addThreeCommentsFromDifferentUsers() {
        ForumServiceMock forumServiceMock = new ForumServiceMock();
        ForumService forumService = new ForumServiceGamificationProxy(forumServiceMock, achievementStorage);

        forumService.addComment("user1", "topic1", "comment1");
        forumService.addComment("user1", "topic2", "comment2");
        forumService.addComment("user2", "topic1", "comment3");

        Points pointsGenerated = (Points) forumService.getAchievementStorage().getAchievement("user1", "PARTICIPATION");
        Badge badgeGenerated = (Badge) forumService.getAchievementStorage().getAchievement("user1", "LET ME ADD");

        assertEquals(6, pointsGenerated.getValue());
        assertEquals("LET ME ADD", badgeGenerated.getName());
    }

    @Test
    public void likeOneTopic() {
        ForumServiceMock forumServiceMock = new ForumServiceMock();
        ForumService forumService = new ForumServiceGamificationProxy(forumServiceMock, achievementStorage);

        forumService.likeTopic("user1", "topic1", "user2");

        Points pointsGenerated = (Points) forumService.getAchievementStorage().getAchievement("user1", "CREATION");
        assertEquals(1, pointsGenerated.getValue());
    }

    @Test
    public void likeOneComment() {
        ForumServiceMock forumServiceMock = new ForumServiceMock();
        ForumService forumService = new ForumServiceGamificationProxy(forumServiceMock, achievementStorage);

        forumService.likeComment("user1", "topic1", "comment1", "user2");

        Points pointsGenerated = (Points) forumService.getAchievementStorage().getAchievement("user1", "PARTICIPATION");

        assertEquals(1, pointsGenerated.getValue());
    }


    @Test
    public void addTwoTopicsForSameUser() {
        ForumServiceMock forumServiceMock = new ForumServiceMock();
        ForumService forumService = new ForumServiceGamificationProxy(forumServiceMock, achievementStorage);

        forumService.addTopic("user1", "topic1");
        forumService.addTopic("user1", "topic2");

        Points pointsGenerated = (Points) forumService.getAchievementStorage().getAchievement("user1", "CREATION");
        List<Achievement> achievementList = forumService.getAchievementStorage().getAchievements("user1");

        assertEquals(10, pointsGenerated.getValue());
        assertEquals(1, achievementList.stream().filter(it -> it.getName().equals("I CAN TALK")).count());
    }

    @Test
    public void invokeSomeMethodsAndSeeIfResultIsExpected() {
        ForumServiceMock forumServiceMock = new ForumServiceMock();
        ForumService forumService = new ForumServiceGamificationProxy(forumServiceMock, achievementStorage);

        forumService.addTopic("user1", "topic1");
        forumService.addTopic("user1", "topic2");
        forumService.addComment("user1", "topic1", "comment1");
        forumService.addComment("user1", "topic2", "comment2");
        forumService.likeTopic("user1", "topic1", "user2");
        forumService.likeTopic("user1", "topic2", "user2");
        forumService.likeComment("user1", "topic1", "comment1", "user2");
        forumService.likeComment("user1", "topic1", "comment2", "user3");

        Points pointsGenerated = (Points) forumService.getAchievementStorage().getAchievement("user1", "CREATION");
        List<Achievement> achievementList = forumService.getAchievementStorage().getAchievements("user1");

        assertEquals(20, achievementList.stream().filter(it -> it instanceof Points).mapToInt(it -> ((Points) it).getValue()).sum());
        assertEquals(1, achievementList.stream().filter(it -> it.getName().equals("I CAN TALK")).count());
        assertEquals(1, achievementList.stream().filter(it -> it.getName().equals("LET ME ADD")).count());
    }

    @Test
    public void mockedClassShouldThrowExceptionAndProxyWontBeCalled(){
        ForumServiceMock forumServiceMock = new ForumServiceMock();
        forumServiceMock.setExceptionWhenCallAddTopic();
        ForumService forumService = new ForumServiceGamificationProxy(forumServiceMock, achievementStorage);

        try {
            forumService.addTopic("user1", "topic1");
        } catch (RuntimeException e ) {
            e.printStackTrace();
        }

        List<Achievement> achievementList = forumService.getAchievementStorage().getAchievements("user1");

        assertNull(achievementList);
    }

    @Test
    public void creationObservedActivated(){
        ForumServiceMock forumServiceMock = new ForumServiceMock();
        ForumService forumService = new ForumServiceGamificationProxy(forumServiceMock, achievementStorage);
        CreationObserver creationObserver = new CreationObserver(achievementStorage);
        achievementStorage.addObserver(creationObserver);

        forumService.addTopic("user1", "topic1");
        forumService.addTopic("user1", "topic2");
        forumService.addTopic("user1", "topic3");
        forumService.addTopic("user1", "topic4");
        forumService.addTopic("user1", "topic5");
        forumService.addTopic("user1", "topic6");
        forumService.addTopic("user1", "topic7");
        forumService.addTopic("user1", "topic8");
        forumService.addTopic("user1", "topic9");
        forumService.addTopic("user1", "topic10");
        forumService.addTopic("user1", "topic11");
        forumService.addTopic("user1", "topic12");
        forumService.addTopic("user1", "topic13");
        forumService.addTopic("user1", "topic14");
        forumService.addTopic("user1", "topic15");
        forumService.addTopic("user1", "topic16");
        forumService.addTopic("user1", "topic17");
        forumService.addTopic("user1", "topic18");
        forumService.addTopic("user1", "topic19");
        forumService.addTopic("user1", "topic20");

        List<Achievement> achievementList = forumService.getAchievementStorage().getAchievements("user1");
        assertEquals(1, achievementList.stream().filter(it -> it.getName().equals("INVENTOR")).count());

        achievementStorage.removeObserver(creationObserver);
    }

    @Test
    public void participationObservedActivated(){
        ForumServiceMock forumServiceMock = new ForumServiceMock();
        ForumService forumService = new ForumServiceGamificationProxy(forumServiceMock, achievementStorage);
        ParticipationObserver participationObserver = new ParticipationObserver(achievementStorage);
        achievementStorage.addObserver(participationObserver);

        forumService.addComment("user1", "topic1", "comment1");
        forumService.addComment("user1", "topic1", "comment2");
        forumService.addComment("user1", "topic1", "comment3");
        forumService.addComment("user1", "topic1", "comment4");
        forumService.addComment("user1", "topic1", "comment5");
        forumService.addComment("user1", "topic1", "comment6");
        forumService.addComment("user1", "topic1", "comment7");
        forumService.addComment("user1", "topic1", "comment8");
        forumService.addComment("user1", "topic1", "comment9");
        forumService.addComment("user1", "topic1", "comment10");
        forumService.addComment("user1", "topic1", "comment11");
        forumService.addComment("user1", "topic1", "comment12");
        forumService.addComment("user1", "topic1", "comment13");
        forumService.addComment("user1", "topic1", "comment14");
        forumService.addComment("user1", "topic1", "comment15");
        forumService.addComment("user1", "topic1", "comment16");
        forumService.addComment("user1", "topic1", "comment17");
        forumService.addComment("user1", "topic1", "comment18");
        forumService.addComment("user1", "topic1", "comment19");
        forumService.addComment("user1", "topic1", "comment20");
        forumService.addComment("user1", "topic1", "comment21");
        forumService.addComment("user1", "topic1", "comment22");
        forumService.addComment("user1", "topic1", "comment23");
        forumService.addComment("user1", "topic1", "comment24");
        forumService.addComment("user1", "topic1", "comment25");
        forumService.addComment("user1", "topic1", "comment26");
        forumService.addComment("user1", "topic1", "comment27");
        forumService.addComment("user1", "topic1", "comment28");
        forumService.addComment("user1", "topic1", "comment29");
        forumService.addComment("user1", "topic1", "comment30");
        forumService.addComment("user1", "topic1", "comment31");
        forumService.addComment("user1", "topic1", "comment32");
        forumService.addComment("user1", "topic1", "comment33");
        forumService.addComment("user1", "topic1", "comment34");

        List<Achievement> achievementList = forumService.getAchievementStorage().getAchievements("user1");
        assertEquals(1, achievementList.stream().filter(it -> it.getName().equals("PART OF THE COMMUNITY")).count());
        achievementStorage.removeObserver(participationObserver);
    }

}