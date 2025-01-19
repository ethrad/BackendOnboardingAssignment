package com.sparta.backendonboardingassignment.domain.auth.controller;

import org.springframework.web.bind.annotation.RestController;

import com.sparta.backendonboardingassignment.domain.auth.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;
}
