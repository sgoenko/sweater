package com.hay.sweater;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;

import com.hay.sweater.controller.MessageController;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
public class LoginTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private MessageController controller;

	@Test
	public void contextLoads() throws Exception {
		this.mockMvc.perform(get("/"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Hello, guest")))
				.andExpect(content().string(containsString("Please, login")));
	}
	
	@Test
	public void accessDeniedTest() throws Exception {
		this.mockMvc.perform(get("/main"))
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/login"));
	}

	@Test
	@Sql(value= {"/create-user-before.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(value= {"/create-user-after.sql"}, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void correctLoginTest() throws Exception {
		this.mockMvc.perform(SecurityMockMvcRequestBuilders.formLogin().user("dru").password("1"))
				.andDo(print())
				.andExpect(redirectedUrl("/"));
	}

	@Test
	public void badCfedentials() throws Exception {
		this.mockMvc.perform(post("/login").param("user", "Alfred"))
				.andDo(print())
				.andExpect(status().isForbidden());
	}
	
}