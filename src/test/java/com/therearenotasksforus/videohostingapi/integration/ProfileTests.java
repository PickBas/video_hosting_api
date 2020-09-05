//package com.therearenotasksforus.videohostingapi.integration;
//
//import com.therearenotasksforus.videohostingapi.VideoHostingApiApplication;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotEquals;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = VideoHostingApiApplication.class,
//        properties = { "spring.jpa.hibernate.ddl-auto=create-drop" })
//@WebAppConfiguration
//@AutoConfigureMockMvc
//class ProfileTests {
//
//    @Autowired
//    public MockMvc mvc;
//
//    @Autowired
//    protected WebApplicationContext webApplicationContext;
//
//    public String userToken;
//
//    public void setUp() {
//        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//    }
//
//    @BeforeEach
//    public void preTest() throws Exception {
//        TestMethods.register(mvc);
//        userToken = TestMethods.getToken(mvc);
//    }
//
//    @Test
//    public void profilesListLoads() throws Exception {
//        super.register();
//
//        String uri = "/api/profiles";
//        String token = super.getToken();
//
//        MvcResult mvcResult = super.getRequest(uri, token);
//
//        int status = mvcResult.getResponse().getStatus();
//
//        ArrayList<Map<String, Object>> responseBodyArray = super.mapFromJsonList(mvcResult);
//
//        assertEquals(200, status);
//        assertNotEquals(0, responseBodyArray.size());
//        assertNotEquals(null, responseBodyArray.get(0).get("user"));
//        assertEquals(responseBodyArray.get(0).get("id"), responseBodyArray.get(0).get("user"));
//    }
//
//    @Test
//    public void profileByIdLoads() throws Exception {
//        super.register();
//
//        String uri = "/api/profile/id/1";
//        String token = super.getToken();
//
//        MvcResult mvcResult = super.getRequest(uri, token);
//
//        int status = mvcResult.getResponse().getStatus();
//
//        Map<String, Object> responseBody = super.mapFromJson(mvcResult);
//
//        assertEquals(200, status);
//        assertNotEquals(null, responseBody.get("customUrl"));
//        assertEquals(responseBody.get("id"), responseBody.get("user"));
//    }
//
//    @Test
//    public void profileByTokenLoads() throws Exception {
//        super.register();
//
//        String uri = "/api/profile";
//        String token = super.getToken();
//
//        MvcResult mvcResult = super.getRequest(uri, token);
//
//        int status = mvcResult.getResponse().getStatus();
//
//        Map<String, Object> responseBody = super.mapFromJson(mvcResult);
//
//        assertEquals(200, status);
//        assertNotEquals(null, responseBody.get("customUrl"));
//        assertEquals(responseBody.get("id"), responseBody.get("user"));
//    }
//
//    @Test
//    public void profileUpdate() throws Exception {
//        super.register();
//        String uri = "/api/profile/update";
//        String token = super.getToken();
//
//        Map<String, String> requestBody = new HashMap<>();
//        requestBody.put("aboutProfileInfo", "Updated profile information");
//        requestBody.put("gender", "M");
//        requestBody.put("country", "USA");
//        requestBody.put("customUrl", "test_custom_url");
//        requestBody.put("privateSublist", "true");
//
//        String jsonBody = this.mapToJson(requestBody);
//
//        MvcResult mvcResult = super.postRequest(uri, token, jsonBody);
//
//        int status = mvcResult.getResponse().getStatus();
//
//        Map <String, Object> responseBody = super.mapFromJson(mvcResult);
//
//        assertEquals(200, status);
//        assertEquals("Updated profile information", responseBody.get("aboutProfileInfo"));
//        assertEquals("M", responseBody.get("gender"));
//        assertEquals("USA", responseBody.get("country"));
//        assertEquals("test_custom_url", responseBody.get("customUrl"));
//        assertEquals(true, responseBody.get("privateSublist"));
//
//        uri = "/api/profile";
//        mvcResult = super.getRequest(uri, token);
//        status = mvcResult.getResponse().getStatus();
//
//        responseBody = super.mapFromJson(mvcResult);
//
//        assertEquals(200, status);
//        assertEquals("Updated profile information", responseBody.get("aboutProfileInfo"));
//        assertEquals("M", responseBody.get("gender"));
//        assertEquals("USA", responseBody.get("country"));
//        assertEquals("test_custom_url", responseBody.get("customUrl"));
//        assertEquals(true, responseBody.get("privateSublist"));
//    }
//
//    @Test
//    public void profileUpdateAvatar() throws Exception {
//        super.register();
//        String uri = "/api/profile/upload/avatar";
//        String token = super.getToken();
//
//        String prevAvatar = super.mapFromJson(super.getRequest("/api/profile", token))
//                .get("avatarUrl").toString();
//
//        final MockMultipartFile avatar = new MockMultipartFile("file",
//                "test.png",
//                "image/png",
//                "test.png".getBytes());
//
//        final MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
//                .multipart(uri)
//                .file(avatar)
//                .headers(this.getHttpHeaders(token))
//                .contentType(MediaType.MULTIPART_FORM_DATA))
//                .andReturn();
//
//        String currentAvatar = super.mapFromJson(super.getRequest("/api/profile", token))
//                .get("avatarUrl").toString();
//
//        assertEquals(200, mvcResult.getResponse().getStatus());
//        assertNotEquals(prevAvatar, currentAvatar);
//    }
//}
