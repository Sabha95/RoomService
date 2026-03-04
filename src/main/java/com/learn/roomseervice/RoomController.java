package com.learn.roomseervice;

import com.learn.roomseervice.dto.UserManagement;
import com.learn.roomseervice.dto.UserRoomInfo;
import com.learn.roomseervice.whatsapp.WhatsAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController("/room")
public class RoomController {

    private RoomService roomService;

    private WhatsAppService whatsAppService;

    @Autowired
    RoomController(RoomService roomService, WhatsAppService whatsAppService) {
        this.roomService = roomService;
        this.whatsAppService = whatsAppService;
    }

    public record Room(String yesNo, String notEmpty,String phoneNumber){
    }

//    @RequestMapping  ("/binStatus")
//    public void updateBinStatus(@PathVariable Room room){
//            //PAge will say, did you do the bins ?
//            //Yes and no Options
//            //
//
//            roomService.updateBinStatus(room);
//          //  return ResponseEntity.status(HttpStatus.).body("Room Not Found");
//        }

    @RequestMapping  ("/commonAreaCleanStatus")
    public void commonAreaCleanStatus(@RequestBody Room room){

    }

    @RequestMapping("/test/whatsapp/api")
    public void testWhatsApp(){
        List<UserRoomInfo> users = roomService.getUsers();
        UserRoomInfo userRoomInfo = users.stream().filter(u->u.getBinStatus().equalsIgnoreCase("TRUE")).findFirst().orElse(new UserRoomInfo());
        //whatsAppService.sendUtilityTemplate("918828173732","Bhakti");
        whatsAppService.sendUtilityTemplate(userRoomInfo.getPhoneNumber(),userRoomInfo.getUsername());
    }
}







