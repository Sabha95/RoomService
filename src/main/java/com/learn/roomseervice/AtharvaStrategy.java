package com.learn.roomseervice;

import com.learn.roomseervice.dto.UserRoomInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class AtharvaStrategy implements Strategy {
    @Autowired
    private RoomService roomService;

    @Override
    public void execute(UserRoomInfo userResponse, List<UserRoomInfo> users, String binStatus, String phoneNumber, Long roomNumber) {
        if( userResponse.getBinCount() == 0){
            //  Optional<UserRoomInfo> secondRoommate = getSecondRoommate(users,userResponse.getRoomNumber());
            roomService.updateBinStatus(binStatus, List.of(phoneNumber),1);
            roomService.updateBinStatusWithoutBinCount("TRUE",List.of(phoneNumber));
        }else if( userResponse.getBinCount() == 1){
            UserRoomInfo userResponseRoommate =  users.stream().filter(u-> u.getRoomNumber().equals(userResponse.getRoomNumber()) && !u.getUsername().equalsIgnoreCase(userResponse.getUsername())).findFirst().get();
            Optional<UserRoomInfo> nextRoomMember = roomService.getNextRoomMember( users, roomNumber);
            roomService.updateBinStatus(binStatus,List.of(userResponse.getPhoneNumber()),0);
            roomService.updateBinStatusWithoutBinCount("TRUE", List.of(nextRoomMember.map(UserRoomInfo::getPhoneNumber).orElseGet(()->"") ));
            // updateBinStatus(binStatus,List.of(userResponse.getPhoneNumber()),0);
        }
    }
}
