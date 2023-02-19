package com.therearenotasksforus.videohostingapi;

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
class RegistrationTest {

	@Autowired
	protected MockMvc mvc;

	@Autowired
	protected WebApplicationContext webApplicationContext;

	public void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void registerGetRequest() throws Exception {
		String uri = "/api/auth/register";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		Assertions.assertEquals(405, status);
	}

	@Test
	public void registerPostRequest() throws Exception {
		String uri = "/api/auth/register";
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("email", "test1@test.test");
		requestBody.put("username", "test1");
		requestBody.put("password", "Asdf123!");
		String jsonBody = TestMethods.mapToJson(requestBody);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(jsonBody)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		Map<String, Object> responseBody = TestMethods.mapFromJson(mvcResult);
		Assertions.assertEquals(201, status);
		Assertions.assertNotEquals(null, responseBody.get("username"));
		Assertions.assertNotEquals(null, responseBody.get("email"));
	}

	public int getStatusWithWeakPassword(Map<String, String> requestBody, String password) throws Exception {
		String uri = "/api/auth/register";
		requestBody.put("password", password);
		String jsonBody = TestMethods.mapToJson(requestBody);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
				.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(jsonBody)).andReturn();
		return mvcResult.getResponse().getStatus();
	}

	@Test
	public void registerWithWeakPassword() throws Exception {
		String uri = "/api/auth/register";
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("email", "test2@test.test");
		requestBody.put("username", "test2");
		requestBody.put("password", "123");
		String jsonBody = TestMethods.mapToJson(requestBody);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(jsonBody)).andReturn();
		int statusWithThreeNums = mvcResult.getResponse().getStatus();
		Assertions.assertEquals(400, statusWithThreeNums);
		int statusWithEightNums = getStatusWithWeakPassword(requestBody, "12345678");
		Assertions.assertEquals(400, statusWithEightNums);
		int statusWithEightLetters = getStatusWithWeakPassword(requestBody, "asdfghjk");
		Assertions.assertEquals(400, statusWithEightLetters);
		int statusWithoutSpecialCharsAndUppercase = getStatusWithWeakPassword(requestBody, "asdf1234");
		Assertions.assertEquals(400, statusWithoutSpecialCharsAndUppercase);
		int statusWithoutSpecialChars = getStatusWithWeakPassword(requestBody, "Asdf1234");
		Assertions.assertEquals(400, statusWithoutSpecialChars);
		int statusShortPasswordCorrectPattern = getStatusWithWeakPassword(requestBody, "Asd123!");
		Assertions.assertEquals(400, statusShortPasswordCorrectPattern);
	}

	@Test
	public void registerWithUsedEmailAndUsername() throws Exception {
		String uri = "/api/auth/register";
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("email", "test2@test.test");
		requestBody.put("username", "test2");
		requestBody.put("password", "Asdf123!");
		String jsonBody = TestMethods.mapToJson(requestBody);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(jsonBody)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		Assertions.assertEquals(201, status);
		requestBody = new HashMap<>();
		requestBody.put("email", "test2@test.test");
		requestBody.put("username", "test2");
		requestBody.put("password", "Asdf123!");
		jsonBody = TestMethods.mapToJson(requestBody);
		mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(jsonBody)).andReturn();
		status = mvcResult.getResponse().getStatus();
		Assertions.assertEquals(400, status);

	}
}
