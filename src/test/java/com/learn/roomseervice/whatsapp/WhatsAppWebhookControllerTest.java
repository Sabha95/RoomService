package com.learn.roomseervice.whatsapp;

import com.learn.roomseervice.RoomDao;
import com.learn.roomseervice.RoomService;
import com.learn.roomseervice.RoomServiceImpl;
import com.learn.roomseervice.dto.UserManagement;
import com.learn.roomseervice.dto.UserRoomInfo;
import jakarta.transaction.Transactional;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


//@RunWith(SpringJUnit4ClassRunner.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class WhatsAppWebhookControllerTest {

    static List<Map<String, Object>> testData =  new ArrayList<>();
    static List <UserRoomInfo> userRoomDetailsList =  new ArrayList<>();;
    static List<UserRoomInfo> requestData =  new ArrayList<>();

    @Autowired
    RoomDao roomDao;

    @InjectMocks
    RoomServiceImpl roomService;

    @Autowired
    MockMvc mockMvc;

    @BeforeAll
    public static void setup(){
        //roomDao.deleteAll();
        testData = loadTestData();
    }

    private static List<Map<String, Object>> loadTestData() {
        List<UserManagement> users = new ArrayList<>();

        userRoomDetailsList.add(new UserRoomInfo(1L, "Bhakti", "918828173732", 2L, 0, "False", "False"));
        userRoomDetailsList.add(new UserRoomInfo(6L, "Varsha", "919880623282", 3L, 1, "False", "TRUE"));
        userRoomDetailsList.add(new UserRoomInfo(5L, "Krupa", "919742561999", 3L, 0, "False", "False"));
        userRoomDetailsList.add(new UserRoomInfo(2L, "Asmita", "918898346696", 2L, 0, "FALSE", "False"));
        userRoomDetailsList.add(new UserRoomInfo(3L, "Shreyas", "918655537642", 1L, 0, "FALSE", "False"));
        userRoomDetailsList.add(new UserRoomInfo(4L, "Atharva", "919503443228", 1L, 0, "FALSE", "False"));

        requestData.add(new UserRoomInfo(1L, "Bhakti", "918828173732", 2L, 0, "False", "YES"));
        requestData.add(new UserRoomInfo(6L, "Varsha", "919880623282", 3L, 1, "False", "YES"));
        requestData.add(new UserRoomInfo(5L, "Krupa", "919742561999", 3L, 0, "False", "YES"));
        requestData.add(new UserRoomInfo(2L, "Asmita", "918898346696", 2L, 0, "FALSE", "YES"));
        requestData.add(new UserRoomInfo(3L, "Shreyas", "918655537642", 1L, 0, "FALSE", "YES"));
        requestData.add(new UserRoomInfo(4L, "Atharva", "919503443228", 1L, 0, "FALSE", "YES"));

        List<Map<String, Object>> roomInfoList = new ArrayList<>();
        for(UserRoomInfo userRoomInfo : userRoomDetailsList){


        }

        return roomInfoList;
    }

    Map<String,Object> getRequest( UserRoomInfo userRoomInfo){
        Map map = new HashMap<>();
        map.put("object", "whatsapp_business_account");

        Map<String, Object> profile = new HashMap<>();
        profile.put("name",userRoomInfo.getUsername());

        Map<String, Object> contact = new HashMap<>();
        contact.put("profile", profile);
        contact.put("wa_id", userRoomInfo.getPhoneNumber());

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("display_phone_number", "XXXX");
        metadata.put("phone_number_id", "XXXXXX");

        Map<String, Object> context = new HashMap<>();
        context.put("from", "XXXXXX");
        context.put("id", "wamid.HBgMOTE4ODI4MTczNzMyFQIAERgSQzRDRDcyNjcyNEVEQjhCOEI0AA==");

        Map<String, Object> button = new HashMap<>();
        button.put("payload", "Yes");
        button.put("text", "Yes");

        Map<String, Object> message = new HashMap<>();
        message.put("context", context);
        message.put("from", "918828173732");
        message.put("id", "wamid.XXXXXXXXX");
        message.put("timestamp", "1773177598");
        message.put("type", "button");
        message.put("button", button);

        List<Object> messages = new ArrayList<>();
        messages.add(message);

        List<Object> contacts = new ArrayList<>();
        contacts.add(contact);

        Map<String, Object> value = new HashMap<>();
        value.put("messaging_product", "whatsapp");
        value.put("metadata", metadata);
        value.put("contacts", contacts);
        value.put("messages", messages);

        Map<String, Object> change = new HashMap<>();
        change.put("value", value);
        change.put("field", "messages");

        List<Object> changes = new ArrayList<>();
        changes.add(change);

        Map<String, Object> entryObj = new HashMap<>();
        entryObj.put("id", "XXXXX");
        entryObj.put("changes", changes);

        List<Object> entry = new ArrayList<>();
        entry.add(entryObj);

        map.put("entry", entry);

       // roomInfoList.add(map);
        return map;
    }



//    @BeforeEach
//    void setup() {
//        roomService = new RoomServiceImpl(roomRepository);
//    }


    @Transactional
    @Test
    public void testReceiveWebhookAPI() throws Exception {

        UserRoomInfo userRoomInfo = userRoomDetailsList.stream().filter(user -> user.getBinStatus().equalsIgnoreCase("TRUE")).findFirst().orElse(new UserRoomInfo());

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(getRequest(userRoomInfo));
        // Turn Varsha - she said yes (assuimg the bin stauts 1), now update the bin status for her to False and bin count to 0;
        int i =1;
        for(UserRoomInfo userRoomInfo2 : requestData){
            int roomNo = i;
            roomDao.updateBinStatus("yes",userRoomDetailsList.stream().filter(u->u.getRoomNumber() == roomNo).map(UserRoomInfo::getPhoneNumber).collect(Collectors.toList()),);
            i++;
        }



        mockMvc.perform(post("/webhook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        Long newRoomNumber = userRoomInfo.getRoomNumber()== 3 ? 1L: userRoomInfo.getRoomNumber();
        //UserRoomInfo expectedUserInfo = userRoomDetailsList.stream().filter(user-> Objects.equals(user.getRoomNumber(), newRoomNumber)).skip(1).findFirst().orElse(new UserRoomInfo());
      //  UserRoomInfo expectedUserInfo = userRoomDetailsList.stream().filter(u->u.getBinStatus().equalsIgnoreCase("TRUE")).findFirst().get();
        for(UserRoomInfo userRoomInfo2 : requestData){
            when(roomService.whoTurnsIsNext(userRoomInfo2.getPhoneNumber(),userRoomInfo2.getBinStatus())).thenReturn();
        }


        UserRoomInfo actualUserInfo = roomDao.getUsers();
        System.out.print("actual user : " + actualUserInfo);
        System.out.print("expected user : " + expectedUserInfo);
        //Assert.assertTrue(new ReflectionEquals(expectedUserInfo).matches(actualUserInfo));

        assertThat(actualUserInfo)
                .usingRecursiveComparison().comparingOnlyFields("username")
                .isEqualTo(expectedUserInfo);

        userRoomDetailsList.stream().filter(u->u.getBinStatus().equalsIgnoreCase("TRUE")).forEach(u->{
            u.setBinStatus("FALSE");
        });
        userRoomDetailsList.stream().filter(u->actualUserInfo.getUsername().equalsIgnoreCase(u.getUsername())).forEach(u->{
            u.setBinStatus("TRUE");
        });

        System.out.print(" new userRoomDetailsList : " + userRoomDetailsList);
    }


    @RepeatedTest(1)
    public void testReceiveWebhookAPI2() throws Exception {

        UserRoomInfo userRoomInfo = userRoomDetailsList.stream().filter(user -> user.getBinStatus().equalsIgnoreCase("TRUE")).findFirst().orElse(new UserRoomInfo());

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(getRequest(userRoomInfo));

        mockMvc.perform(post("/webhook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        Long newRoomNumber = userRoomInfo.getRoomNumber()== 3 ? 1L: userRoomInfo.getRoomNumber();

        UserRoomInfo expectedUserInfo = userRoomDetailsList.stream().filter(user-> Objects.equals(user.getRoomNumber(), newRoomNumber)).skip(1).findFirst().orElse(new UserRoomInfo());
        UserRoomInfo actualUserInfo = roomDao.getUsers().stream().filter(u->u.getBinStatus().equalsIgnoreCase("TRUE")).findFirst().get();
        System.out.print("actual user : " + actualUserInfo);
        System.out.print("expected user : " + expectedUserInfo);
        //Assert.assertTrue(new ReflectionEquals(expectedUserInfo).matches(actualUserInfo));


        assertThat(actualUserInfo)
                .usingRecursiveComparison().comparingOnlyFields("username")
                .isEqualTo(expectedUserInfo);

        userRoomDetailsList.stream().filter(u->u.getBinStatus().equalsIgnoreCase("TRUE")).forEach(u->{
            u.setBinStatus("FALSE");
        });
        userRoomDetailsList.stream().filter(u->actualUserInfo.getUsername().equalsIgnoreCase(u.getUsername())).forEach(u->{
            u.setBinStatus("TRUE");
        });

        System.out.print(" new userRoomDetailsList : " + userRoomDetailsList);


    }


}