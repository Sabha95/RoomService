package com.learn.roomseervice;

import com.learn.roomseervice.dto.UserManagement;
import com.learn.roomseervice.dto.UserRoomInfo;

import java.util.List;
import java.util.Optional;

public interface RoomService {
    void updateBinStatus(String binStatus, List<String> phoneNumber,Integer binCount);

    void updateBinStatusWithoutBinCount(String binStatus, List<String> phoneNumber);

    List<UserRoomInfo> getUsers();

    void whoTurnsIsNext(String phoneNumber, String binStatus);

    public Optional<UserRoomInfo> getNextRoomMember(List<UserRoomInfo> users, Long roomNumber);
}
