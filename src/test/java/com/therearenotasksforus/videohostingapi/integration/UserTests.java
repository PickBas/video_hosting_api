package com.therearenotasksforus.videohostingapi.integration;

import com.therearenotasksforus.videohostingapi.VideoHostingApiApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = VideoHostingApiApplication.class,
        properties = { "spring.jpa.hibernate.ddl-auto=create-drop" })
@WebAppConfiguration
@AutoConfigureMockMvc
class UserTests {

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
    public void usersListLoads() throws Exception {
        String uri = "/api/users";
        MvcResult mvcResult = TestMethods.getRequest(mvc, uri, userToken);
        ArrayList<Map<String, Object>> responseBodyArray = TestMethods.mapFromJsonList(mvcResult);

        assertEquals(200, mvcResult.getResponse().getStatus());
        assertNotEquals(0, responseBodyArray.size());
        assertNotEquals(null, responseBodyArray.get(0).get("username"));
        assertEquals(responseBodyArray.get(0).get("id"), responseBodyArray.get(0).get("profile"));
        assertNotEquals(null, responseBodyArray.get(0).get("email"));
    }

    @Test
    public void userByIdLoads() throws Exception {
        String uri = "/api/user/id/1";
        MvcResult mvcResult = TestMethods.getRequest(mvc, uri, userToken);
        Map<String, Object> responseBody = TestMethods.mapFromJson(mvcResult);

        assertEquals(200, mvcResult.getResponse().getStatus());
        assertNotEquals(null, responseBody.get("username"));
        assertEquals(responseBody.get("id"), responseBody.get("profile"));
        assertNotEquals(null, responseBody.get("email"));
    }

    @Test
    public void userByTokenLoads() throws Exception {
        String uri = "/api/user";
        MvcResult mvcResult = TestMethods.getRequest(mvc, uri, userToken);
        Map<String, Object> responseBody = TestMethods.mapFromJson(mvcResult);

        assertEquals(200, mvcResult.getResponse().getStatus());
        assertNotEquals(null, responseBody.get("username"));
        assertEquals(responseBody.get("id"), responseBody.get("profile"));
        assertNotEquals(null, responseBody.get("email"));
    }

    @Test
    public void userUpdate() throws Exception {
        String uri = "/api/user/update";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("firstName", "updated_first_name");
        requestBody.put("lastName", "updated_last_name");

        String jsonBody = TestMethods.mapToJson(requestBody);
        MvcResult mvcResult = TestMethods.postRequest(mvc, uri, userToken, jsonBody);
        Map <String, Object> responseBody = TestMethods.mapFromJson(mvcResult);

        assertEquals(200, mvcResult.getResponse().getStatus());
        assertEquals("updated_first_name", responseBody.get("firstName"));
        assertEquals("updated_last_name", responseBody.get("lastName"));

        uri = "/api/user";
        mvcResult = TestMethods.getRequest(mvc, uri, userToken);
        responseBody = TestMethods.mapFromJson(mvcResult);

        assertEquals(200, mvcResult.getResponse().getStatus());
        assertEquals("updated_first_name", responseBody.get("firstName"));
        assertEquals("updated_last_name", responseBody.get("lastName"));
    }
}