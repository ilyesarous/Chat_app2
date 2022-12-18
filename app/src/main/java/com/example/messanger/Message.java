package com.example.messanger;

public class Message {
    private String reciever;
    private String sender;
    private String content;

    public Message()
    {}

    public Message(String reciever, String sender, String content) {
        this.reciever = reciever;
        this.sender = sender;
        this.content = content;
    }

    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
