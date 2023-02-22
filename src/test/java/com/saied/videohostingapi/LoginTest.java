package com.saied.videohostingapi;

import org.junit.jupiter.api.Assertions;
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

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = VideoHostingApiApplication.class,
        properties = { "spring.jpa.hibernate.ddl-auto=create-drop" })
@WebAppConfiguration
@AutoConfigureMockMvc
class LoginTest {

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void loginPostRequestTest() throws Exception {
        TestMethods.register(mvc);
        String uri = "/api/auth/login";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("username", "firsttestuser");
        requestBody.put("password", "Asdf123!");
        String jsonBody = TestMethods.mapToJson(requestBody);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonBody)).andReturn();
        Map<String, Object> responseBody = TestMethods.mapFromJson(mvcResult);
        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(200, status);
        Assertions.assertNotEquals(null, responseBody.get("access_token"));
    }

    @Test
    public void loginPostRequestBadCredentialsTest() throws Exception {
        String uri = "/api/auth/login";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("username", "BadCredentials");
        requestBody.put("password", "BadCredentials");
        String jsonBody = TestMethods.mapToJson(requestBody);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonBody)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(400, status);
    }

    @Test
    public void refreshTokenTest() throws Exception {
        TestMethods.register(mvc);
        String uri = "/api/auth/token/refresh";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("username", "firsttestuser");
        requestBody.put("password", "Asdf123!");
        String jsonBody = TestMethods.mapToJson(requestBody);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonBody)).andReturn();
        Assertions.assertEquals(200, mvcResult.getResponse().getStatus());
        Map<String, Object> responseBody = TestMethods.mapFromJson(mvcResult);
        String accessToken = (String)responseBody.get("access_token");
        String refreshToken = (String)responseBody.get("refresh_token");
        Assertions.assertNotNull(accessToken);
        Assertions.assertNotNull(refreshToken);
        mvcResult = mvc.perform(MockMvcRequestBuilders
                        .get("/api/auth/token/refresh").servletPath("/api/auth/token/refresh")
                        .header("AUTHORIZATION", "Bearer " + refreshToken))
                .andReturn();
        Map<String, Object> response = TestMethods.mapFromJson(mvcResult);
        Assertions.assertEquals(200, mvcResult.getResponse().getStatus());
        Assertions.assertNotNull(response.get("access_token"));
        Assertions.assertEquals(refreshToken, response.get("refresh_token"));
    }
}
