package com.therearenotasksforus.videohostingapi;

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
import static org.junit.Assert.assertNotEquals;

@SpringBootTest(properties = { "spring.jpa.hibernate.ddl-auto=create-drop" })
@RunWith(SpringRunner.class)
class RegistrationTests extends AbstractTest {

	@Test
	public void registerGetRequest() throws Exception {
		String uri = "/api/auth/register";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(405, status);
	}

	@Test
	public void registerPostRequest() throws Exception {
		String uri = "/api/auth/register";

		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("email", "test1@test.test");
		requestBody.put("username", "test1");
		requestBody.put("password", "asdf123!");

		String jsonBody = super.mapToJson(requestBody);

		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(jsonBody)).andReturn();

		int status = mvcResult.getResponse().getStatus();

		Map<String, Object> responseBody = super.mapFromJson(mvcResult);

		assertEquals(201, status);
		assertNotEquals(null, responseBody.get("username"));
		assertNotEquals(null, responseBody.get("email"));
	}

	@Test
	public void registerWithWeakPassword() throws Exception {
		String uri = "/api/auth/register";

		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("email", "test2@test.test");
		requestBody.put("username", "test2");
		requestBody.put("password", "asd");

		String jsonBody = super.mapToJson(requestBody);

		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(jsonBody)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(400, status);
	}

}
