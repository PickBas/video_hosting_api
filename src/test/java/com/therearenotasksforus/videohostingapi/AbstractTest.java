package com.therearenotasksforus.videohostingapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = VideoHostingApiApplication.class)
@WebAppConfiguration
@AutoConfigureMockMvc
public abstract class AbstractTest {

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    protected Map<String, Object> mapFromJson(MvcResult mvcResult)
            throws IOException {

        return new ObjectMapper().readValue(
                mvcResult.getResponse().getContentAsString(),
                HashMap.class
        );
    }

    protected ArrayList<Map<String, Object>> mapFromJsonList(MvcResult mvcResult)
            throws IOException {

        return new ObjectMapper().readValue(
                mvcResult.getResponse().getContentAsString(),
                ArrayList.class
        );
    }

    public void register() throws Exception {
        String uri = "/api/auth/register";

        Map<String, String> requestBodyRegister = new HashMap<>();
        requestBodyRegister.put("email", "firsttestuser@firsttestuser.com");
        requestBodyRegister.put("username", "firsttestuser");
        requestBodyRegister.put("password", "asdf123!");

        String jsonBodyRegister = this.mapToJson(requestBodyRegister);

        mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonBodyRegister)).andReturn();
    }

    public int registerWithEmailAndUsername(String email, String username) throws Exception {
        String uri = "/api/auth/register";

        Map<String, String> requestBodyRegister = new HashMap<>();
        requestBodyRegister.put("email", email);
        requestBodyRegister.put("username", username);
        requestBodyRegister.put("password", "asdf123!");

        String jsonBodyRegister = this.mapToJson(requestBodyRegister);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonBodyRegister)).andReturn();
        return (int)mapFromJson(mvcResult).get("id");
    }

    public MvcResult login() throws Exception {
        String uri = "/api/auth/login";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("username", "firsttestuser");
        requestBody.put("password", "asdf123!");

        String jsonBody = this.mapToJson(requestBody);

        return mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonBody)).andReturn();
    }

    public MvcResult loginWithUsername(String username) throws Exception {
        String uri = "/api/auth/login";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("username", username);
        requestBody.put("password", "asdf123!");

        String jsonBody = this.mapToJson(requestBody);

        return mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonBody)).andReturn();
    }

    public MvcResult createChannel(String token) throws Exception {
        String uri = "/api/channel/create";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", "TestChannel");
        requestBody.put("info", "TestChannel information");

        String requestBodyJson = this.mapToJson(requestBody);
        return this.postRequest(uri, token, requestBodyJson);
    }

    public String getToken() throws Exception {
        return "TOKEN_" + this.mapFromJson(this.login()).get("token").toString();
    }

    public String getTokenWithUsername(String username) throws Exception {
        return "TOKEN_" + this
                .mapFromJson(this.loginWithUsername(username))
                .get("token")
                .toString();
    }

    public HttpHeaders getHttpHeaders (String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);

        return headers;
    }

    protected MvcResult getRequest(String uri, String token) throws Exception {
        return mvc.perform(MockMvcRequestBuilders.get(uri)
                .headers(this.getHttpHeaders(token)))
                .andReturn();
    }

    protected MvcResult postRequest(String uri, String token, String jsonBody) throws Exception {
        return mvc.perform(MockMvcRequestBuilders.post(uri)
                .headers(this.getHttpHeaders(token))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonBody)).andReturn();
    }

    public ArrayList<Object> getLongArrayByKey(Map<String, Object> responseBody, String key) {
        return (ArrayList) responseBody.get(key);
    }

    public MvcResult uploadVideoWithUriAndToken(String uri, String token) throws Exception {
        final MockMultipartFile video = new MockMultipartFile("file",
                "test.mp4",
                "video/mp4",
                "test video".getBytes());

        return mvc.perform(MockMvcRequestBuilders
                .multipart(uri)
                .file(video)
                .headers(this.getHttpHeaders(token))
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andReturn();
    }
}