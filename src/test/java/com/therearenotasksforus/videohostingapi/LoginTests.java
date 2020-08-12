package com.therearenotasksforus.videohostingapi;

import com.therearenotasksforus.videohostingapi.models.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@SpringBootTest(properties = { "spring.jpa.hibernate.ddl-auto=create-drop" })
@RunWith(SpringRunner.class)
class LoginTests extends AbstractTest {

    @Test
    public void loginPostRequestTest() throws Exception {
        String uri = "/api/auth/register";

        Map<String, String> requestBodyRegister = new HashMap<>();
        requestBodyRegister.put("email", "firsttestuser@firsttestuser.com");
        requestBodyRegister.put("username", "firsttestuser");
        requestBodyRegister.put("password", "asdf123!");

        String jsonBodyRegister = super.mapToJson(requestBodyRegister);

        mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonBodyRegister)).andReturn();

        uri = "/api/auth/login";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("username", "firsttestuser");
        requestBody.put("password", "asdf123!");

        String jsonBody = super.mapToJson(requestBody);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonBody)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    public void loginPostRequestBadCredentialsTest() throws Exception {
        String uri = "/api/auth/login";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("username", "firsttestuser");
        requestBody.put("password", "asdf123!");

        String jsonBody = super.mapToJson(requestBody);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonBody)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(400, status);
    }
}
