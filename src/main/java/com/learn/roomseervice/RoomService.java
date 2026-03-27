package com.learn.roomseervice;

import com.learn.roomseervice.dto.UserManagement;
import com.learn.roomseervice.dto.UserRoomInfo;

import java.util.List;

public interface RoomService {
    void updateBinStatus(String binStatus, List<String> phoneNumber,Integer binCount);

    void updateBinStatusWithoutBinCount(String binStatus, List<String> phoneNumber);

    List<UserRoomInfo> getUsers();

    void whoTurnsIsNext(String phoneNumber, String binStatus);
}
