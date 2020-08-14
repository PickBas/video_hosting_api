package com.therearenotasksforus.videohostingapi;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@SpringBootTest(properties = { "spring.jpa.hibernate.ddl-auto=create-drop" })
@RunWith(SpringRunner.class)
class ChannelTests extends AbstractTest {

    @Test
    public void channelEmptyListLoads() throws Exception {
        super.register();

        String token = super.getToken();
        String uri = "/api/channels";

        MvcResult mvcResult = super.getRequest(uri, token);
        ArrayList<Map<String, Object>> responseBodyArray = super.mapFromJsonList(mvcResult);

        int status = mvcResult.getResponse().getStatus();

        assertEquals(200, status);
        assertEquals(0, responseBodyArray.size());
    }

    @Test
    public void channelCreate() throws Exception {
        super.register();

        String token = super.getToken();
        String uri = "/api/channel/create";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", "FirstTestChannel");
        requestBody.put("info", "FirstTestChannel information");

        String requestBodyJson = super.mapToJson(requestBody);
        MvcResult mvcResult = super.postRequest(uri, token, requestBodyJson);

        int status = mvcResult.getResponse().getStatus();
        Map<String, Object> responseBody = super.mapFromJson(mvcResult);

        int channelId = (int)responseBody.get("id");

        assertEquals(201, status);
        assertEquals("FirstTestChannel", responseBody.get("name"));
        assertEquals("FirstTestChannel information", responseBody.get("info"));

        uri = "/api/channels";

        mvcResult = super.getRequest(uri, token);
        ArrayList<Map<String, Object>> responseBodyArray = super.mapFromJsonList(mvcResult);

        status = mvcResult.getResponse().getStatus();

        assertEquals(200, status);
        assertNotEquals(0, responseBodyArray.size());
        assertEquals("FirstTestChannel", responseBodyArray.get(channelId - 1).get("name"));
        assertEquals("FirstTestChannel information", responseBodyArray.get(channelId - 1).get("info"));

        uri = "/api/channel/" + channelId;

        mvcResult = super.getRequest(uri, token);
        responseBody = super.mapFromJson(mvcResult);

        status = mvcResult.getResponse().getStatus();

        assertEquals(200, status);
        assertNotEquals(0, responseBodyArray.size());
        assertEquals("FirstTestChannel", responseBody.get("name"));
        assertEquals("FirstTestChannel information", responseBody.get("info"));
    }

    @Test
    public void channelListLoads() throws Exception {
        super.register();

        String token = super.getToken();
        String uri = "/api/channel/create";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", "SecondTestChannel");
        requestBody.put("info", "SecondTestChannel information");

        String requestBodyJson = super.mapToJson(requestBody);
        super.postRequest(uri, token, requestBodyJson);

        uri = "/api/channels";

        MvcResult mvcResult = super.getRequest(uri, token);
        ArrayList<Map<String, Object>> responseBodyArray = super.mapFromJsonList(mvcResult);

        int status = mvcResult.getResponse().getStatus();

        assertEquals(200, status);
        assertNotEquals(0, responseBodyArray.size());
    }

    @Test
    public void channelUpdate() throws Exception {
        super.register();

        String token = super.getToken();
        MvcResult mvcResult = super.crateChannel(token);

        int channelId = (int)super.mapFromJson(mvcResult).get("id");

        String uri = "/api/channel/" + channelId + "/update";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", "Updated name");
        requestBody.put("info", "Updated info");

        String requestBodyJson = super.mapToJson(requestBody);
        mvcResult = super.postRequest(uri, token, requestBodyJson);

        Map<String, Object> responseBody = super.mapFromJson(mvcResult);

        int status = mvcResult.getResponse().getStatus();

        assertEquals(200, status);
        assertEquals("Updated name", responseBody.get("name"));
        assertEquals("Updated info", responseBody.get("info"));
    }
}
