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

@Service
public class RoomServiceImpl implements RoomService {

    private RoomDao roomDao;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    public  RoomServiceImpl(RoomDao roomDao) {
        this.roomDao = roomDao;
    }

    @Transactional
    @Override
    public void updateBinStatus(String binStatus, List<String> phoneNumber,Integer binCount) {
        roomDao.updateBinStatus(binStatus.equalsIgnoreCase("yes") ? "FALSE":"TRUE", phoneNumber,binCount);
    }

    @Transactional
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

    public Optional<UserRoomInfo> getSecondRoommate(List<UserRoomInfo> users, Long roomNumber){
        return users.stream().filter(u-> u.getRoomNumber().equals(roomNumber) && u.getBinCount() == 0 ).skip(1).findFirst();
    }

    @Transactional
    public void logicForAtharav( UserRoomInfo userResponse ,List<UserRoomInfo> users,String binStatus,String phoneNumber,Long roomNumber ){
        if( userResponse.getBinCount() == 0){
          //  Optional<UserRoomInfo> secondRoommate = getSecondRoommate(users,userResponse.getRoomNumber());
            updateBinStatus(binStatus, List.of(phoneNumber),1);
            updateBinStatusWithoutBinCount("TRUE",List.of(phoneNumber));
        }else if( userResponse.getBinCount() == 1){
            UserRoomInfo userResponseRoommate =  users.stream().filter(u-> u.getRoomNumber().equals(userResponse.getRoomNumber()) && !u.getUsername().equalsIgnoreCase(userResponse.getUsername())).findFirst().get();
            Optional<UserRoomInfo> nextRoomMember = getNextRoomMember( users, roomNumber);
            updateBinStatus(binStatus,List.of(userResponse.getPhoneNumber()),0);
            updateBinStatusWithoutBinCount("TRUE", List.of(nextRoomMember.map(UserRoomInfo::getPhoneNumber).orElseGet(()->"") ));
           // updateBinStatus(binStatus,List.of(userResponse.getPhoneNumber()),0);
        }
    }

    public  Optional<UserRoomInfo> getNextRoomMember(List<UserRoomInfo> users,Long roomNumber){
       return  users.stream().filter(u-> u.getRoomNumber().equals(roomNumber)).findFirst();
    }

    @Transactional
    @Override
    public void whoTurnsIsNext(String phoneNumber, String binStatus) {
        List<UserRoomInfo> users = getUsers();
        UserRoomInfo userResponse= users.stream().filter(u->u.getPhoneNumber().equals(phoneNumber))
                .findFirst().orElse(new UserRoomInfo());
        String name = userResponse.getUsername();
        UserRoomInfo nextRoomMember = new UserRoomInfo();
        UserRoomInfo userResponseRoommate ;
        Long roomNumber = userResponse.getRoomNumber() == 3 ? 1 : userResponse.getRoomNumber()+1L;
        if(name.equalsIgnoreCase("Atharva")){
            logicForAtharav(userResponse ,users, binStatus, phoneNumber, roomNumber );
        }else{
            logicForRestOfRoommates(userResponse ,users, binStatus, phoneNumber,roomNumber);
        }

//

    }

    @Transactional
     void logicForRestOfRoommates(UserRoomInfo userResponse, List<UserRoomInfo> users, String binStatus, String phoneNumber,Long roomNumber) {
        if(userResponse.getBinCount() == 0){
            UserRoomInfo secondRoommate = users.stream().filter(u-> u.getRoomNumber().equals(userResponse.getRoomNumber()) && u.getBinCount() == 0 ).skip(1).findFirst().get();
            updateBinStatus(binStatus, List.of(phoneNumber,secondRoommate.getPhoneNumber()),1);
            updateBinStatusWithoutBinCount("TRUE",List.of(secondRoommate.getPhoneNumber()));
        }else {
            Optional<UserRoomInfo> nextRoomMember = getNextRoomMember(users, roomNumber);
                //long roomNumber = nextRoomMember.getRoomNumber();
                if(nextRoomMember.map(UserRoomInfo::getUsername).orElse("").equals("Shreyas")){
                    UserRoomInfo userResponseRoommate = users.stream().filter(u -> u.getRoomNumber().
                            equals(nextRoomMember.map(UserRoomInfo::getRoomNumber).orElse(0L)) && !u.getUsername()
                            .equalsIgnoreCase("Shreyas")).findFirst().get();
                    updateBinStatus(binStatus,List.of(userResponse.getPhoneNumber(),userResponseRoommate.getPhoneNumber()),0);
                    updateBinStatusWithoutBinCount("TRUE", List.of(userResponseRoommate.getPhoneNumber()));
                }else{
                    Optional<UserRoomInfo>   nextRoomMember2  = getNextRoomMember(users, roomNumber);
                    UserRoomInfo userResponseRoommate =  users.stream().filter(u-> u.getRoomNumber().equals(userResponse.getRoomNumber()) && !u.getUsername().equalsIgnoreCase(userResponse.getUsername())).findFirst().get();
                    updateBinStatus(binStatus,List.of(userResponse.getPhoneNumber(),userResponseRoommate.getPhoneNumber()),0);
                    updateBinStatusWithoutBinCount("TRUE", List.of(nextRoomMember2.map(UserRoomInfo::getPhoneNumber).orElse("")));
                }
        }
//            else{
//                long newRoomNumber = userResponse.getRoomNumber()+1l;
//                nextRoomMember = users.stream().filter(u-> u.getRoomNumber().equals(newRoomNumber)).findFirst().get();
//            }
    }
}
