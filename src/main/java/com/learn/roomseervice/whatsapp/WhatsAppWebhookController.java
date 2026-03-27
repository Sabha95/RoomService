package com.learn.roomseervice.whatsapp;

import com.learn.roomseervice.RoomService;
import com.learn.roomseervice.dto.Profile;
import com.learn.roomseervice.dto.UserRoomInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.ObjectMapper;
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
    @PostMapping(consumes = "application/json")
    public ResponseEntity receive(@RequestBody Map<String, Object> payload) {

         record Change(com.learn.roomseervice.dto.Value value, String field){ }
         record Entry(String id,List<Change> changes){ }
         record Root(String object,List<Entry> entry){ }

        System.out.println("Incoming webhook:");
        System.out.println(payload);
        ObjectMapper mapper = new ObjectMapper();

        Root obj = mapper.convertValue(payload, Root.class);

        String phoneNumber= obj.entry.get(0).changes().get(0).value().getContacts().get(0).wa_id;
       // String name = obj.entry.get(0).changes().get(0).value().getContacts().get(0).profile.getName();
        String binStatus= obj.entry.get(0).changes().get(0).value().getMessages().get(0).getButton().text;
        Boolean isNext=false;
        if(!binStatus.equalsIgnoreCase("yes")){
           return ResponseEntity.status(HttpStatus.OK).body("Resource created");
        }
        roomService.whoTurnsIsNext(phoneNumber,binStatus);
        List<UserRoomInfo> users = roomService.getUsers();

        UserRoomInfo userResponse= users.stream().filter(u->u.getPhoneNumber().equals(phoneNumber))
                .findFirst().get();
        String name = userResponse.getUsername();
        if(name.equalsIgnoreCase("Atharva") && userResponse.getBinCount() == 0){
            UserRoomInfo secondRoommate = users.stream().filter(u-> u.getRoomNumber().equals(userResponse.getRoomNumber()) && u.getBinCount() == 0 ).skip(1).findFirst().get();
            roomService.updateBinStatus(binStatus, List.of(phoneNumber),1);
            roomService.updateBinStatusWithoutBinCount("TRUE",List.of(phoneNumber));
        }else if(name.equalsIgnoreCase("Atharva") && userResponse.getBinCount() == 1){
            //UserRoomInfo userResponseRoommate =  users.stream().filter(u-> u.getRoomNumber().equals(userResponse.getRoomNumber()) && !u.getUsername().equalsIgnoreCase(userResponse.getUsername())).findFirst().get();
            long newRoomNumber = userResponse.getRoomNumber()+1l;
            UserRoomInfo nextRoomMember = users.stream().filter(u-> u.getRoomNumber().equals(newRoomNumber)).findFirst().get();
            roomService.updateBinStatus(binStatus,List.of(userResponse.getPhoneNumber()),0);
            roomService.updateBinStatusWithoutBinCount("TRUE", List.of(nextRoomMember.getPhoneNumber()));
        }
        UserRoomInfo nextRoomMember = new UserRoomInfo();
        UserRoomInfo userResponseRoommate ;
        //Scenario 1 its the first turn out of 2 of a particular room.
        if(!name.equalsIgnoreCase("Atharva") && userResponse.getBinCount() == 0){
            UserRoomInfo secondRoommate = users.stream().filter(u-> u.getRoomNumber().equals(userResponse.getRoomNumber()) && u.getBinCount() == 0 ).skip(1).findFirst().get();
            roomService.updateBinStatus(binStatus, List.of(phoneNumber,secondRoommate.getPhoneNumber()),1);
            roomService.updateBinStatusWithoutBinCount("TRUE",List.of(secondRoommate.getPhoneNumber()));
        }else if(!name.equalsIgnoreCase("Atharva")){
            if(userResponse.getRoomNumber() == 3 ){
                long newRoomNumber = 1;
                 nextRoomMember = users.stream().filter(u-> u.getRoomNumber().equals(newRoomNumber)).findFirst().get();
                long roomNumber = nextRoomMember.getRoomNumber();
                 if(nextRoomMember.getUsername().equals("Shreyas")){
                      userResponseRoommate =  users.stream().filter(u-> u.getRoomNumber().equals(roomNumber) && !u.getUsername().equalsIgnoreCase("Shreyas")).findFirst().get();
                     nextRoomMember = userResponseRoommate;
                     roomService.updateBinStatus(binStatus,List.of(userResponse.getPhoneNumber(),userResponseRoommate.getPhoneNumber()),0);
                     roomService.updateBinStatusWithoutBinCount("TRUE", List.of(nextRoomMember.getPhoneNumber()));
                     return ResponseEntity.ok().build();
                 }
            }else{
                long newRoomNumber = userResponse.getRoomNumber()+1l;
                 nextRoomMember = users.stream().filter(u-> u.getRoomNumber().equals(newRoomNumber)).findFirst().get();
            }

             userResponseRoommate =  users.stream().filter(u-> u.getRoomNumber().equals(userResponse.getRoomNumber()) && !u.getUsername().equalsIgnoreCase(userResponse.getUsername())).findFirst().get();
            roomService.updateBinStatus(binStatus,List.of(userResponse.getPhoneNumber(),userResponseRoommate.getPhoneNumber()),0);
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