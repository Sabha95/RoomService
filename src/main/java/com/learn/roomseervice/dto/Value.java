package com.learn.roomseervice.dto;

import lombok.Data;

import java.util.List;

@Data
public class Value {
    String messaging_product;
    Metadat metadat;
    List<Contact> contacts;
    List<Message> messages;
}
