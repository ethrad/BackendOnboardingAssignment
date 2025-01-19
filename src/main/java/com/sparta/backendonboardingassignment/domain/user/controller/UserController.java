package com.sparta.backendonboardingassignment.domain.user.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.backendonboardingassignment.domain.user.dto.response.UserInfoResponse;
import com.sparta.backendonboardingassignment.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserController {
	private final UserService userService;

	@GetMapping("/v1/user")
	public UserInfoResponse getUserInfo(Authentication authentication) {
		String username = authentication.getName();

		return userService.getUserInfo(username);
	}
}
