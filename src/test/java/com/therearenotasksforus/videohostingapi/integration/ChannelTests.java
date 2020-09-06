package com.therearenotasksforus.videohostingapi.integration;

import com.therearenotasksforus.videohostingapi.VideoHostingApiApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = VideoHostingApiApplication.class,
        properties = {"spring.jpa.hibernate.ddl-auto=create-drop"})
@WebAppConfiguration
@AutoConfigureMockMvc
class ChannelTests {

    @Autowired
    public MockMvc mvc;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    public String userToken;

    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @BeforeEach
    public void preTest() throws Exception {
        TestMethods.register(mvc);
        userToken = TestMethods.getToken(mvc);
    }

    @Test
    public void channelCreate() throws Exception {
        String uri = "/api/channel/create";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", "FirstTestChannel");
        requestBody.put("info", "FirstTestChannel information");

        String requestBodyJson = TestMethods.mapToJson(requestBody);
        MvcResult mvcResult = TestMethods.postRequest(mvc, uri, userToken, requestBodyJson);
        Map<String, Object> responseBody = TestMethods.mapFromJson(mvcResult);
        int channelId = (int) responseBody.get("id");

        assertEquals(201, mvcResult.getResponse().getStatus());
        assertEquals("FirstTestChannel", responseBody.get("name"));
        assertEquals("FirstTestChannel information", responseBody.get("info"));

        uri = "/api/channels";

        mvcResult = TestMethods.getRequest(mvc, uri, userToken);
        ArrayList<Map<String, Object>> responseBodyArray = TestMethods.mapFromJsonList(mvcResult);

        assertEquals(200, mvcResult.getResponse().getStatus());
        assertNotEquals(0, responseBodyArray.size());
        assertEquals("FirstTestChannel", responseBodyArray.get(channelId - 1).get("name"));
        assertEquals("FirstTestChannel information", responseBodyArray.get(channelId - 1).get("info"));

        uri = "/api/channel/" + channelId;
        mvcResult = TestMethods.getRequest(mvc, uri, userToken);
        responseBody = TestMethods.mapFromJson(mvcResult);

        assertEquals(200, mvcResult.getResponse().getStatus());
        assertNotEquals(0, responseBodyArray.size());
        assertEquals("FirstTestChannel", responseBody.get("name"));
        assertEquals("FirstTestChannel information", responseBody.get("info"));
    }

    @Test
    public void channelListLoads() throws Exception {
        String uri = "/api/channel/create";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", "SecondTestChannel");
        requestBody.put("info", "SecondTestChannel information");

        String requestBodyJson = TestMethods.mapToJson(requestBody);
        TestMethods.postRequest(mvc, uri, userToken, requestBodyJson);

        uri = "/api/channels";
        MvcResult mvcResult = TestMethods.getRequest(mvc, uri, userToken);
        ArrayList<Map<String, Object>> responseBodyArray = TestMethods.mapFromJsonList(mvcResult);

        assertEquals(200, mvcResult.getResponse().getStatus());
        assertNotEquals(0, responseBodyArray.size());
    }

    @Test
    public void channelUpdate() throws Exception {
        MvcResult mvcResult = TestMethods.createChannel(mvc, userToken);

        int channelId = (int) TestMethods.mapFromJson(mvcResult).get("id");
        String uri = "/api/channel/" + channelId + "/update";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", "Updated name");
        requestBody.put("info", "Updated info");

        String requestBodyJson = TestMethods.mapToJson(requestBody);
        mvcResult = TestMethods.postRequest(mvc, uri, userToken, requestBodyJson);
        Map<String, Object> responseBody = TestMethods.mapFromJson(mvcResult);

        assertEquals(200, mvcResult.getResponse().getStatus());
        assertEquals("Updated name", responseBody.get("name"));
        assertEquals("Updated info", responseBody.get("info"));
    }

    @Test
    public void channelSubscriptionByOwner() throws Exception {
        MvcResult mvcResult = TestMethods.createChannel(mvc, userToken);

        int channelId = (int) TestMethods.mapFromJson(mvcResult).get("id");
        String uri = "/api/channel/" + channelId + "/subscribe";

        mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .headers(TestMethods.getHttpHeaders(userToken))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertEquals(400, mvcResult.getResponse().getStatus());
    }

    @Test
    public void channelSubscriptionByRandomUser() throws Exception {
        MvcResult mvcResult = TestMethods.createChannel(mvc, userToken);

        int channelId = (int) TestMethods.mapFromJson(mvcResult).get("id");

        String uri = "/api/channel/" + channelId + "/subscribe";
        int secondUserId = TestMethods.registerWithEmailAndUsername(
                mvc,
                "random@com.com",
                "SubTestUser"
        );

        String customUserToken = TestMethods.getTokenWithUsername(mvc, "SubTestUser");
        mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .headers(TestMethods.getHttpHeaders(customUserToken))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());

        uri = "/api/channel/" + channelId;
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .headers(TestMethods.getHttpHeaders(customUserToken)))
                .andReturn();

        Map<String, Object> responseBody = TestMethods.mapFromJson(mvcResult);
        ArrayList<Object> subList = TestMethods.getLongArrayByKey(responseBody, "subscribers");

        assertNotEquals(0, subList.size());
        assertTrue(subList.contains(secondUserId));
    }

    @Test
    public void channelUnsubscribeByOwner() throws Exception {
        MvcResult mvcResult = TestMethods.createChannel(mvc, userToken);

        int channelId = (int) TestMethods.mapFromJson(mvcResult).get("id");
        String uri = "/api/channel/" + channelId + "/unsubscribe";
        mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .headers(TestMethods.getHttpHeaders(userToken))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertEquals(400, mvcResult.getResponse().getStatus());
    }

    @Test
    public void channelUnsubscribeByUnsubscribedUser() throws Exception {
        MvcResult mvcResult = TestMethods.createChannel(mvc, userToken);

        int channelId = (int) TestMethods.mapFromJson(mvcResult).get("id");
        String uri = "/api/channel/" + channelId + "/unsubscribe";
        TestMethods.registerWithEmailAndUsername(mvc, "random@mail.com", "unSubTestUser");
        String customUserToken = TestMethods.getTokenWithUsername(mvc, "unSubTestUser");
        mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .headers(TestMethods.getHttpHeaders(customUserToken))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertEquals(400, mvcResult.getResponse().getStatus());
    }

    @Test
    public void channelUnsubscribeBySubscribedUser() throws Exception {
        MvcResult mvcResult = TestMethods.createChannel(mvc, userToken);
        int channelId = (int) TestMethods.mapFromJson(mvcResult).get("id");
        String uri = "/api/channel/" + channelId + "/subscribe";
        TestMethods.registerWithEmailAndUsername(mvc, "random@gmail.com", "unSubTestUserSubscribed");
        String customUserToken = TestMethods.getTokenWithUsername(mvc, "unSubTestUserSubscribed");

        mvc.perform(MockMvcRequestBuilders.post(uri)
                .headers(TestMethods.getHttpHeaders(customUserToken))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        uri = "/api/channel/" + channelId + "/unsubscribe";
        mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .headers(TestMethods.getHttpHeaders(customUserToken))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());
    }

    @Test
    public void channelDelete() throws Exception {
        int channelId = (int) TestMethods
                .mapFromJson(TestMethods
                        .createChannel(mvc, userToken))
                .get("id");

        int ownedChannelBefore = TestMethods
                .mapFromJsonList(TestMethods.getRequest(mvc, "/api/channels/owned", userToken))
                .size();

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .delete("/api/channel/" + channelId)
                .headers(TestMethods.getHttpHeaders(userToken)))
                .andReturn();

        int ownedChannelAfter = TestMethods
                .mapFromJsonList(TestMethods.getRequest(mvc, "/api/channels/owned", userToken))
                .size();

        assertEquals(200, mvcResult.getResponse().getStatus());
        assertNotEquals(ownedChannelBefore, ownedChannelAfter);
    }
}
