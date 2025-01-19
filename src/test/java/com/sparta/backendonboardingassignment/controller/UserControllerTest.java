package com.sparta.backendonboardingassignment.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.sparta.backendonboardingassignment.domain.user.entity.User;
import com.sparta.backendonboardingassignment.domain.user.repository.UserRepository;
import com.sparta.backendonboardingassignment.mock.WithCustomMockUser;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	void setUp() {
		User user = new User("test", "test1234", "testUser");

		userRepository.save(user);
	}

	@Test
	@WithCustomMockUser
	public void IfUserExistsThenGetUserInfoReturnsSuccess() throws Exception {

		mockMvc.perform(get("/v1/user")
				.header("X-AUTH-TOKEN", "aaaaaaa"))
			.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@AfterEach
	public void tearDown() {
		userRepository.deleteAll();
	}
}