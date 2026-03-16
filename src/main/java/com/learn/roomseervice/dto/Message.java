package com.learn.roomseervice.dto;

import lombok.Data;

@Data
public class Message{
    public Context context;
    public String from;
    public String id;
    public String timestamp;
    public String type;
    public Button button;
}