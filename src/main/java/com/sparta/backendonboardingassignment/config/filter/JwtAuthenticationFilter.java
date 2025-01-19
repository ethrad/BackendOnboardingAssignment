package com.sparta.backendonboardingassignment.config.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.backendonboardingassignment.config.JwtUtil;
import com.sparta.backendonboardingassignment.domain.auth.dto.request.SigninRequest;
import com.sparta.backendonboardingassignment.domain.auth.dto.response.SigninResponse;
import com.sparta.backendonboardingassignment.domain.user.enums.UserRole;
import com.sparta.backendonboardingassignment.security.UserDetailsImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private final JwtUtil jwtUtil;

	public JwtAuthenticationFilter(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
		setFilterProcessesUrl("/auth/sign");
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
		AuthenticationException {
		log.info("로그인 시도");
		try {
			SigninRequest requestDto = new ObjectMapper().readValue(request.getInputStream(), SigninRequest.class);

			return getAuthenticationManager().authenticate(
				new UsernamePasswordAuthenticationToken(
					requestDto.getUsername(),
					requestDto.getPassword(),
					null
				)
			);
		} catch (IOException e) {
			log.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException,
		ServletException {
		log.info("로그인 성공 및 JWT 생성");
		Long id = ((UserDetailsImpl)authResult.getPrincipal()).getUser().getId();
		String username = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getUsername();
		List<UserRole> roles = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getAuthorities();

		String token = jwtUtil.createToken(id, username, roles);
		jwtUtil.addJwtToCookie(token, response);

		SigninResponse signinResponse = new SigninResponse(token);

		// 응답에 SigninResponse 반환
		response.setContentType("application/json");
		new ObjectMapper().writeValue(response.getOutputStream(), signinResponse);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
		log.info("로그인 실패");
		response.setStatus(401);
	}
}