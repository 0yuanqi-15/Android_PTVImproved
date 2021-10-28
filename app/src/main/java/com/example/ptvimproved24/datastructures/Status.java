package com.example.ptvimproved24.datastructures;

public class Status {
    private String version;
    private int health;

    public Status(String version, int health) {
        this.version = version;
        this.health = health;
    }

    public String getVersion() {
        return version;
    }

    public int getHealth() {
        return health;
    }
}
