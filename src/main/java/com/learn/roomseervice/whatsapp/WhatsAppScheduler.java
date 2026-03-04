package com.learn.roomseervice.whatsapp;

import com.learn.roomseervice.RoomService;
import com.learn.roomseervice.dto.UserRoomInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WhatsAppScheduler {

    private final WhatsAppService whatsAppService;
    private RoomService roomService;

    @Autowired
    public WhatsAppScheduler(WhatsAppService whatsAppService,RoomService roomService) {
        this.whatsAppService = whatsAppService;
        this.roomService = roomService;
    }

    @Scheduled(cron = "0 0 9 */2 * *") // every 2 days at 9:00 AM
    public void sendBinReminder() {

        // Fetch users from DB if needed
        List<UserRoomInfo> users = roomService.getUsers();
        UserRoomInfo userRoomInfo = users.stream().filter(u->u.getBinStatus().equalsIgnoreCase("TRUE")).findFirst().orElse(new UserRoomInfo());
        whatsAppService.sendUtilityTemplate(userRoomInfo.getPhoneNumber(), userRoomInfo.getUsername());
    }
}