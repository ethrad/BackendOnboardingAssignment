package com.sparta.backendonboardingassignment.domain.user.service;

import org.springframework.stereotype.Service;

import com.sparta.backendonboardingassignment.common.exception.InvalidRequestException;
import com.sparta.backendonboardingassignment.domain.user.dto.response.UserInfoResponse;
import com.sparta.backendonboardingassignment.domain.user.entity.User;
import com.sparta.backendonboardingassignment.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	public UserInfoResponse getUserInfo(String username) {
		User user = userRepository.findByUsername(username).orElseThrow(
			() -> new InvalidRequestException("가입되지 않은 유저입니다."));

		return new UserInfoResponse(user.getUsername(), user.getNickname(), user.getAuthorities());
	}
}
