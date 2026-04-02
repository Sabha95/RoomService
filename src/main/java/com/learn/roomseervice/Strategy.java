package com.learn.roomseervice;

import com.learn.roomseervice.dto.UserRoomInfo;

import java.util.List;

public interface Strategy {
    void execute(UserRoomInfo user,
                 List<UserRoomInfo> users,
                 String binStatus,
                 String phoneNumber,
                 Long roomNumber);
}
