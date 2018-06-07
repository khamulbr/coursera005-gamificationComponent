package com.gamification.model;

public class Points extends Achievement{

    private int value = 0;

    public Points(String name, int value) {
        super(name);
        this.value += value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public void process(Achievement a) {
        this.value += ((Points) a).getValue();
    }
}
