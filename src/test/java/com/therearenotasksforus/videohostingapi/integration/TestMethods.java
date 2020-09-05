package com.therearenotasksforus.videohostingapi.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.therearenotasksforus.videohostingapi.VideoHostingApiApplication;
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

public class TestMethods {

    public static String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    public static Map<String, Object> mapFromJson(MvcResult mvcResult)
            throws IOException {

        return new ObjectMapper().readValue(
                mvcResult.getResponse().getContentAsString(),
                HashMap.class
        );
    }

    public static ArrayList<Map<String, Object>> mapFromJsonList(MvcResult mvcResult)
            throws IOException {

        return new ObjectMapper().readValue(
                mvcResult.getResponse().getContentAsString(),
                ArrayList.class
        );
    }

    public static void register(MockMvc mvc) throws Exception {
        String uri = "/api/auth/register";

        Map<String, String> requestBodyRegister = new HashMap<>();
        requestBodyRegister.put("email", "firsttestuser@firsttestuser.com");
        requestBodyRegister.put("username", "firsttestuser");
        requestBodyRegister.put("password", "asdf123!");

        String jsonBodyRegister = mapToJson(requestBodyRegister);

        mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonBodyRegister)).andReturn();
    }

    public static int registerWithEmailAndUsername(MockMvc mvc, String email, String username) throws Exception {
        String uri = "/api/auth/register";

        Map<String, String> requestBodyRegister = new HashMap<>();
        requestBodyRegister.put("email", email);
        requestBodyRegister.put("username", username);
        requestBodyRegister.put("password", "asdf123!");

        String jsonBodyRegister = mapToJson(requestBodyRegister);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonBodyRegister)).andReturn();
        return (int)mapFromJson(mvcResult).get("id");
    }

    public static MvcResult login(MockMvc mvc) throws Exception {
        String uri = "/api/auth/login";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("username", "firsttestuser");
        requestBody.put("password", "asdf123!");

        String jsonBody = mapToJson(requestBody);

        return mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonBody)).andReturn();
    }

    public static MvcResult loginWithUsername(MockMvc mvc, String username) throws Exception {
        String uri = "/api/auth/login";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("username", username);
        requestBody.put("password", "asdf123!");

        String jsonBody = mapToJson(requestBody);

        return mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonBody)).andReturn();
    }

    public static MvcResult createChannel(MockMvc mvc, String token) throws Exception {
        String uri = "/api/channel/create";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", "TestChannel");
        requestBody.put("info", "TestChannel information");

        String requestBodyJson = mapToJson(requestBody);
        return postRequest(mvc, uri, token, requestBodyJson);
    }

    public static String getToken(MockMvc mvc) throws Exception {
        return "TOKEN_" + mapFromJson(login(mvc)).get("token").toString();
    }

    public static String getTokenWithUsername(MockMvc mvc, String username) throws Exception {
        return "TOKEN_" + mapFromJson(loginWithUsername(mvc, username))
                .get("token")
                .toString();
    }

    public static HttpHeaders getHttpHeaders (String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);

        return headers;
    }

    public static MvcResult getRequest(MockMvc mvc, String uri, String token) throws Exception {
        return mvc.perform(MockMvcRequestBuilders.get(uri)
                .headers(getHttpHeaders(token)))
                .andReturn();
    }

    public static MvcResult postRequest(MockMvc mvc, String uri, String token, String jsonBody) throws Exception {
        return mvc.perform(MockMvcRequestBuilders.post(uri)
                .headers(getHttpHeaders(token))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonBody)).andReturn();
    }

    public static ArrayList<Object> getLongArrayByKey(Map<String, Object> responseBody, String key) {
        return (ArrayList) responseBody.get(key);
    }

    public static MvcResult uploadVideoWithUriAndToken(MockMvc mvc, String uri, String token) throws Exception {
        final MockMultipartFile video = new MockMultipartFile("file",
                "test.mp4",
                "video/mp4",
                "test video".getBytes());

        return mvc.perform(MockMvcRequestBuilders
                .multipart(uri)
                .file(video)
                .headers(getHttpHeaders(token))
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andReturn();
    }
}