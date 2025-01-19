package com.sparta.backendonboardingassignment.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupRequest {

	@NotBlank
	private String username;
	@NotBlank
	private String password;
	@NotBlank
	private String nickname;

}