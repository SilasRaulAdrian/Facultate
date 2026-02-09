package com.org.example.model;

import java.time.LocalDateTime;

import static com.org.example.utils.Constants.FORMATTER;

public class Message {
    private String id;
    private String message;
    private String body;
    private String from;
    private String to;
    private LocalDateTime date;

    public Message(String id, String message, String body, String from, String to, LocalDateTime date) {
        this.id = id;
        this.message = message;
        this.body = body;
        this.from = from;
        this.to = to;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", message='" + message + '\'' +
                ", body='" + body + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", date=" + date.format(FORMATTER) +
                '}';
    }
}
