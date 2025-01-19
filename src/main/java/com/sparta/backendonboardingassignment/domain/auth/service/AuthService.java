package com.sparta.backendonboardingassignment.domain.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.backendonboardingassignment.common.exception.InvalidRequestException;
import com.sparta.backendonboardingassignment.config.JwtUtil;
import com.sparta.backendonboardingassignment.domain.auth.dto.request.SigninRequest;
import com.sparta.backendonboardingassignment.domain.auth.dto.request.SignupRequest;
import com.sparta.backendonboardingassignment.domain.auth.dto.response.SigninResponse;
import com.sparta.backendonboardingassignment.domain.auth.dto.response.SignupResponse;
import com.sparta.backendonboardingassignment.domain.auth.exception.AuthException;
import com.sparta.backendonboardingassignment.domain.user.entity.User;
import com.sparta.backendonboardingassignment.domain.user.repository.UserRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	@Transactional
	public SignupResponse signup(@Valid SignupRequest signupRequest) {
		if (userRepository.existsByUsername(signupRequest.getUsername())) {
			throw new InvalidRequestException("이미 존재하는 사용자 이름입니다.");
		}

		String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

		User newUser = new User(
			signupRequest.getUsername(),
			encodedPassword,
			signupRequest.getNickname()
		);
		User savedUser = userRepository.save(newUser);

		return new SignupResponse(savedUser.getUsername(), savedUser.getNickname(), savedUser.getAuthorities());
	}

	public SigninResponse sign(@Valid SigninRequest signinRequest) {
		User user = userRepository.findByUsername(signinRequest.getUsername()).orElseThrow(
			() -> new InvalidRequestException("가입되지 않은 유저입니다."));

		if (!passwordEncoder.matches(signinRequest.getPassword(), user.getPassword())) {
			throw new AuthException("잘못된 비밀번호입니다.");
		}

		String bearerToken = jwtUtil.createToken(user.getId(), user.getUsername(), user.getAuthorities());

		return new SigninResponse(bearerToken);
	}
}
