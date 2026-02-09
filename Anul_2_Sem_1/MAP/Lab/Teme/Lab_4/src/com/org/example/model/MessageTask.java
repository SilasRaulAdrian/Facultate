package com.org.example.model;

public class MessageTask extends Task {
    private Message message;

    public MessageTask(String taskID, String descriere, Message message) {
        super(taskID, descriere);
        this.message = message;
    }

    @Override
    public void execute() {
        System.out.println(this);
    }

    @Override
    public String toString() {
        return super.toString() + " " + this.message;
    }
}
