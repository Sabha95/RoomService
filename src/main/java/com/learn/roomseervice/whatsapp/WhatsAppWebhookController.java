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
        return ResponseEntity.ok().build();

    }
}