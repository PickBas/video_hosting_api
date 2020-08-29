package com.therearenotasksforus.videohostingapi;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@SpringBootTest(properties = { "spring.jpa.hibernate.ddl-auto=create-drop" })
@RunWith(SpringRunner.class)
class ChannelTests extends AbstractTest {

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
        MvcResult mvcResult = super.createChannel(token);

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

    @Test
    public void channelSubscriptionByOwner() throws Exception {
        super.register();

        String token = super.getToken();
        MvcResult mvcResult = super.createChannel(token);

        int channelId = (int)super.mapFromJson(mvcResult).get("id");
        String uri = "/api/channel/" + channelId + "/subscribe";

        mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .headers(this.getHttpHeaders(token))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertEquals(400, mvcResult.getResponse().getStatus());
    }

    @Test
    public void channelSubscriptionByRandomUser() throws Exception {
        super.register();

        String token = super.getToken();
        MvcResult mvcResult = super.createChannel(token);

        int channelId = (int)super.mapFromJson(mvcResult).get("id");

        String uri = "/api/channel/" + channelId + "/subscribe";
        int secondUserId = super.registerWithEmailAndUsername(
                "random@com.com",
                "SubTestUser"
        );

        String customUserToken = super.getTokenWithUsername("SubTestUser");

        mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .headers(this.getHttpHeaders(customUserToken))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());

        uri = "/api/channel/" + channelId;
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .headers(this.getHttpHeaders(customUserToken)))
                .andReturn();

        Map<String, Object> responseBody = super.mapFromJson(mvcResult);
        ArrayList<Object> subList = super.getLongArrayByKey(responseBody, "subscribers");

        assertNotEquals(0, subList.size());
        assertTrue(subList.contains(secondUserId));
    }

    @Test
    public void channelUnsubscribeByOwner() throws Exception {
        super.register();

        String token = super.getToken();
        MvcResult mvcResult = super.createChannel(token);

        int channelId = (int)super.mapFromJson(mvcResult).get("id");
        String uri = "/api/channel/" + channelId + "/unsubscribe";

        mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .headers(this.getHttpHeaders(token))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertEquals(400, mvcResult.getResponse().getStatus());
    }

    @Test
    public void channelUnsubscribeByUnsubscribedUser() throws Exception {
        super.register();

        String token = super.getToken();
        MvcResult mvcResult = super.createChannel(token);

        int channelId = (int)super.mapFromJson(mvcResult).get("id");
        String uri = "/api/channel/" + channelId + "/unsubscribe";

        super.registerWithEmailAndUsername("random@mail.com", "unSubTestUser");
        String customUserToken = super.getTokenWithUsername("unSubTestUser");

        mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .headers(this.getHttpHeaders(customUserToken))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertEquals(400, mvcResult.getResponse().getStatus());
    }

    @Test
    public void channelUnsubscribeBySubscribedUser() throws  Exception {
        super.register();

        String token = super.getToken();
        MvcResult mvcResult = super.createChannel(token);

        int channelId = (int)super.mapFromJson(mvcResult).get("id");
        String uri = "/api/channel/" + channelId + "/subscribe";

        super.registerWithEmailAndUsername("random@gmail.com", "unSubTestUserSubscribed");
        String customUserToken = super.getTokenWithUsername("unSubTestUserSubscribed");

        mvc.perform(MockMvcRequestBuilders.post(uri)
                .headers(this.getHttpHeaders(customUserToken))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        uri = "/api/channel/" + channelId + "/unsubscribe";

        mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .headers(this.getHttpHeaders(customUserToken))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());
    }

    @Test
    public void channelDelete() throws Exception {
        super.register();
        String token = super.getToken();
        int channelId = (int) super.mapFromJson(super.createChannel(token)).get("id");
        int ownedChannelBefore = super
                .mapFromJsonList(super.getRequest("/api/channels/owned", token))
                .size();

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .delete("/api/channel/" + channelId)
                .headers(super.getHttpHeaders(token)))
                .andReturn();

        int ownedChannelAfter = super
                .mapFromJsonList(super.getRequest("/api/channels/owned", token))
                .size();

        assertEquals(200, mvcResult.getResponse().getStatus());
        assertNotEquals(ownedChannelBefore, ownedChannelAfter);
    }
}
