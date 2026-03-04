package com.learn.roomseervice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserRoomInfo {
    private Long userId;
    private String username;
    private String phoneNumber;
    private Long roomNumber;
    private Integer binCount;
    private String commonAreaCleanStatus;
    private String binStatus;
}