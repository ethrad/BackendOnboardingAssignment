package com.sparta.backendonboardingassignment.domain.auth.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.backendonboardingassignment.domain.auth.dto.request.SigninRequest;
import com.sparta.backendonboardingassignment.domain.auth.dto.request.SignupRequest;
import com.sparta.backendonboardingassignment.domain.auth.dto.response.SigninResponse;
import com.sparta.backendonboardingassignment.domain.auth.dto.response.SignupResponse;
import com.sparta.backendonboardingassignment.domain.auth.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

	@PostMapping("/auth/signup")
	public SignupResponse signup(@Valid @RequestBody SignupRequest signupRequest) {
		return authService.signup(signupRequest);
	}

	@PostMapping("/auth/sign")
	public SigninResponse sign(@Valid @RequestBody SigninRequest signinRequest) {
		return authService.sign(signinRequest);
	}

}
