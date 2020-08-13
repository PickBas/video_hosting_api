package com.therearenotasksforus.videohostingapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.therearenotasksforus.videohostingapi.dto.user.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@SpringBootTest(properties = { "spring.jpa.hibernate.ddl-auto=create-drop" })
@RunWith(SpringRunner.class)
class UserTests extends AbstractTest {

    @Test
    public void usersEmptyListLoads() throws Exception {
        String uri = "/api/users";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        int responseLength = mvcResult.getResponse().getContentLength();

        assertEquals(200, status);
        assertEquals(0, responseLength);
    }

    @Test
    public void usersListLoads() throws Exception {
        super.register();

        String uri = "/api/users";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();

        int status = mvcResult.getResponse().getStatus();

        ArrayList<Map<String, Object>> responseBodyArray = new ObjectMapper().readValue(
                mvcResult.getResponse().getContentAsString(),
                ArrayList.class
        );

        assertEquals(200, status);
        assertNotEquals(null, responseBodyArray.get(0).get("username"));
    }

    @Test
    public void userLoad() throws Exception {
        super.register();

        String uri = "/api/user/id/1";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();

        int status = mvcResult.getResponse().getStatus();

        Map<String, Object> responseBody = new ObjectMapper().readValue(
                mvcResult.getResponse().getContentAsString(),
                HashMap.class
        );

        assertEquals(200, status);
        assertNotEquals(null, responseBody.get("username"));
        assertEquals(responseBody.get("id"), responseBody.get("profile"));
        assertNotEquals(null, responseBody.get("email"));
    }
}