package com.learn.roomseervice.whatsapp;

import com.learn.roomseervice.RoomDao;
import com.learn.roomseervice.RoomService;
import com.learn.roomseervice.RoomServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


//@RunWith(SpringJUnit4ClassRunner.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class WhatsAppWebhookControllerTest {


    @Autowired
    RoomDao roomDao;

    @InjectMocks
    RoomServiceImpl roomService;

    @Autowired
    MockMvc mockMvc;

//    @BeforeEach
//    void setup() {
//        roomService = new RoomServiceImpl(roomRepository);
//    }


    @Transactional
    @Test
    public void testReceiveWebhookAPI() throws Exception {
        Map<String, Object> map = new HashMap<>();

        map.put("object", "whatsapp_business_account");

        Map<String, Object> profile = new HashMap<>();
        profile.put("name", "Bhakti");

        Map<String, Object> contact = new HashMap<>();
        contact.put("profile", profile);
        contact.put("wa_id", "918828173732");

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

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(map);

        mockMvc.perform(post("/webhook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

    }
}