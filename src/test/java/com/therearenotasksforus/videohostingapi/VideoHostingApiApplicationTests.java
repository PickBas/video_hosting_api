package com.therearenotasksforus.videohostingapi;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
class VideoHostingApiApplicationTests extends AbstractTest {

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
		requestBody.put("email", "test@test.test");
		requestBody.put("name", "test");
		requestBody.put("password", "asdf123!");

		String jsonBody = super.mapToJson(requestBody);

		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(jsonBody)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(201, status);
	}

	@Test
	public void profilesLoads() throws Exception {
		String uri = "/api/profiles";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
											.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		int jsonLength = mvcResult.getResponse().getContentLength();
		assertEquals(200, status);
		assertEquals(0, jsonLength);
	}

//	@Test
//	void contextLoads() {
//	}

}
