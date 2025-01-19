package com.sparta.backendonboardingassignment.domain.user.dto.response;

import java.util.List;

import com.sparta.backendonboardingassignment.domain.user.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserInfoResponse {
	private String username;
	private String nickname;
	private List<UserRole> authorities;
}
