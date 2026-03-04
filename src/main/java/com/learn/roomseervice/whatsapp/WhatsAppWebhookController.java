package com.learn.roomseervice.whatsapp;

import com.learn.roomseervice.RoomService;
import com.learn.roomseervice.dto.UserRoomInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/webhook")
public class WhatsAppWebhookController {

    @Value("${webhook.verify.token}")
    private String verifyToken;


    private RoomService roomService;

    @Autowired
    public WhatsAppWebhookController(RoomService roomService){
        this.roomService = roomService;
    }
    // Verification endpoint
    @GetMapping
    public ResponseEntity<String> verify(
            @RequestParam("hub.mode") String mode,
            @RequestParam("hub.verify_token") String token,
            @RequestParam("hub.challenge") String challenge) {

        if ("subscribe".equals(mode) && verifyToken.equals(token)) {
            return ResponseEntity.ok(challenge);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    // Receive messages
    @PostMapping
    public ResponseEntity<Void> receive(@RequestBody Map<String, Object> payload) {

        System.out.println("Incoming webhook:");
        System.out.println(payload);
        String phoneNumber="";
        String binStatus="";
        Boolean isNext=false;
        List<UserRoomInfo> users = roomService.getUsers();
        UserRoomInfo userResponse= users.stream().filter(u->u.getPhoneNumber().equals(phoneNumber))
                .findFirst().get();
        if(userResponse.getBinCount() == 0){
            UserRoomInfo secondRoommate = users.stream().filter(u-> u.getRoomNumber().equals(userResponse.getRoomNumber()) && u.getBinCount() == 0 ).skip(1).findFirst().get();
            roomService.updateBinStatus(binStatus, List.of(phoneNumber,secondRoommate.getPhoneNumber()),1);
            roomService.updateBinStatusWithoutBinCount("TRUE",List.of(secondRoommate.getPhoneNumber()));
        }else{
            UserRoomInfo nextRoomMember = users.stream().filter(u->u.getPhoneNumber().equalsIgnoreCase(userResponse.getPhoneNumber())).skip(2).findFirst().get();
           // UserRoomInfo nextRoom2ndMember = users.stream().filter(u->u.getRoomNumber().equalsIgnoreCase(nextRoomMember.getRoomNumber())).skip(1).findFirst().get();
            roomService.updateBinStatus(binStatus,List.of(userResponse.getPhoneNumber()),0);
            roomService.updateBinStatusWithoutBinCount("TRUE", List.of(nextRoomMember.getPhoneNumber()));
        }
        //Two scenarios
        //
        //1 . user clicks Yes ->First get the userRoom info from the db since this list will fetch records accorign to the room , i'll update next record to the bin status True.
        // Now since the bin count is 1 and The user has send Yes, then set the bin count to 0 again their turn is done.
        // but if its the last record then it should again start with 1 -
        //
        // then  update the bin status bin count to 1 and chagne the bin
        // status to False. where mobile number and username are same the  for that user.
        //
        //2. They say its not full. -> Update the count for not empty, once done for the day then do not

        return ResponseEntity.ok().build();
    }
}