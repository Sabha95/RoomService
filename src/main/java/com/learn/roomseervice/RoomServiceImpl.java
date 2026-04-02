package com.learn.roomseervice;


import com.learn.roomseervice.dto.UserRoomInfo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class RoomServiceImpl implements RoomService {

    private RoomDao roomDao;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    public  RoomServiceImpl(RoomDao roomDao) {
        this.roomDao = roomDao;
    }


    @Override
    public void updateBinStatus(String binStatus, List<String> phoneNumber,Integer binCount) {
        roomDao.updateBinStatus(binStatus.equalsIgnoreCase("yes") ? "FALSE":"TRUE", phoneNumber,binCount);
    }


    @Override
    public void updateBinStatusWithoutBinCount(String binStatus, List<String> phoneNumber) {
        roomDao.updateBinStatusWithoutBinCount(binStatus, phoneNumber);
    }

    @Override
    public List<UserRoomInfo> getUsers() {
        return roomDao.getUsers();
    }


    @Override
    public  Optional<UserRoomInfo> getNextRoomMember(List<UserRoomInfo> users,Long roomNumber){
       return  users.stream().filter(u-> u.getRoomNumber().equals(roomNumber)).findFirst();
    }

    @Transactional
    @Override
    public void whoTurnsIsNext(String phoneNumber, String binStatus) {
        List<UserRoomInfo> users = getUsers();
        UserRoomInfo userResponse= users.stream().filter(u->u.getPhoneNumber().equals(phoneNumber))
                .findFirst().orElse(new UserRoomInfo());
        Long roomNumber = userResponse.getRoomNumber() == 3 ? 1 : userResponse.getRoomNumber()+1L;
        Strategy strategy = StrategyFactory.getStrategy(userResponse);
        strategy.execute(userResponse,users,binStatus,phoneNumber,roomNumber);
    }


}
