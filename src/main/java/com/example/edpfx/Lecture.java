package com.example.edpfx;

/**
 * Lecture
 */
public class Lecture {

    private String room;
    private String module;

    public Lecture(String room, String module) {
        this.room = room;
        this.module = module;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }
}
