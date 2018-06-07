package com.gamification.model;

public abstract class Achievement{

    protected String name;

    public Achievement(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract void process(Achievement a);
}
