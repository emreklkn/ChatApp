package com.example.chatprograming.Models;

public class MessageModel {

    String Uıd, message, messageId;
    Long timestamp;


    public MessageModel(String uıd, String message, Long timestamp) {
        Uıd = uıd;
        this.message = message;
        this.timestamp = timestamp;
    }

    public MessageModel(String uıd, String message) {
        Uıd = uıd;
        this.message = message;
    }

    public MessageModel(){

    }


    public String getUıd() {
        return Uıd;
    }

    public void setUıd(String uıd) {
        Uıd = uıd;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
