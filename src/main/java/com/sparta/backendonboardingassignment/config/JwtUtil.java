package com.sparta.backendonboardingassignment.config;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.security.SignatureException;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.sparta.backendonboardingassignment.domain.user.enums.UserRole;
import com.sparta.backendonboardingassignment.common.exception.ServerException;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {

	private static final String BEARER_PREFIX = "Bearer ";
	private static final long TOKEN_TIME = 60 * 60 * 1000L; // 60분

	public static final String AUTHORIZATION_HEADER = "Authorization";

	@Value("${jwt.secret.key}")
	private String secretKey;
	private Key key;
	private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

	@PostConstruct
	public void init() {
		byte[] bytes = Base64.getDecoder().decode(secretKey);
		key = Keys.hmacShaKeyFor(bytes);
	}

	public String createToken(Long userId, String username, List<UserRole> userRoles) {
		Date date = new Date();

		return BEARER_PREFIX +
			Jwts.builder()
				.setSubject(String.valueOf(userId))
				.claim("username", username)
				.claim("userRoles", userRoles)
				.setExpiration(new Date(date.getTime() + TOKEN_TIME))
				.setIssuedAt(date) // 발급일
				.signWith(key, signatureAlgorithm) // 암호화 알고리즘
				.compact();
	}

	public String substringToken(String tokenValue) {
		if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
			return tokenValue.substring(7);
		}
		throw new ServerException("Not Found Token");
	}

	public Claims extractClaims(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

	public void addJwtToCookie(String token, HttpServletResponse res) {
		try {
			token = URLEncoder.encode(token, "utf-8").replaceAll("\\+", "%20"); // Cookie Value 에는 공백이 불가능해서 encoding 진행

			Cookie cookie = new Cookie(AUTHORIZATION_HEADER, token); // Name-Value
			cookie.setPath("/");

			// Response 객체에 Cookie 추가
			res.addCookie(cookie);
		} catch (UnsupportedEncodingException e) {
			throw new ServerException(e.getMessage());
		}
	}

	public String getTokenFromRequest(HttpServletRequest req) {
		Cookie[] cookies = req.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(AUTHORIZATION_HEADER)) {
					try {
						return URLDecoder.decode(cookie.getValue(), "UTF-8"); // Encode 되어 넘어간 Value 다시 Decode
					} catch (UnsupportedEncodingException e) {
						return null;
					}
				}
			}
		}
		return null;
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (SecurityException | MalformedJwtException e) {
			throw new ServerException("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
		} catch (ExpiredJwtException e) {
			throw new ServerException("Expired JWT token, 만료된 JWT token 입니다.");
		} catch (UnsupportedJwtException e) {
			throw new ServerException("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
		} catch (IllegalArgumentException e) {
			throw new ServerException("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
		}
	}
}
