package com.learn.roomseervice;

import com.learn.roomseervice.dto.UserRoomInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class DefaultStrategy implements Strategy {


    @Autowired
    private RoomService roomService;

    @Override
    public void execute(UserRoomInfo userResponse, List<UserRoomInfo> users, String binStatus, String phoneNumber, Long roomNumber) {
        if(userResponse.getBinCount() == 0){
            UserRoomInfo secondRoommate = users.stream().filter(u-> u.getRoomNumber().equals(userResponse.getRoomNumber()) && u.getBinCount() == 0 ).skip(1).findFirst().get();
            roomService.updateBinStatus(binStatus, List.of(phoneNumber,secondRoommate.getPhoneNumber()),1);
            roomService.updateBinStatusWithoutBinCount("TRUE",List.of(secondRoommate.getPhoneNumber()));
        }else {
            Optional<UserRoomInfo> nextRoomMember = roomService.getNextRoomMember(users, roomNumber);
            //long roomNumber = nextRoomMember.getRoomNumber();
            if(nextRoomMember.map(UserRoomInfo::getUsername).orElse("").equals("Shreyas")){
                UserRoomInfo userResponseRoommate = users.stream().filter(u -> u.getRoomNumber().
                        equals(nextRoomMember.map(UserRoomInfo::getRoomNumber).orElse(0L)) && !u.getUsername()
                        .equalsIgnoreCase("Shreyas")).findFirst().get();
                roomService.updateBinStatus(binStatus,List.of(userResponse.getPhoneNumber(),userResponseRoommate.getPhoneNumber()),0);
                roomService.updateBinStatusWithoutBinCount("TRUE", List.of(userResponseRoommate.getPhoneNumber()));
            }else{
                Optional<UserRoomInfo>   nextRoomMember2  = roomService.getNextRoomMember(users, roomNumber);
                UserRoomInfo userResponseRoommate =  users.stream().filter(u-> u.getRoomNumber().equals(userResponse.getRoomNumber()) && !u.getUsername().equalsIgnoreCase(userResponse.getUsername())).findFirst().get();
                roomService.updateBinStatus(binStatus,List.of(userResponse.getPhoneNumber(),userResponseRoommate.getPhoneNumber()),0);
                roomService.updateBinStatusWithoutBinCount("TRUE", List.of(nextRoomMember2.map(UserRoomInfo::getPhoneNumber).orElse("")));
            }
        }
    }
}
