package com.therearenotasksforus.videohostingapi;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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
class UserTests extends AbstractTest {

    @Test
    public void usersListLoads() throws Exception {
        super.register();

        String uri = "/api/users";
        String token = super.getToken();

        MvcResult mvcResult = super.getRequest(uri, token);

        int status = mvcResult.getResponse().getStatus();

        ArrayList<Map<String, Object>> responseBodyArray = super.mapFromJsonList(mvcResult);

        assertEquals(200, status);
        assertNotEquals(0, responseBodyArray.size());
        assertNotEquals(null, responseBodyArray.get(0).get("username"));
        assertEquals(responseBodyArray.get(0).get("id"), responseBodyArray.get(0).get("profile"));
        assertNotEquals(null, responseBodyArray.get(0).get("email"));
    }

    @Test
    public void userByIdLoads() throws Exception {
        super.register();

        String uri = "/api/user/id/1";
        String token = super.getToken();

        MvcResult mvcResult = super.getRequest(uri, token);

        int status = mvcResult.getResponse().getStatus();

        Map<String, Object> responseBody = super.mapFromJson(mvcResult);

        assertEquals(200, status);
        assertNotEquals(null, responseBody.get("username"));
        assertEquals(responseBody.get("id"), responseBody.get("profile"));
        assertNotEquals(null, responseBody.get("email"));
    }

    @Test
    public void userByTokenLoads() throws Exception {
        super.register();

        String uri = "/api/user";
        String token = super.getToken();

        MvcResult mvcResult = super.getRequest(uri, token);

        int status = mvcResult.getResponse().getStatus();

        Map<String, Object> responseBody = super.mapFromJson(mvcResult);

        assertEquals(200, status);
        assertNotEquals(null, responseBody.get("username"));
        assertEquals(responseBody.get("id"), responseBody.get("profile"));
        assertNotEquals(null, responseBody.get("email"));
    }

    @Test
    public void userUpdate() throws Exception {
        super.register();
        String uri = "/api/user/update";
        String token = super.getToken();

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("firstName", "updated_first_name");
        requestBody.put("lastName", "updated_last_name");

        String jsonBody = this.mapToJson(requestBody);

        MvcResult mvcResult = super.postRequest(uri, token, jsonBody);

        int status = mvcResult.getResponse().getStatus();

        Map <String, Object> responseBody = super.mapFromJson(mvcResult);

        assertEquals(200, status);
        assertEquals("updated_first_name", responseBody.get("firstName"));
        assertEquals("updated_last_name", responseBody.get("lastName"));

        uri = "/api/user";
        mvcResult = super.getRequest(uri, token);
        status = mvcResult.getResponse().getStatus();

        responseBody = super.mapFromJson(mvcResult);

        assertEquals(200, status);
        assertEquals("updated_first_name", responseBody.get("firstName"));
        assertEquals("updated_last_name", responseBody.get("lastName"));
    }
}