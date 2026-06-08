package com.olicard.timemanager.domain.model;

public enum Importance {
    LOW(1),
    MEDIUM(2),
    HIGH(3);

    private final int weight;

    Importance(int weight) {
        this.weight = weight;
    }

    public int weight() {
        return weight;
    }
}
