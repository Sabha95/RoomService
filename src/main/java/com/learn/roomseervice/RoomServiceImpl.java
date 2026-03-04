package com.learn.roomseervice;


import com.learn.roomseervice.dto.UserRoomInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    private RoomDao roomDao;

    @Autowired
    public  RoomServiceImpl(RoomDao roomDao) {
        this.roomDao = roomDao;
    }

    @Override
    public void updateBinStatus(String binStatus, List<String> phoneNumber,Integer binCount) {
        roomDao.updateBinStatus(binStatus.equalsIgnoreCase("yes") ? "FALSE":"TRUE", phoneNumber,binCount);
        findNextforBinDuties("");
    }

    @Override
    public void updateBinStatusWithoutBinCount(String binStatus, List<String> phoneNumber) {
        roomDao.updateBinStatusWithoutBinCount(binStatus, phoneNumber);
        findNextforBinDuties("");
    }
    void findNextforBinDuties(String roomMateName){

    }



   public List<UserRoomInfo> whoTurnsIsNext(){
       List<UserRoomInfo> users = roomDao.getUsers();
       List<UserRoomInfo> newUsers = new ArrayList<>();
       return newUsers;
    }

    @Override
    public List<UserRoomInfo> getUsers() {
        return roomDao.getUsers();
    }
}
