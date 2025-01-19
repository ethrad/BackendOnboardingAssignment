package com.sparta.backendonboardingassignment.domain.user.enums;

import java.util.Arrays;

import com.sparta.backendonboardingassignment.common.exception.InvalidRequestException;

import lombok.Getter;

@Getter
public enum UserRole {
	USER(Authority.USER),  // 사용자 권한
	ADMIN(Authority.ADMIN);  // 관리자 권한

	public static UserRole of(String role) {
		return Arrays.stream(UserRole.values())
			.filter(r -> r.name().equalsIgnoreCase(role))
			.findFirst()
			.orElseThrow(() -> new InvalidRequestException("유효하지 않은 UerRole"));
	}

	private final String authorityName;

	UserRole(String authority) {
		this.authorityName = authority;
	}

	public static class Authority {
		public static final String USER = "ROLE_USER";
		public static final String ADMIN = "ROLE_ADMIN";
	}
}
