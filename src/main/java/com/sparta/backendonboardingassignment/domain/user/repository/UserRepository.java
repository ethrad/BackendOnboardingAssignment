package com.sparta.backendonboardingassignment.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.backendonboardingassignment.domain.user.entity.User;

import jakarta.validation.constraints.NotBlank;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);

	boolean existsByUsername(@NotBlank String username);
}
