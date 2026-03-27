package com.learn.roomseervice.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Entity
@Data
@Table(name= "user_management")
public class UserManagement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;

    public UserManagement() {

    }
}
