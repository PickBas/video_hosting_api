package com.therearenotasksforus.videohostingapi;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@SpringBootTest(properties = { "spring.jpa.hibernate.ddl-auto=create-drop" })
@RunWith(SpringRunner.class)
class ProfileTests extends AbstractTest {

    @Test
    public void profilesListLoads() throws Exception {
        super.register();

        String uri = "/api/profiles";
        String token = super.getToken();

        MvcResult mvcResult = super.getRequest(uri, token);

        int status = mvcResult.getResponse().getStatus();

        ArrayList<Map<String, Object>> responseBodyArray = super.mapFromJsonList(mvcResult);

        assertEquals(200, status);
        assertNotEquals(0, responseBodyArray.size());
        assertNotEquals(null, responseBodyArray.get(0).get("user"));
        assertEquals(responseBodyArray.get(0).get("id"), responseBodyArray.get(0).get("user"));
    }

    @Test
    public void profileByIdLoads() throws Exception {
        super.register();

        String uri = "/api/profile/id/1";
        String token = super.getToken();

        MvcResult mvcResult = super.getRequest(uri, token);

        int status = mvcResult.getResponse().getStatus();

        Map<String, Object> responseBody = super.mapFromJson(mvcResult);

        assertEquals(200, status);
        assertNotEquals(null, responseBody.get("customUrl"));
        assertEquals(responseBody.get("id"), responseBody.get("user"));
    }

    @Test
    public void profileByTokenLoads() throws Exception {
        super.register();

        String uri = "/api/profile";
        String token = super.getToken();

        MvcResult mvcResult = super.getRequest(uri, token);

        int status = mvcResult.getResponse().getStatus();

        Map<String, Object> responseBody = super.mapFromJson(mvcResult);

        assertEquals(200, status);
        assertNotEquals(null, responseBody.get("customUrl"));
        assertEquals(responseBody.get("id"), responseBody.get("user"));
    }

    @Test
    public void profileUpdate() throws Exception {
        super.register();
        String uri = "/api/profile/update";
        String token = super.getToken();

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("aboutProfileInfo", "Updated profile information");
        requestBody.put("gender", "M");
        requestBody.put("country", "USA");
        requestBody.put("customUrl", "test_custom_url");
        requestBody.put("privateSublist", "true");

        String jsonBody = this.mapToJson(requestBody);

        MvcResult mvcResult = super.postRequest(uri, token, jsonBody);

        int status = mvcResult.getResponse().getStatus();

        Map <String, Object> responseBody = super.mapFromJson(mvcResult);

        assertEquals(200, status);
        assertEquals("Updated profile information", responseBody.get("aboutProfileInfo"));
        assertEquals("M", responseBody.get("gender"));
        assertEquals("USA", responseBody.get("country"));
        assertEquals("test_custom_url", responseBody.get("customUrl"));
        assertEquals(true, responseBody.get("privateSublist"));

        uri = "/api/profile";
        mvcResult = super.getRequest(uri, token);
        status = mvcResult.getResponse().getStatus();

        responseBody = super.mapFromJson(mvcResult);

        assertEquals(200, status);
        assertEquals("Updated profile information", responseBody.get("aboutProfileInfo"));
        assertEquals("M", responseBody.get("gender"));
        assertEquals("USA", responseBody.get("country"));
        assertEquals("test_custom_url", responseBody.get("customUrl"));
        assertEquals(true, responseBody.get("privateSublist"));
    }

    @Test
    public void profileUpdateAvatar() throws Exception {
        super.register();
        String uri = "/api/profile/upload/avatar";
        String token = super.getToken();

        final InputStream inputStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("test.png");

        final MockMultipartFile avatar = new MockMultipartFile("file",
                "test.png",
                "image/png",
                inputStream);

        final MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .multipart(uri)
                .file(avatar)
                .headers(this.getHttpHeaders(token))
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());
    }
}
